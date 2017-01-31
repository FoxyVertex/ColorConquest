package com.foxyvertex.colorconquest.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.tools.Drawable;

/**
 * Created by aidan on 1/23/17.
 */
public class MobileController implements Drawable, InputProcessor {
    private PlayerInput inputManager;

    Viewport viewport;
    public Stage stage;
    OrthographicCamera cam;

    boolean superToggle = false;
    boolean upTouched, leftTouched, rightTouched;

    Image upImg;
    Image downImg;
    Image leftImg;
    Image rightImg;
    Image superImg;

    public MobileController(final PlayerInput inputManager){
        this.inputManager = inputManager;
        cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, Globals.game.batch);

        stage.addListener(new InputListener(){
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled()) {
                    Image colorInic = Globals.hudScene.colorIndicator;
                    if (x > colorInic.getImageX() && x < colorInic.getImageX() + colorInic.getWidth() && y > colorInic.getImageY() && y < colorInic.getImageY() + colorInic.getHeight()) {
                        inputManager.currentColorIndex += 1;
                        if (inputManager.currentColorIndex >= Globals.gameMan.player.colors.size)
                            inputManager.currentColorIndex = 0;
                        if (inputManager.currentColorIndex < 0)
                            inputManager.currentColorIndex = Globals.gameMan.player.colors.size - 1;
                        Globals.hudScene.updateData();
                        event.handle();
                    }
                }
                if (!event.isHandled()) {
                    Globals.gameMan.player.shoot(new Vector2(x, y));
                }
                return false;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled()) {

                }
            }
        });

        upImg = new Image(new Texture("flatDark25.png"));
        upImg.setSize(60, 60);
        upImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upTouched = false;
            }
        });

        rightImg = new Image(new Texture("flatDark24.png"));
        rightImg.setSize(60, 60);
        rightImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightTouched = false;
            }
        });

        leftImg = new Image(new Texture("flatDark23.png"));
        leftImg.setSize(60, 60);
        leftImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftTouched = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftTouched = false;
            }
        });

        superImg = new Image(new Texture("flatDark27.png"));
        superImg.setSize(60, 60);
        superImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                superToggle = !superToggle;
            }
        });

        Table outsideTable = new Table();
        Table bottomLeftTable = new Table();
        Table bottomMiddleTable = new Table();
        Table bottomRightTable = new Table();

        bottomLeftTable.left();
        bottomLeftTable.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
        bottomRightTable.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
        bottomMiddleTable.setSize(Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);

        bottomLeftTable.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight()).padRight(5);
        bottomLeftTable.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight()).padLeft(5);
        bottomLeftTable.padLeft(-290f);

        bottomRightTable.right();
        bottomRightTable.add(superImg).size(superImg.getWidth(), superImg.getHeight()).padBottom(5);
        bottomRightTable.row();
        bottomRightTable.add(upImg).size(upImg.getWidth(), upImg.getHeight()).padTop(5);
        bottomRightTable.padRight(-100f);

        outsideTable.pad(5,Gdx.graphics.getWidth()/2,140,5);
        outsideTable.add(bottomLeftTable);
        outsideTable.add(bottomMiddleTable).size(400, 50);
        outsideTable.add(bottomRightTable);

        stage.addActor(outsideTable);

    }

    public void draw(){
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public void handleInput() {
        inputManager.debugSuperAbilityPressed = superToggle || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        inputManager.backwardPressed = Gdx.input.isKeyPressed(Input.Keys.A) || leftTouched;
        inputManager.forwardPressed = Gdx.input.isKeyPressed(Input.Keys.D) || rightTouched;
        inputManager.jumpPressed = Gdx.input.isKeyPressed(Input.Keys.SPACE) || upTouched;
        inputManager.downPressed = Gdx.input.isKeyPressed(Input.Keys.S);
        inputManager.debugNextLevelPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        inputManager.debugZoomInPressed = Gdx.input.isKeyPressed(Input.Keys.EQUALS);
        inputManager.debugZoomOutPressed = Gdx.input.isKeyPressed(Input.Keys.MINUS);
        inputManager.debugSpawnpointPressed = Gdx.input.isKeyPressed(Input.Keys.F);
        inputManager.debugZoomInPressed = Gdx.input.isKeyPressed(Input.Keys.EQUALS);

        leftImg.setColor(inputManager.backwardPressed ? Color.GRAY : Color.WHITE);
        rightImg.setColor(inputManager.forwardPressed ? Color.GRAY : Color.WHITE);
        upImg.setColor(inputManager.jumpPressed ? Color.GRAY : Color.WHITE);
        superImg.setColor(inputManager.debugSuperAbilityPressed ? Color.GRAY : Color.WHITE);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}