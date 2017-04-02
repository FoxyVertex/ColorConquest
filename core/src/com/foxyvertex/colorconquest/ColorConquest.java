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
import com.kotcrab.vis.ui.VisUI;

/**
 * The main game class
 */
public class ColorConquest extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        Globals.game = this;
        batch = new SpriteBatch();
        VisUI.load();
        Assets.load();
        UserPrefs.load();
        Levels.load();
        Assets.volumeChanged();
        switch (Finals.debugMode) {
            case SKIP_SPLASH:
                new GameScreen();
                setScreen(new MenuScreen());
                new SplashScreen();
                break;
            case SKIP_TO_GAME:
                new SplashScreen();
                new MenuScreen();
                setScreen(new GameScreen());
                break;
            case NORMAL:
                new GameScreen();
                new MenuScreen();
                setScreen(new SplashScreen());
                break;
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
        super.dispose();
        batch.dispose();
        Assets.dispose();
        UserPrefs.dispose();
        VisUI.dispose();
    }
}