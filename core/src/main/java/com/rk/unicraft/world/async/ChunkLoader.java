package com.rk.unicraft.world.async;

import com.badlogic.gdx.Gdx;
import com.rk.unicraft.entity.Player;
import com.badlogic.gdx.math.Vector2;
import com.rk.unicraft.util.SimpleVector2;
import com.rk.unicraft.world.World;
import com.rk.unicraft.world.chunk.Chunk;
import com.rk.unicraft.world.chunk.ChunkModel;
import com.rk.unicraft.world.generator.MapGenerator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkLoader extends Thread {

    // private volatile boolean doReset;
    private final int viewDistance;
    private MapGenerator mg;
    private Lock chunkLock;
    private SimpleVector2 playerChunkPos;
    private HashMap<SimpleVector2, Chunk> loadedChunks;
    private final SimpleVector2 tmp = new SimpleVector2();
    //  private final SimpleVector2 tmpPlayerPos = new SimpleVector2();
    private int start;
    private int end;
    private final int seed;
    // public static volatile boolean pcp = false;
    int x, z;
    private Player player;
    
    
    public ChunkLoader(
            Lock chunkLock,
            int viewDistance,
            int seed,
            Player player,
            SimpleVector2 playerChunkPos,
            HashMap<SimpleVector2, Chunk> loadedChunks) {

        this.viewDistance = viewDistance + 2;
        this.seed = seed;
        this.chunkLock = chunkLock;
        this.playerChunkPos = playerChunkPos;
        this.loadedChunks = loadedChunks;
        this.player = player;
    }

    @Override
    public void run() {
        mg = new MapGenerator(seed);
        genStartingChunks();

        while (!isInterrupted()) {

            try {
                if (chunkLock.tryLock(20, TimeUnit.MILLISECONDS)) {
                    try {
                        x = playerChunkPos.x;
                        z = playerChunkPos.y;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        chunkLock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            genChunksAround(x, z);
            
            
        }
    }

    private void genChunksAround(int x, int z) {

        start = -viewDistance;
        end = viewDistance;

        for (int i = start + x; i < end + x; i++) {
            for (int j = start + z; j < end + z; j++) {
                tmp.x = i;
                tmp.y = j;

                chunkLock.lock();
                if (loadedChunks.get(tmp) == null) {
                    loadedChunks.put(tmp, new Chunk(mg, i, j));
                }
                chunkLock.unlock();
            }
        }
    }
    
    private void genStartingChunks() {

        int sz = 3;
        int szn = -3;

        chunkLock.lock();

        try {
            for (int i = szn; i < sz; i++) {
                for (int j = szn; j < sz; j++) {
                    loadedChunks.put(new SimpleVector2(i, j), new Chunk(mg, i, j));
                }
            }
        } finally {
            chunkLock.unlock();
        }
    }

    /*  public void reset() {
        resetLock.lock();
        try {
            doReset = true;
        } finally {
            resetLock.unlock();
        }
    }*/
}
