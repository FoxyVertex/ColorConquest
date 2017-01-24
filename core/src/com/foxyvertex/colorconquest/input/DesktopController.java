package com.foxyvertex.colorconquest.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Levels;

/**
 * Processes the input to move the player
 */
public class DesktopController extends InputAdapter implements InputProcessor {

    private PlayerInput inputManager;

    private boolean isSpacePreviousPressed = false;
    private boolean backKeyPrev = false;
    private boolean forwardKeyPrev = false;

    /**
     * Sets the classes player variable to the player
     */
    public DesktopController(PlayerInput inputManager) {
        this.inputManager = inputManager;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Globals.gameMan.player.shoot(new Vector2(screenX, screenY));

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        inputManager.currentColorIndex += amount;
        if (inputManager.currentColorIndex >= Globals.gameMan.player.colors.size)
            inputManager.currentColorIndex = 0;
        if (inputManager.currentColorIndex < 0)
            inputManager.currentColorIndex = Globals.gameMan.player.colors.size - 1;
        //Uncomment for lulz!!!
        Globals.gameMan.player.setColor(Globals.gameMan.player.colors.get(inputManager.currentColorIndex));
        Globals.gameMan.player.setSelectedColor(Globals.gameMan.player.colors.get(inputManager.currentColorIndex));

        return super.scrolled(amount);
    }

    /**
     * Handles all of the input
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public void handleInput(float delta) {
        inputManager.backwardPressed = Gdx.input.isKeyPressed(Input.Keys.A);
        inputManager.forwardPressed = Gdx.input.isKeyPressed(Input.Keys.D);
        inputManager.jumpPressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        inputManager.downPressed = Gdx.input.isKeyPressed(Input.Keys.S);
        inputManager.debugNextLevelPressed = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        inputManager.debugZoomInPressed = Gdx.input.isKeyPressed(Input.Keys.EQUALS);
        inputManager.debugZoomOutPressed = Gdx.input.isKeyPressed(Input.Keys.MINUS);
        inputManager.debugSpawnpointPressed = Gdx.input.isKeyPressed(Input.Keys.F);
        inputManager.debugSuperAbilityPressed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);
        inputManager.debugZoomInPressed = Gdx.input.isKeyPressed(Input.Keys.EQUALS);
    }


}