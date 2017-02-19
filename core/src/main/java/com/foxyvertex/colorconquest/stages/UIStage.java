package com.foxyvertex.colorconquest.stages;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxyvertex.colorconquest.Globals;

/**
 * Created by aidan on 11/27/2016.
 * <p>
 * Parent class for all of my scenes
 */
public abstract class UIStage implements Disposable {
    public final Stage stage;
    private Viewport viewport;
    private Screen screen;

    /**
     * Creates the main variables for a scene
     *
     * @param screen used to switch scenes on command
     */
    public UIStage(final Screen screen) {
        this.screen = screen;
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, Globals.game.batch);
    }

    /**
     * Creates the main variables for a scene
     */
    public UIStage() {
        viewport = new ScreenViewport(new OrthographicCamera());
        stage = new Stage(viewport, Globals.game.batch);
    }

    /**
     * Called when the scene gets show if it has been reused in memory
     */
    public abstract void show();

    /**
     * Called every frame
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public abstract void tick(float delta);

    public abstract void resize(int width, int height);
}