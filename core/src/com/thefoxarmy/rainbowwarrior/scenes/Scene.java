package com.thefoxarmy.rainbowwarrior.scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.screens.MenuScreen;

/**
 * Created by aidan on 11/27/2016.
 */

public abstract class Scene implements Disposable {
    public final Stage stage;
    private Viewport viewport;
    private Screen screen;
    private MenuScreen menuScreen;

    public Scene(final Screen screen) {
        this.screen = screen;
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, DynamicGlobals.game.batch);
    }
    public Scene(final MenuScreen screen) {
        this.menuScreen = screen;
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, DynamicGlobals.game.batch);
    }

    public abstract void show();
}
