package com.foxyvertex.colorconquest.screens;

import com.badlogic.gdx.Gdx;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.scenes.TitleScreen;

/**
 * Handles the Main menu for the game, as well as saving and loading player data.
 */
public class MenuScreen extends Screen {

    /**
     * Sets up the title screen
     */
    public MenuScreen() {
        currentScene = new TitleScreen(this);
        Globals.titleScreenScene = currentScene;
        Gdx.input.setInputProcessor(currentScene.stage);
        currentScene.show();
        Globals.menuScreen = this;
    }

    @Override
    public void show() {
        currentScene = Globals.titleScreenScene;
        if (!Assets.menuMusic.isPlaying()) {
            Assets.playMusic(Assets.menuMusic);
            Assets.menuMusic.setLooping(true);
        }
    }

    @Override
    public void render(float delta) {
        currentScene.tick(delta);
        Globals.game.batch.setProjectionMatrix(currentScene.stage.getCamera().combined);
        currentScene.stage.act();
        currentScene.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        currentScene.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        currentScene.dispose();
    }
}