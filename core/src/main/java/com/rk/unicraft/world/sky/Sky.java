package com.rk.unicraft.world.sky;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Sky {
    private ModelInstance spriteInstance;
    private ModelBatch modelBatch;
    private PerspectiveCamera cam;
    

    public Sky(PerspectiveCamera cam) {
        this.cam = cam;
        modelBatch = new ModelBatch();
        //Texture texture = ;
       spriteInstance = createTexturedSphere(this.cam.far, 20, new Texture("skybox.png"));
       // spriteInstance = createTexturedCube(cam.far,
         //   new Texture("cubemap_0.png"),new Texture("cubemap_1.png"),new Texture("cubemap_2.png"),new Texture("cubemap_3.png"),
         //   new Texture("cubemap_4.png"),new Texture("cubemap_5.png"));
    }

    public void render() {
        
        
        // move the sprite in opposite direction
        spriteInstance.transform.setToTranslation(cam.position).scl(-1f);
        
        
        
        
        modelBatch.begin(cam);
        modelBatch.render(spriteInstance);
        modelBatch.end();
        
    }

    public void clear() {
        modelBatch.dispose();
        
    }

    private ModelInstance createTexturedCube(float size, Texture texture1,Texture texture2,Texture texture3,Texture texture4,
        Texture texture5,Texture texture6) {
        ModelBuilder modelBuilder = new ModelBuilder();
        Model model =
                modelBuilder.createBox(size,size,size,new Material(TextureAttribute.createDiffuse(texture1)),
                        Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        return new ModelInstance(model);
    }

    private ModelInstance createTexturedSphere(float radius, int divisions, Texture texture) {
        ModelBuilder modelBuilder = new ModelBuilder();
        Model model =
                modelBuilder.createSphere(
                        radius,
                        radius,
                        radius,
                        divisions,
                        divisions,
                        new Material(TextureAttribute.createDiffuse(texture)),
                        Usage.Position | Usage.Normal | Usage.TextureCoordinates);

        return new ModelInstance(model);
    }
}
