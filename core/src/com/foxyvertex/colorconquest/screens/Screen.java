package com.foxyvertex.colorconquest.screens;

import com.foxyvertex.colorconquest.scenes.Scene;

/**
 * Created by aidan on 11/29/2016.
 */

public abstract class Screen implements com.badlogic.gdx.Screen {
    public Scene currentScene;

    public Screen() {

    }

    public void switchScene(Scene newScene) {
        currentScene = newScene;
        currentScene.show();
    }
}
