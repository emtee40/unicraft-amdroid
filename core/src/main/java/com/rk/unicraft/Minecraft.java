package com.rk.unicraft;

import com.badlogic.gdx.InputMultiplexer;
import com.rk.unicraft.config.RuntimeConfig;
import com.rk.unicraft.entity.Player;
// import com.rk.unicraft.input.DefaultInputProcessor;
import com.rk.unicraft.render.ChunkRenderer;
import com.rk.unicraft.render.SkyboxRenderer;
import com.rk.unicraft.ui.DebugScreen;
import com.rk.unicraft.ui.HUD;
import com.rk.unicraft.ui.Hotbar;
import com.rk.unicraft.world.World;
import com.rk.unicraft.world.chunk.ChunkModel;
import com.rk.unicraft.world.sky.Skybox;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.utils.TimeUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.rk.unicraft.input.Controls;

public class Minecraft implements ApplicationListener {

    private Player player;
    private HUD hud;
    private DebugScreen debugScreen;
    private Hotbar hotbar;
    private World world;
    private ChunkRenderer chunkRenderer;
    private Controls controls;

    @Override
    public void create() {
        PerspectiveCamera cam =
                new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.near = 0.3f;
        cam.far = 300f;

        Environment environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.51f, 0.5f, 0.5f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        ModelBatch modelBatch = new ModelBatch();
        chunkRenderer = new ChunkRenderer(modelBatch, environment, cam);

        world = World.init((int) TimeUtils.millis());
        player = new Player(cam);
        world.setPlayer(player);

        hud = new HUD(player);
        debugScreen = hud.getDebugScreen();
        hotbar = hud.getHotbar();
        controls = new Controls(player);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(controls.stage);
        multiplexer.addProcessor(controls);

        Gdx.input.setInputProcessor(multiplexer);
        // DefaultInputProcessor camController = new DefaultInputProcessor(player);
        // Gdx.input.setInputProcessor(controls.stage);
        // Gdx.input.setCursorCatched(true);
    }

    private void generateMap() {
        world.reset((int) TimeUtils.millis());
    }

    @Override
    public void resize(int width, int height) {
        hud.resize(width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(
                GL20.GL_COLOR_BUFFER_BIT
                        | GL20.GL_DEPTH_BUFFER_BIT
                        | (Gdx.graphics.getBufferFormat().coverageSampling
                                ? GL20.GL_COVERAGE_BUFFER_BIT_NV
                                : 0));

        // Face Culling
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        Gdx.gl.glCullFace(GL20.GL_FRONT);

        player.update();

        // List<ChunkModel> chunksToRender = world.getModels();

        // render World
        // int visibleChunks =
        chunkRenderer.render(world.getModels());

        // render HUD
        hotbar.setSelectedIndex(player.getSelectedIndex());
        // debugScreen.setVisibleEnts(visibleChunks);

        hud.update();

        /*  if (RuntimeConfig.DO_MAP_REBUILD) {
            generateMap();
            RuntimeConfig.DO_MAP_REBUILD = false;
        }*/

        // render buttons
        controls.render();
    }

    @Override
    public void dispose() {
        chunkRenderer.cleanup();
        world.cleanUp();
        hud.dispose();
        controls.clear();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}
}
