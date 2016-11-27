package com.thefoxarmy.rainbowwarrior;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thefoxarmy.rainbowwarrior.screens.MenuScreen;

/**
 * The main game class
 */
public class RainbowWarrior extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        Assets.load();
        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();

    }
}
