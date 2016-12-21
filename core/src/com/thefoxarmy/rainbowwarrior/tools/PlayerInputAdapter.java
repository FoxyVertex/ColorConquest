package com.thefoxarmy.rainbowwarrior.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.sprites.Player;

/**
 * Processes the input to move the player
 */
public class PlayerInputAdapter extends InputAdapter implements InputProcessor {

    private float currentJumpLength = 0;
<<<<<<< HEAD
=======
    private float forceApplied = 55;
>>>>>>> origin/master
    private boolean isSpacePreviousPressed = false;

    private boolean backKeyPrev = false;
    private boolean forwardKeyPrev = false;
    private boolean canJump = true;
<<<<<<< HEAD

    /**
     * Sets the classes player variable to the player
     *
     * @param player the instance of the local player for handling input
     */
    public PlayerInputAdapter(Player player) {
        this.player = player;
    }
=======
>>>>>>> origin/master

    /**
     * Handles all of the input
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public void handleInput(float delta) {

<<<<<<< HEAD
        boolean isSpacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        float maxJumpForceLength = 0.2f;
        if (isSpacePressed) {
=======

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
>>>>>>> origin/master
            currentJumpLength += delta;

            if (currentJumpLength >= maxJumpForceLength) canJump = false;

            isSpacePreviousPressed = true;
        } else {
            currentJumpLength = 0f;
            canJump = DynamicGlobals.gameScreen.player.body.getLinearVelocity().y == 0 && !isSpacePreviousPressed;
            isSpacePreviousPressed = false;
        }


        if (Gdx.input.isKeyJustPressed(Input.Keys.S))
            DynamicGlobals.gameScreen.player.body.applyLinearImpulse(new Vector2(0, -10f), DynamicGlobals.gameScreen.player.body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.D) && DynamicGlobals.gameScreen.player.body.getLinearVelocity().x <= 2) {
            DynamicGlobals.gameScreen.player.body.applyLinearImpulse(new Vector2(0.125f, 0), DynamicGlobals.gameScreen.player.body.getWorldCenter(), true);
            forwardKeyPrev = true;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.A) && forwardKeyPrev) {
            DynamicGlobals.gameScreen.player.body.applyLinearImpulse(new Vector2(0.125f, 0), DynamicGlobals.gameScreen.player.body.getWorldCenter(), true);
            forwardKeyPrev = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && DynamicGlobals.gameScreen.player.body.getLinearVelocity().x >= -2) {
            DynamicGlobals.gameScreen.player.body.applyLinearImpulse(new Vector2(-0.125f, 0), DynamicGlobals.gameScreen.player.body.getWorldCenter(), true);
            backKeyPrev = true;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.A) && backKeyPrev) {
            DynamicGlobals.gameScreen.player.body.applyLinearImpulse(new Vector2(0.125f, 0), DynamicGlobals.gameScreen.player.body.getWorldCenter(), true);
            backKeyPrev = false;
        }
        //NOTE: This is a debug feature that will likely be replaced by a debug console command in the final product
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            DynamicGlobals.gameScreen.player.body.setLinearVelocity(new Vector2(0, 0));
            DynamicGlobals.gameScreen.player.body.setTransform(DynamicGlobals.gameScreen.player.spawnPoint, DynamicGlobals.gameScreen.player.body.getAngle());
        }


        float forceApplied = 55;
        if (!(currentJumpLength >= maxJumpForceLength) && currentJumpLength > 0 && canJump)
            DynamicGlobals.gameScreen.player.body.applyLinearImpulse(new Vector2(0, forceApplied * delta), DynamicGlobals.gameScreen.player.body.getWorldCenter(), true);

    }
}