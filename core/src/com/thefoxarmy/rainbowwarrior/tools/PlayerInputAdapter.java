package com.thefoxarmy.rainbowwarrior.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.thefoxarmy.rainbowwarrior.sprites.Player;

/**
 * Processes the input to move the player
 */
public class PlayerInputAdapter extends InputAdapter implements InputProcessor {

    private float maxJumpForceLength = 0.175f;
    private float currentJumpLength = 0;
    private float forceApplied = 55;
    private boolean isSpacePressed = false;
    private boolean isSpacePreviousPressed = false;
    private Player player;
    private boolean backKeyPrev = false;
    private boolean forwardKeyPrev = false;
    private boolean canJump = true;
    /**
     * Sets the classes player variable to the player
     *
     * @param player the instance of the local player for handling input
     */
    public PlayerInputAdapter(Player player) {
        this.player = player;
    }

    /**
     * Handles all of the input
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public void handleInput(float delta) {

        isSpacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        if (isSpacePressed) {
            currentJumpLength += delta;

            if (currentJumpLength >= maxJumpForceLength) canJump = false;

            isSpacePreviousPressed = true;
        } else {
            currentJumpLength = 0f;
            canJump = player.body.getLinearVelocity().y == 0;
            isSpacePreviousPressed = false;
        }



        if (Gdx.input.isKeyJustPressed(Input.Keys.S))
            player.body.applyLinearImpulse(new Vector2(0, -10f), player.body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.body.getLinearVelocity().x <= 2) {
            player.body.applyLinearImpulse(new Vector2(0.125f, 0), player.body.getWorldCenter(), true);
            forwardKeyPrev = true;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.A) && forwardKeyPrev) {
            player.body.applyLinearImpulse(new Vector2(0.125f, 0), player.body.getWorldCenter(), true);
            forwardKeyPrev = false;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.body.getLinearVelocity().x >= -2) {
            player.body.applyLinearImpulse(new Vector2(-0.125f, 0), player.body.getWorldCenter(), true);
            backKeyPrev = true;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.A) && backKeyPrev) {
            player.body.applyLinearImpulse(new Vector2(0.125f, 0), player.body.getWorldCenter(), true);
            backKeyPrev = false;
        }
        //NOTE: This is a debug feature that will likely be replaced by a debug console command in the final product
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            player.body.setLinearVelocity(new Vector2(0, 0));
            player.body.setTransform(player.spawnPoint, player.body.getAngle());
        }
        if (!(currentJumpLength >= maxJumpForceLength) && currentJumpLength > 0 && canJump)
            player.body.applyLinearImpulse(new Vector2(0, forceApplied * delta), player.body.getWorldCenter(), true);

    }
}