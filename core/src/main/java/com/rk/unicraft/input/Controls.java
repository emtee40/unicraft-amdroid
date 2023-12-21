package com.rk.unicraft.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.rk.unicraft.config.RuntimeConfig;
import com.rk.unicraft.entity.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.rk.unicraft.ui.DebugScreen;

@SuppressWarnings("unused")
public class Controls implements InputProcessor {
    public Stage stage;
    public static final int screen_height = Gdx.graphics.getHeight();
    public static final int screen_width = Gdx.graphics.getWidth();
    private final Player player;

    public Controls(Player player) {
        this.player = player;
        this.stage = stage;

        stage = new Stage(new ScreenViewport());

        Skin mySkin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));
        Gdx.app.log("controls", "first button resgister");

        // forward button
        Button button_F = new Button(mySkin);
        button_F.setSize(100, 100);
        /* button_F.getStyle().imageUp =
                new TextureRegionDrawable(
                        new TextureRegion(new Texture(Gdx.files.internal("raw/dirt.png"))));
        button_F.getStyle().imageDown =
                new TextureRegionDrawable(
                        new TextureRegion(new Texture(Gdx.files.internal("raw/armor.png"))));*/

        button_F.addListener(
                new InputListener() {

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        player.movingForward = true;

                        return true;
                    }

                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {
                        player.movingForward = false;
                    }
                });
        // backward button
        Button button_B = new Button(mySkin);
        button_B.setSize(100, 100);

        button_B.addListener(
                new InputListener() {

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        // outputLabel.setText("Pressed Image Button");
                        player.movingBackward = true;
                        return true;
                    }

                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {
                        player.movingBackward = false;
                    }
                });
        // left button
        Button button_L = new Button(mySkin);
        button_L.setSize(100, 100);
        button_L.addListener(
                new InputListener() {

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        // outputLabel.setText("Pressed Image Button");
                        player.movingLeft = true;
                        return true;
                    }

                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {
                        player.movingLeft = false;
                    }
                });

        // right button
        Button button_R = new Button(mySkin);
        button_R.setSize(100, 100);
        button_R.addListener(
                new InputListener() {

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        // outputLabel.setText("Pressed Image Button");
                        player.movingRight = true;
                        return true;
                    }

                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {
                        player.movingRight = false;
                    }
                });
        // up
        Button button_U = new Button(mySkin);
        button_U.setSize(100, 100);

        button_U.addListener(
                new InputListener() {

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        // outputLabel.setText("Pressed Image Button");
                        player.movingUp = true;
                        player.jumpPressed = true;
                        // player.movingDown = true;
                        return true;
                    }

                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {

                        player.movingUp = false;
                        player.jumpPressed = false;
                        // player.movingDown = false;
                    }
                });
        // down
        Button button_D = new Button(mySkin);
        button_D.setSize(100, 100);
        button_D.addListener(
                new InputListener() {

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        // outputLabel.setText("Pressed Image Button");

                        //    RuntimeConfig.RENDER_MODE_SWITCH =
                        //  ++RuntimeConfig.RENDER_MODE_SWITCH % RuntimeConfig.RENDER_MODES.length;

                        return true;
                    }

                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {
                        player.isFlying = !player.isFlying;
                    }
                });
        
        Button button_V = new Button(mySkin);
        button_V.setSize(100, 100);
        button_V.addListener(
                new InputListener() {

                    @Override
                    public boolean touchDown(
                            InputEvent event, float x, float y, int pointer, int button) {
                        // outputLabel.setText("Pressed Image Button");

                        RuntimeConfig.RENDER_MODE_SWITCH =
                      ++RuntimeConfig.RENDER_MODE_SWITCH % RuntimeConfig.RENDER_MODES.length;

                        return true;
                    }

                    @Override
                    public void touchUp(
                            InputEvent event, float x, float y, int pointer, int button) {
                        
                    }
                });
        
        
        

        button_F.setPosition(Utils.percent(screen_width, 10), Utils.percent(screen_height, 45));
        button_B.setPosition(
                Utils.percent(screen_width, 10),
                Utils.percent(Utils.percent(screen_height, 45), 25));

        float middleYFloat = (button_F.getY() + button_B.getY()) / 2;
        int middleY = (int) middleYFloat;

        button_L.setPosition(Utils.percent(screen_width, 2), middleY);
        button_R.setPosition(Utils.percent(screen_width, 18), middleY);

        button_U.setPosition(Utils.percent(screen_width, 87), Utils.percent(screen_height, 40));
        button_D.setPosition(Utils.percent(screen_width, 87), Utils.percent(screen_height, 20));

        button_V.setPosition(Utils.percent(screen_width,87),Utils.percent(screen_height,60));

        stage.addActor(button_F);
        stage.addActor(button_B);
        stage.addActor(button_L);
        stage.addActor(button_R);
        stage.addActor(button_U);
        stage.addActor(button_D);
        stage.addActor(button_V);

        /*
        button_U is actually down button
        and Button_D is jump button its reversed

        */
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    private int lastCursorX = Gdx.graphics.getWidth() / 2;
    private int lastCursorY = Gdx.graphics.getHeight() / 2;
    private final float MOUSE_SENSITIVITY = 0.2f;

    @Override
    public boolean touchDragged(int screenX, int screenY, int touch_pointer) {
        float dx = lastCursorX - screenX;
        float dy = lastCursorY - screenY;

        // Rotate the camera based on accumulated values
        player.rotateYaw(dx * MOUSE_SENSITIVITY);
        player.rotatePitch(dy * MOUSE_SENSITIVITY);

        // Update lastCursorX and lastCursorY without resetting them
        lastCursorX = screenX;
        lastCursorY = screenY;

        return true;
    }

    public void clear() {
        stage.dispose();
    }

    @Override
    public boolean keyDown(int arg0) {
        return false;
    }

    @Override
    public boolean keyUp(int arg0) {
        return false;
    }

    @Override
    public boolean keyTyped(char arg0) {
        return false;
    }

    @Override
    public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
        lastCursorX = arg0;
        lastCursorY = arg1;
        // implement attack here
        return true;
    }

    @Override
    public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
        return false;
    }

    @Override
    public boolean mouseMoved(int arg0, int arg1) {
        return false;
    }

    @Override
    public boolean scrolled(float arg0, float arg1) {
        player.increaseSelectedIndex((int) arg1); // Adjust as needed
        return true;
    }

    private class Utils {
        public static int percent(int num, int percentage) {
            return (int) ((percentage / 100.0) * num);
        }
    }
}
