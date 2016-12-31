package com.foxyvertex.colorconquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.entities.Block;
import com.foxyvertex.colorconquest.managers.Levels;
import com.foxyvertex.colorconquest.managers.UserPrefs;
import com.foxyvertex.colorconquest.scenes.GameReadyScreen;
import com.foxyvertex.colorconquest.scenes.Hud;
import com.foxyvertex.colorconquest.scenes.PauseMenu;
import com.foxyvertex.colorconquest.entities.Player;
import com.foxyvertex.colorconquest.tools.PlayerInputAdapter;
import com.foxyvertex.colorconquest.tools.Utilities;
import com.foxyvertex.colorconquest.tools.WorldPhysicsContactListener;
import com.foxyvertex.colorconquest.tools.WorldPhysicsCreator;

import static com.badlogic.gdx.Gdx.input;

/**
 * Handles tiledMap loading, all of the objects in the world, and anything outside of a menu
 */
public class GameScreen extends Screen {

    public static GameState gameState;

    public TiledMap tiledMap;
    public Levels.Level currentLevel;
    public Player player;
    public float timeSinceStartLevel = 0;
    //Camera stuff
    private OrthographicCamera cam;
    private Viewport viewport;
    private TiledMapRenderer mapRenderer;
    private World world;
    private Box2DDebugRenderer b2dRenderer;

    /**
     * Sets up the GameScreen for being cached
     */
    public GameScreen() {
        this.currentLevel = Levels.levels.get(UserPrefs.getLevel(Globals.currentGameSave));

        //Camera stuff
        cam = new OrthographicCamera();
        viewport = new StretchViewport(Finals.V_WIDTH / Finals.PPM, Finals.V_HEIGHT / Finals.PPM, cam);

        Globals.hudScene = new Hud(this);
        Globals.pauseMenuScene = new PauseMenu(this);
        Globals.gameReadyScene = new GameReadyScreen(this);
        Globals.gameScreen = this;
    }

    /**
     * Currently, does nothing
     */
    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load(currentLevel.path);
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / Finals.PPM);

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -15), true);
        world.setContactListener(new WorldPhysicsContactListener(this));
        b2dRenderer = new Box2DDebugRenderer();
        new WorldPhysicsCreator(world, tiledMap);
        //Spawns the player at a location designated on the map
        player = new Player(new PlayerInputAdapter(),
                new Vector2(
                        tiledMap.getLayers().get("triggerPoints").getObjects().get("p1SpawnPoint").getProperties().get("x", Float.class),
                        tiledMap.getLayers().get("triggerPoints").getObjects().get("p1SpawnPoint").getProperties().get("y", Float.class)
                )
        );
        cam.position.y = player.body.getPosition().y;
        gameState = GameState.READY;
        Globals.gameReadyScene.show();
    }

    /**
     * Calculates all physics on behalf of the world
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    private void tick(float delta) {
        switch (gameState) {
            case READY:
                updateReady(delta);
                break;
            case RUNNING:
                updateRunning(delta);
                break;
            case PAUSED:
                updatePaused();
                break;
            case LEVEL_END:
                updateLevelEnd();
                break;
            case OVER:
                updateGameOver();
                break;
        }
    }

    /**
     * This is called to update physics for the READY state
     */
    private void updateReady(float delta) {
        Globals.gameReadyScene.tick(delta);
    }

    /**
     * This is called to show stuff for the READY state
     */
    private void presentReady() {
        Globals.game.batch.setProjectionMatrix(Globals.gameReadyScene.stage.getCamera().combined);
        Globals.gameReadyScene.stage.draw();
    }

    /**
     * This is called to update physics for the RUNNING state
     */
    private void updateRunning(float delta) {
        if (input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameState = GameState.PAUSED;
            Globals.pauseMenuScene.show();
            return;
        }

        timeSinceStartLevel += delta;
        world.step(1 / 60f, 6, 2);
        cam.position.x = player.body.getPosition().x;
        cam.position.y = player.body.getPosition().y;
        MapProperties levelProps = tiledMap.getProperties();
        int mapPixelWidth = levelProps.get("width", Integer.class) * levelProps.get("tilewidth", Integer.class);
        int mapPixelHeight = levelProps.get("height", Integer.class) * levelProps.get("tileheight", Integer.class);
        cam.position.x = Utilities.clamp(player.body.getPosition().x, cam.viewportWidth / 2, (mapPixelWidth / Finals.PPM) - (cam.viewportWidth / 2));
        cam.position.y = Utilities.clamp(player.body.getPosition().y, cam.viewportHeight / 2, (mapPixelHeight / Finals.PPM) - (cam.viewportHeight / 2));

        player.tick(delta);
        cam.update();
        mapRenderer.setView(cam);
        Globals.hudScene.stage.act();
        for (Block block : Block.blocks) block.tick(delta);
    }

    /**
     * This is called to show stuff for the READY state
     */
    private void presentRunning() {
        mapRenderer.render();
        b2dRenderer.render(world, cam.combined);
        Globals.game.batch.setProjectionMatrix(cam.combined);
        Globals.game.batch.begin();
        for (Block block : Block.blocks) block.draw(Globals.game.batch);
        player.draw(Globals.game.batch);
        Globals.game.batch.end();
        Globals.game.batch.setProjectionMatrix(Globals.hudScene.stage.getCamera().combined);
        Globals.hudScene.stage.draw();

    }

    /**
     * This is called to update physics for the PAUSED state
     */
    private void updatePaused() {

        if (input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameState = GameState.RUNNING;
            Gdx.input.setInputProcessor(player.input);
        }

    }

    /**
     * This is called to show stuff for the PAUSED state
     */
    private void presentPaused() {
        mapRenderer.render();
        Globals.game.batch.setProjectionMatrix(cam.combined);
        Globals.game.batch.begin();
        player.draw(Globals.game.batch);
        Globals.game.batch.end();
        Globals.game.batch.setProjectionMatrix(Globals.pauseMenuScene.stage.getCamera().combined);
        Globals.pauseMenuScene.stage.act();
        Globals.pauseMenuScene.stage.draw();
    }

    /**
     * This is called to update physics for the LEVEL_END state
     */
    private void updateLevelEnd() {

    }

    /**
     * This is called to show stuff for the LEVEL_END state
     */
    private void presentLevelEnd() {

    }

    /**
     * This is called to update physics for the SHOW state
     */
    private void updateGameOver() {

    }

    /**
     * This is called to show stuff for the SHOW state
     */
    private void presentGameOver() {

    }


    /**
     * Renders all the things
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    @Override
    public void render(float delta) {
        tick(delta);
        switch (gameState) {
            case READY:
                presentReady();
                break;
            case RUNNING:
                presentRunning();
                break;
            case PAUSED:
                presentPaused();
                break;
            case LEVEL_END:
                presentLevelEnd();
                break;
            case OVER:
                presentGameOver();
                break;
        }

    }

    /**
     * Updates the screen's width and height when resized
     *
     * @param width  new screen width
     * @param height new screen height
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        Globals.hudScene.resize(width, height);
        Globals.pauseMenuScene.resize(width, height);
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
        world.dispose();
        b2dRenderer.dispose();
        player.dispose();
    }

    /**
     * Gets the world for anyone who needs it
     *
     * @return the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Update the user's preferences file and create a new playScreen based on the nextLevel property of the current TiledMap.
     */
    public void switchLevel(Levels.Level nextLevel) {
        if (currentLevel.hasCutscene) {
            // TODO: Implement CutScenes
            // For now, this will just load the next level
            currentLevel = nextLevel;
            UserPrefs.setLevel(Globals.currentGameSave, nextLevel.index);
            this.show();
        } else {
            currentLevel = nextLevel;
            UserPrefs.setLevel(Globals.currentGameSave, nextLevel.index);
            this.show();
        }

    }

    /**
     * This houses the different game states.
     */
    public enum GameState {
        READY,
        RUNNING,
        PAUSED,
        LEVEL_END,
        OVER
    }
}

