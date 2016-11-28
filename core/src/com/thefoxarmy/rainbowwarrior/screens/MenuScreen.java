package com.thefoxarmy.rainbowwarrior.screens;

import com.badlogic.gdx.Screen;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.scenes.Scene;
import com.thefoxarmy.rainbowwarrior.scenes.TitleScreen;

/**
 * Handles the Main menu for the game, as well as saving and loading player data.
 */
public class MenuScreen implements Screen {
    Scene currentScene;

    /**
     * Sets up the title screen
     */
    public MenuScreen() {
        currentScene = new TitleScreen(this);
    }


    /*
     * TODO: Finish Implementing scene switching for a screen
     * Maybe have a custom Screen class that extends libGDX's screen?
     */
//    public void switchScene(Scene newScene) {
//        currentScene.dispose();
//        currentScene = newScene;
//    }

    @Override
    public void show() {
        currentScene = new TitleScreen(this);
    }

    @Override
    public void render(float delta) {
        DynamicGlobals.game.batch.setProjectionMatrix(currentScene.stage.getCamera().combined);
        currentScene.stage.act();
        currentScene.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        currentScene.stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        currentScene.dispose();
    }

    @Override
    public void dispose() {
        currentScene.dispose();
    }
}
