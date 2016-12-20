package com.thefoxarmy.rainbowwarrior.screens;

import com.badlogic.gdx.Gdx;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.scenes.Scene;
import com.thefoxarmy.rainbowwarrior.scenes.TitleScreen;

/**
 * Handles the Main menu for the game, as well as saving and loading player data.
 */
public class MenuScreen extends Screen {

    /**
     * Sets up the title screen
     */
    public MenuScreen() {
        currentScene = new TitleScreen(this);
        DynamicGlobals.titleScreenScene = currentScene;
        Gdx.input.setInputProcessor(currentScene.stage);
        currentScene.show();
        DynamicGlobals.menuScreen = this;
    }

    @Override
    public void show() {
        currentScene = DynamicGlobals.titleScreenScene;
    }

    @Override
    public void render(float delta) {
        currentScene.tick(delta);
        DynamicGlobals.game.batch.setProjectionMatrix(currentScene.stage.getCamera().combined);
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
