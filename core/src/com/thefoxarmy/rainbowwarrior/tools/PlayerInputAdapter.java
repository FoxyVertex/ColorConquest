package com.thefoxarmy.rainbowwarrior.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.thefoxarmy.rainbowwarrior.screens.PlayScreen;

public class PlayerInputAdapter extends InputAdapter implements InputProcessor {


    private PlayScreen screen;

    public PlayerInputAdapter(PlayScreen screen) {
        this.screen = screen;
    }

    public void handleInput() {
        Vector2 impulse;
        float vel = 0.1f;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            impulse = new Vector2(vel, 0);
            screen.player.body.applyLinearImpulse(impulse, screen.player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            impulse = new Vector2(-vel, 0);
            screen.player.body.applyLinearImpulse(impulse, screen.player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && screen.player.body.getLinearVelocity().y == 0) {
            impulse = new Vector2(0, 4);
            screen.player.body.applyLinearImpulse(impulse, screen.player.body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            impulse = new Vector2(0, -vel);
            screen.player.body.applyLinearImpulse(impulse, screen.player.body.getWorldCenter(), true);
        }


    }

}