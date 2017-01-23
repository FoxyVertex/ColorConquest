package com.foxyvertex.colorconquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.entities.Block;
import com.foxyvertex.colorconquest.entities.Player;
import com.foxyvertex.colorconquest.game.GameManager;
import com.foxyvertex.colorconquest.game.GameEnd;
import com.foxyvertex.colorconquest.game.LevelEnd;
import com.foxyvertex.colorconquest.game.Paused;
import com.foxyvertex.colorconquest.game.Ready;
import com.foxyvertex.colorconquest.game.Running;
import com.foxyvertex.colorconquest.managers.Levels;
import com.foxyvertex.colorconquest.managers.UserPrefs;
import com.foxyvertex.colorconquest.scenes.GameReadyScreen;
import com.foxyvertex.colorconquest.scenes.Hud;
import com.foxyvertex.colorconquest.scenes.PauseMenu;
import com.foxyvertex.colorconquest.tools.PlayerInputAdapter;
import com.foxyvertex.colorconquest.tools.Utilities;
import com.foxyvertex.colorconquest.tools.WorldPhysicsContactListener;
import com.foxyvertex.colorconquest.tools.WorldPhysicsCreator;

import static com.badlogic.gdx.Gdx.input;

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