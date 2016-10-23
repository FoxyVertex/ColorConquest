package com.thefoxarmy.rainbowwarrior;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.thefoxarmy.rainbowwarrior.screens.MenuScreen;

public class RainbowWarrior extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();

        setScreen(new MenuScreen(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.7f, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //batch.begin();
        super.render();
        //batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
