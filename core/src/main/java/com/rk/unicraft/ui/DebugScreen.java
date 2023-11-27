package com.rk.unicraft.ui;

import com.rk.unicraft.entity.Player;
import com.rk.unicraft.util.MathHelper;
import com.rk.unicraft.world.chunk.Chunk;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

@SuppressWarnings("unused")
public class DebugScreen implements HUDElement{

    private static StringBuilder sb;
    private final Label label;
    private final Stage stage;
    private final Player player;

    private int visibleEnts;

    public DebugScreen(Player player) {
        stage = new Stage();
        BitmapFont font = new BitmapFont();
        font.getData().setScale(1.6f);
        label = new Label("\n\n\n", new Label.LabelStyle(font, Color.WHITE));
        stage.addActor(label);
        label.setPosition(0, Gdx.graphics.getHeight()- label.getHeight() - 10);
        sb = new StringBuilder();
        this.player = player;
    }

    @Override
    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    @Override
    public void update() {
        Vector3 pPos = player.getPosition();

        sb.setLength(0);
        sb.append("FPS: ").append(Gdx.graphics.getFramesPerSecond());
       
        sb.append("\nX : ").append((int)pPos.x).append(" Y: ").append((int)pPos.y).append(" Z: ").append((int)pPos.z);
        label.setText(sb);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public int getVisibleEnts() {
        return visibleEnts;
    }

    public void setVisibleEnts(int visibleEnts) {
        this.visibleEnts = visibleEnts;
    }
}
