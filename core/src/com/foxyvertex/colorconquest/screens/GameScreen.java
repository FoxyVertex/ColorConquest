package com.foxyvertex.colorconquest.screens;

import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.game.GameManager;
import com.foxyvertex.colorconquest.scenes.GameReadyScreen;
import com.foxyvertex.colorconquest.scenes.Hud;
import com.foxyvertex.colorconquest.scenes.PauseMenu;

/**
 * Handles tiledMap loading, all of the objects in the world, and anything outside of a menu
 */
public class GameScreen extends Screen {
    GameManager gameManager;

    /**
     * Sets up the GameScreen for being cached
     */
    public GameScreen() {
        Globals.pauseMenuScene = new PauseMenu(this);
        Globals.gameReadyScene = new GameReadyScreen(this);
        Globals.hudScene = new Hud(this);
        Globals.gameScreen = this;


        gameManager = new GameManager();
        Globals.gameMan = gameManager;
    }

    /**
     * Currently, does nothing
     */
    @Override
    public void show() {
        gameManager.setup();
    }

    /**
     * Renders all the things
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    @Override
    public void render(float delta) {
        gameManager.tick(delta);
    }

    /**
     * Updates the screen's width and height when resized
     *
     * @param width  new screen width
     * @param height new screen height
     */
    @Override
    public void resize(int width, int height) {
        gameManager.resize(width, height);
    }

    /**
     * Currently, does nothing
     */
    @Override
    public void pause() {

    }

    /**
     * Currently, does nothing
     */
    @Override
    public void resume() {

    }

    /**
     * Currently, does nothing
     */
    @Override
    public void hide() {

    }

    /**
     * Discards all assets in memory
     */
    @Override
    public void dispose() {
        gameManager.dispose();
    }
}