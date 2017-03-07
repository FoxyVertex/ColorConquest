package com.foxyvertex.colorconquest.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.system.PlayerSystem;

/**
 * Created by aidan on 1/23/17.
 */
public class MobileController implements Drawable, InputProcessor {
    public Stage stage;
    private Viewport viewport;
    private boolean firingModeToggle = false;
    private boolean upTouched, leftTouched, rightTouched;
    private Image upImg;
    private Image leftImg;
    private Image rightImg;
    private Image superImg;
    private PlayerSystem inputManager;

    public MobileController(final PlayerSystem inputManager) {
        this.inputManager = inputManager;
        OrthographicCamera cam = new OrthographicCamera();
        viewport = new FitViewport(800, 480, cam);
        stage = new Stage(viewport, Globals.game.batch);

        stage.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled()) {
                    // TODO: 2/18/2017 If the player touches the color meter, then move to the next color.
                }
                if (!event.isHandled()) {
                    inputManager.shoot(new Vector2(x, y));
                }
                return false;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!event.isHandled()) {

                }
            }
        });

        upImg = new Image(new Texture("gfx/UI/up.png"));
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

        rightImg = new Image(new Texture("gfx/UI/right.png"));
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

        leftImg = new Image(new Texture("gfx/UI/left.png"));
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

        superImg = new Image(new Texture("gfx/UI/dot.png"));
        superImg.setSize(60, 60);
        superImg.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                firingModeToggle = !firingModeToggle;
            }
        });

        Table outsideTable = new Table();
        Table bottomLeftTable = new Table();
        Table bottomMiddleTable = new Table();
        Table bottomRightTable = new Table();

        bottomLeftTable.left();
        bottomLeftTable.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);
        bottomRightTable.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);
        bottomMiddleTable.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);

        bottomLeftTable.add(leftImg).size(leftImg.getWidth(), leftImg.getHeight()).padRight(5);
        bottomLeftTable.add(rightImg).size(rightImg.getWidth(), rightImg.getHeight()).padLeft(5);
        bottomLeftTable.padLeft(-290f);

        bottomRightTable.right();
        bottomRightTable.add(superImg).size(superImg.getWidth(), superImg.getHeight()).padBottom(5);
        bottomRightTable.row();
        bottomRightTable.add(upImg).size(upImg.getWidth(), upImg.getHeight()).padTop(5);
        bottomRightTable.padRight(-100f);

        outsideTable.pad(5, Gdx.graphics.getWidth() / 2, 140, 5);
        outsideTable.add(bottomLeftTable);
        outsideTable.add(bottomMiddleTable).size(400, 50);
        outsideTable.add(bottomRightTable);

        stage.addActor(outsideTable);

    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void handleInput() {
        inputManager.firingModePressed = firingModeToggle || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

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

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        stage.act();
        stage.draw();
    }

    @Override
    public float getLeftWidth() {
        return 0;
    }

    @Override
    public void setLeftWidth(float leftWidth) {

    }

    @Override
    public float getRightWidth() {
        return 0;
    }

    @Override
    public void setRightWidth(float rightWidth) {

    }

    @Override
    public float getTopHeight() {
        return 0;
    }

    @Override
    public void setTopHeight(float topHeight) {

    }

    @Override
    public float getBottomHeight() {
        return 0;
    }

    @Override
    public void setBottomHeight(float bottomHeight) {

    }

    @Override
    public float getMinWidth() {
        return 0;
    }

    @Override
    public void setMinWidth(float minWidth) {

    }

    @Override
    public float getMinHeight() {
        return 0;
    }

    @Override
    public void setMinHeight(float minHeight) {

    }
}