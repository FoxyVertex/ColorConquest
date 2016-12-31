package com.foxyvertex.colorconquest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.managers.Levels;
import com.foxyvertex.colorconquest.managers.UserPrefs;
import com.foxyvertex.colorconquest.screens.GameScreen;
import com.foxyvertex.colorconquest.screens.MenuScreen;
import com.foxyvertex.colorconquest.screens.SplashScreen;

/**
 * The main game class
 */
public class ColorConquest extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        Globals.game = this;
        batch = new SpriteBatch();
        Assets.load();
        UserPrefs.load();
        Levels.load();
        if (Finals.SKIP_TO_GAME) {
            new SplashScreen();
            new MenuScreen();
            setScreen(new GameScreen());
        } else {
            new GameScreen();
            new MenuScreen();
            setScreen(new SplashScreen());
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();

    }
}
