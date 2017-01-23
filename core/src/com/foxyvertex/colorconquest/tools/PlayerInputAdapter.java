package com.foxyvertex.colorconquest.tools;

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
public class PlayerInputAdapter extends InputAdapter implements InputProcessor {

    int currentColorIndex = 0;
    private float currentJumpLength = 0;
    private boolean isSpacePreviousPressed = false;
    private boolean backKeyPrev = false;
    private boolean forwardKeyPrev = false;
    private boolean canJump = true;

    /**
     * Sets the classes player variable to the player
     */
    public PlayerInputAdapter() {
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
        currentColorIndex += amount;
        if (currentColorIndex >= Globals.gameMan.player.colors.size)
            currentColorIndex = 0;
        if (currentColorIndex < 0)
            currentColorIndex = Globals.gameMan.player.colors.size - 1;
        //Uncomment for lulz!!!
        //Globals.gameMan.player.setColor(Globals.gameMan.player.colors.get(currentColorIndex));
        Globals.gameMan.player.setSelectedColor(Globals.gameMan.player.colors.get(currentColorIndex));

        return super.scrolled(amount);
    }

    /**
     * Handles all of the input
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public void handleInput(float delta) {

        float maxJumpForceLength = 0.2f;

        //DEBUG JUNK
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            Globals.gameMan.player.blue += 1;
            Globals.hudScene.updateData();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS))
            Globals.gameMan.cam.zoom += 3 / Finals.PPM;
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS))
            Globals.gameMan.cam.zoom -= 3 / Finals.PPM;
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            Globals.gameMan.player.body.setLinearVelocity(new Vector2(0, 0));
            Globals.gameMan.player.body.setTransform(Globals.gameMan.player.spawnPoint, Globals.gameMan.player.body.getAngle());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            Levels.Level nextLevel = Globals.gameMan.currentLevel.nextLevel;
            Globals.gameMan.switchLevel(nextLevel);
        }

        Globals.gameMan.player.isFiring = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            currentJumpLength += delta;

            if (currentJumpLength >= maxJumpForceLength) canJump = false;

            isSpacePreviousPressed = true;
        } else {
            currentJumpLength = 0f;
            canJump = Globals.gameMan.player.body.getLinearVelocity().y == 0 && !isSpacePreviousPressed;
            isSpacePreviousPressed = false;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_RIGHT)) {
            Globals.gameMan.player.runSpeed = Globals.gameMan.player.maxRunSpeed;
            Globals.gameMan.player.jumpForce = Globals.gameMan.player.maxJumpForce;
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.S))
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(0, -10f), Globals.gameMan.player.body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.D) && Globals.gameMan.player.body.getLinearVelocity().x <= 2) {
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(Globals.gameMan.player.runSpeed, 0), Globals.gameMan.player.body.getWorldCenter(), true);
            forwardKeyPrev = true;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.A) && forwardKeyPrev) {
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(-Globals.gameMan.player.runSpeed, 0), Globals.gameMan.player.body.getWorldCenter(), true);
            forwardKeyPrev = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && Globals.gameMan.player.body.getLinearVelocity().x >= -2) {
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(-Globals.gameMan.player.runSpeed, 0), Globals.gameMan.player.body.getWorldCenter(), true);
            backKeyPrev = true;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.A) && backKeyPrev) {
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(-Globals.gameMan.player.runSpeed, 0), Globals.gameMan.player.body.getWorldCenter(), true);
            backKeyPrev = false;
        }


        if (!(currentJumpLength >= maxJumpForceLength) && currentJumpLength > 0 && canJump)
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(0, Globals.gameMan.player.jumpForce * delta), Globals.gameMan.player.body.getWorldCenter(), true);

    }


}