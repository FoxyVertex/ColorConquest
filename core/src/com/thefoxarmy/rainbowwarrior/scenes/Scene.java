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
 *
 * Parent class for all of my scenes
 */
public abstract class Scene implements Disposable {
    public final Stage stage;
    private Viewport viewport;
    private Screen screen;
    private MenuScreen menuScreen;

    /**
     * Creates the main variables for a scene
     * @param screen used to switch scenes on command
     */
    public Scene(final Screen screen) {
        this.screen = screen;
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, DynamicGlobals.game.batch);
    }

    /**
     * Creates the main variables for a scene
     * @param screen used to switch scenes on command
     */
    public Scene(final MenuScreen screen) {
        this.menuScreen = screen;
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, DynamicGlobals.game.batch);
    }

    /**
     * Called when the scene gets show if it has been reused in memory
     */
    public abstract void show();

    /**
     * Called every frame
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public abstract void tick(float delta);

    public abstract void resize(int width, int height);
}
