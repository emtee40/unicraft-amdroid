package com.rk.unicraft.render;

import com.rk.unicraft.world.GameObject;
import com.rk.unicraft.world.chunk.ChunkModel;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

import java.util.List;

public class ChunkRenderer extends Renderer<List<ChunkModel>> {

    private final ModelBatch modelBatch;
    private final Environment environment;

    public ChunkRenderer(ModelBatch modelBatch, Environment environment, Camera camera) {
        super(camera);
        this.modelBatch = modelBatch;
        this.environment = environment;
    }

    @Override
    public int render(List<ChunkModel> targets) {
      //  int count = 0;

        modelBatch.begin(camera);

        for (ChunkModel target : targets) {
            GameObject fl = target.getChunkModelFull();
            GameObject tr = target.getChunkModelTransparent();

            if (isVisible(fl)) {
                modelBatch.render(fl, environment);
               // ++count;
            }

            if (tr != null) {
                if (isVisible(tr)) {
                    modelBatch.render(tr, environment);
                   // ++count;
                }
            }
        }

        modelBatch.end();

       // return count;
        return 0;
    }

    @Override
    public void cleanup() {
        modelBatch.dispose();
    }
}
