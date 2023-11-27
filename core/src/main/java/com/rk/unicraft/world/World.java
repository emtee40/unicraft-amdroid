package com.rk.unicraft.world;

import com.rk.unicraft.entity.Player;
import com.rk.unicraft.render.Renderer;
import com.rk.unicraft.util.MathHelper;
import com.badlogic.gdx.math.Vector2;
import com.rk.unicraft.util.SimpleVector2;
import com.rk.unicraft.world.async.ChunkLoader;
import com.rk.unicraft.world.GameObject;
import com.rk.unicraft.world.async.ChunkLoader;
import com.rk.unicraft.world.chunk.ChunkModel;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import java.util.List;
import com.rk.unicraft.world.block.Block;
import com.rk.unicraft.world.chunk.Chunk;
import com.rk.unicraft.world.chunk.ChunkModel;
import com.rk.unicraft.world.generator.MapGenerator;
import com.badlogic.gdx.math.Vector3;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.rk.unicraft.world.chunk.Chunk.CHUNK_SIZE;


@SuppressWarnings("unused")
public class World extends Renderer<List<ChunkModel>>{
    // init
    private static World world;
    private int viewDistance; // In chunks
    private Player player;

    private ChunkLoader chunkloader;
    public static Lock chunkLock;
    private SimpleVector2 playerChunkPos;
    public List<ChunkModel> chunks;
    private HashMap<SimpleVector2, Chunk> loadedChunks;
    private HashMap<Chunk, ChunkModel> chunkModels;
    private final ModelBatch modelBatch;
    private final Environment environment;

    public World(ModelBatch modelBatch, Environment environment, Camera camera, int seed) {
        super(camera);
        this.modelBatch = modelBatch;
        this.environment = environment;
        loadedChunks = new HashMap<>();
        chunkModels = new HashMap<>();
        chunkLock = new ReentrantLock();
        playerChunkPos = new SimpleVector2();
        viewDistance = 2;
        chunks = new LinkedList<>();
        chunkloader = new ChunkLoader(chunkLock, viewDistance, seed, playerChunkPos, loadedChunks);
        chunkloader.start();
    }

    public static synchronized World getInstance() {
        return world;
    }

    public static World init(
            ModelBatch modelBatch, Environment environment, Camera camera, int seed) {
        world = new World(modelBatch, environment, camera, seed);

        return world;
    }

    /*  public MapGenerator getMapGenerator() {
        return mg;
    }*/

    public void setPlayer(Player player) {
        this.player = player;
    }

   /* private int getSeed() {
        return mg.getSeed();
    }*/

  /*  private void putChunk(Chunk chunk) {
        chunkLock.lock();
        loadedChunks.put(new SimpleVector2(chunk.getX(), chunk.getZ()), chunk);
        chunkLock.unlock();

        ChunkModel cm = new ChunkModel(chunk);
        chunkModels.put(chunk, cm);
    }*/

    private static final SimpleVector2 tmpPlayerPos = new SimpleVector2();

    

    public Chunk getChunkAt(int x, int z) {
        chunkLock.lock();
        Chunk c = loadedChunks.getOrDefault(new SimpleVector2(x, z), null);
        chunkLock.unlock();
        return c;
    }

    public Collection<ChunkModel> getLoadedChunkModels() {
        return chunkModels.values();
    }

    public Block getBlockAt(Vector3 pos) {
        return getBlockAt((int) pos.x, (int) pos.y, (int) pos.z);
    }

    public Chunk setBlockAt(Block b, boolean forceUpdate) {
        return setBlockAt(b, forceUpdate, false);
    }

    public Chunk setBlockAt(Block b, boolean forceUpdate, boolean delete) {
        int x = b.getPos().x, y = b.getPos().y, z = b.getPos().z;

        // Finde Chunk in dem der Block liegt
        int chunkX = Math.floorDiv(x, CHUNK_SIZE), chunkZ = Math.floorDiv(z, CHUNK_SIZE);
        Chunk c = getChunkAt(chunkX, chunkZ); // TODO Chunk generieren, wenn noch nicht existiert

        if (c != null) {
            // Hol den Block aus dem gefundenen Chunk
            if (b.getType() == -1 || delete) {
                c.setBlockAt(Math.floorMod(x, CHUNK_SIZE), y, Math.floorMod(z, CHUNK_SIZE), null);
            } else c.setBlockAt(Math.floorMod(x, CHUNK_SIZE), y, Math.floorMod(z, CHUNK_SIZE), b);

            // Update Chunk
            if (forceUpdate) {
                chunkModels.get(c).update(c);
            }
        }
        return c;
    }

    public void delBlock(Block b) {
        setBlockAt(b, true, true);
    }

    public void setBlocks(List<Block> blocks) {
        List<Chunk> chunksToUpdate = new LinkedList<>();
        for (Block block : blocks) {
            Chunk c = setBlockAt(block, false);

            if (c != null) {
                if (!chunksToUpdate.contains(c)) chunksToUpdate.add(c);
            }
        }

        /*Alle Chunks auf einmal updaten*/
        for (Chunk chunk : chunksToUpdate) {
            chunkModels.get(chunk).update(chunk);
        }
    }

    public Chunk getChunkAt(SimpleVector2 chunkPosition) {
        return getChunkAt(chunkPosition.x, chunkPosition.y);
    }

    public Block getBlockAt(float x, float y, float z) {
        // Finde Chunk in dem der Block liegt
        int chunkX = MathHelper.floatDiv(x, CHUNK_SIZE),
                chunkZ = MathHelper.floatDiv(z, CHUNK_SIZE);
        Chunk c = getChunkAt(chunkX, chunkZ);
        // System.out.printf("getting chunk at %d %d", chunkX, chunkZ);
        // System.out.println(" --> " + c);

        // Hol den Block aus dem gefundenen
        // Chunk Koordinate ist (x % CHUNK_SIZE), modulo bringt aber probleme --> floorMod
        if (c == null) return null;
        int localX = (int) MathHelper.floatMod(x, CHUNK_SIZE);
        int localY = (int) MathHelper.floatMod(z, CHUNK_SIZE);
        return c.getBlockAt(localX, (int) y, localY);
    }

    public Block getBlockRelativeTo(Vector3 pos, float x, float y, float z) {
        return getBlockAt(pos.x + x, pos.y + y, pos.z + z);
    }

    public void reset(int seed) {
        /* mapGenerator.setSeed(seed);
        chunkLock.lock();
        loadedChunks.clear();
        chunkLock.unlock();
        chunkModels.clear();
        chunkloader.reset();*/
    }

    public void cleanUp() {
       chunkloader.interrupt();
        for (ChunkModel chunk : getLoadedChunkModels()) {
            chunk.getChunkModelFull().model.dispose();
            if (chunk.getChunkModelTransparent() != null) {
                chunk.getChunkModelTransparent().model.dispose();
            }
        }
    }

    @Override
    public void cleanup() {
        modelBatch.dispose();
    }

    @Override
    protected boolean isVisible(final GameObject instance) {
        return super.isVisible(instance);
    }

    public void render() {
        chunkLock.lock();
        player.getChunkPosition(tmpPlayerPos);
        chunkLock.unlock();
        playerChunkPos.set(tmpPlayerPos);
        
       chunks.clear();
       chunkLock.lock();
        for (Chunk chunk : loadedChunks.values()) {
            if (!chunkModels.containsKey(chunk)) {
                chunkModels.put(chunk, new ChunkModel(chunk));
            }
            if (Math.abs(tmpPlayerPos.x - chunk.getX()) <= viewDistance
                    && Math.abs(tmpPlayerPos.y - chunk.getZ()) <= viewDistance)
                chunks.add(chunkModels.get(chunk));
        }
        chunkLock.unlock();
        
        modelBatch.begin(camera);
        for (ChunkModel target : chunks) {
            GameObject fl = target.getChunkModelFull();
            GameObject tr = target.getChunkModelTransparent();

            if (isVisible(fl)) {
                modelBatch.render(fl, environment);
            }
            if(tr != null && isVisible(tr)){
                modelBatch.render(tr, environment);
            }
            
        }

        modelBatch.end();
    }
}
