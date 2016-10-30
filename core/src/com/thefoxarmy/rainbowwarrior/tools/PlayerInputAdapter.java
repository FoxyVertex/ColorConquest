package com.thefoxarmy.rainbowwarrior.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.thefoxarmy.rainbowwarrior.screens.PlayScreen;
import com.thefoxarmy.rainbowwarrior.sprites.Player;

import static sun.audio.AudioPlayer.player;

public class PlayerInputAdapter extends InputAdapter implements InputProcessor {


    private PlayScreen screen;
    private Player player;

    public PlayerInputAdapter(PlayScreen screen) {
        this.screen = screen;
    }

    public void handleInput() {

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && screen.player.body.getLinearVelocity().y == 0)
            screen.player.body.applyLinearImpulse(new Vector2(0, 4f), screen.player.body.getWorldCenter(), true);
        if (Gdx.input.isKeyJustPressed(Input.Keys.S))
            screen.player.body.applyLinearImpulse(new Vector2(0, -10f), screen.player.body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.D) && screen.player.body.getLinearVelocity().x <= 2)
            screen.player.body.applyLinearImpulse(new Vector2(0.1f, 0), screen.player.body.getWorldCenter(), true);
        if (Gdx.input.isKeyPressed(Input.Keys.A) && screen.player.body.getLinearVelocity().x >= -2)
            screen.player.body.applyLinearImpulse(new Vector2(-0.1f, 0), screen.player.body.getWorldCenter(), true);

    }

}