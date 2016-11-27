package com.thefoxarmy.rainbowwarrior.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thefoxarmy.rainbowwarrior.Assets;
import com.thefoxarmy.rainbowwarrior.Globals;
import com.thefoxarmy.rainbowwarrior.RainbowWarrior;
import com.thefoxarmy.rainbowwarrior.sprites.Player;
import com.thefoxarmy.rainbowwarrior.tools.PlayerInputAdapter;
import com.thefoxarmy.rainbowwarrior.tools.WorldPhysicsContactListener;
import com.thefoxarmy.rainbowwarrior.tools.WorldPhysicsCreator;

/**
 * Handles level loading, all of the objects in the world, and anything outside of a menu
 */
public class PlayScreen implements Screen {

    public TiledMap level;
    private Player player;
    private RainbowWarrior game;
    //Camera stuff
    private OrthographicCamera cam;
    private Viewport viewport;
    private TiledMapRenderer mapRenderer;
    private World world;
    private Box2DDebugRenderer b2dRenderer;
    private Preferences prefs;

    /**
     * Initializes the current level and sets up the playing screen
     *
     * @param game the main game class
     * @param path path to the `tmx` level
     */
    PlayScreen(RainbowWarrior game, String path) {

        this.game = game;
        this.prefs = Gdx.app.getPreferences("User Data");
        //Camera stuff
        cam = new OrthographicCamera();
        viewport = new StretchViewport(Globals.V_WIDTH / Globals.PPM, Globals.V_HEIGHT / Globals.PPM, cam);

        level = new TmxMapLoader().load(path);
        mapRenderer = new OrthogonalTiledMapRenderer(level, 1 / Globals.PPM);

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -9.89f), true);
        world.setContactListener(new WorldPhysicsContactListener(this));
        b2dRenderer = new Box2DDebugRenderer();

        new WorldPhysicsCreator(this);

        //Spawns the player at a location designated on the map
        player = new Player(this,
                new PlayerInputAdapter(player),
                new Vector2(level.getLayers().get("triggerPoints").getObjects().get("p1SpawnPoint").getProperties().get("x", Float.class),
                        level.getLayers().get("triggerPoints").getObjects().get("p1SpawnPoint").getProperties().get("y", Float.class)
                )
        );
        cam.position.y = player.body.getPosition().y;
    }

    /**
     * Currently, does nothing
     */
    @Override
    public void show() {

    }

    /**
     * Calculates all physics on behalf of the world
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    private void tick(float delta) {

        world.step(1 / 60f, 6, 2);
        cam.position.x = player.body.getPosition().x;
        if (player.body.getPosition().y >= (cam.viewportHeight / 4) * 3)
            cam.position.y = player.body.getPosition().y;

        if (player.body.getPosition().y <= (cam.viewportHeight) / 3)
            cam.position.y = player.body.getPosition().y;


        player.tick(delta);
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            pause();
        cam.update();
        mapRenderer.setView(cam);
    }

    /**
     * Renders all the things
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    @Override
    public void render(float delta) {
        tick(delta);


        //Drawing Area
        mapRenderer.render();


        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
        b2dRenderer.render(world, cam.combined);

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
     * Discards all textures in memory
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
    public void switchLevel() {
        if (!level.getProperties().get("hasCutscene", Boolean.class)) {
            prefs.putString("Level", level.getProperties().get("nextLevel", String.class));
            prefs.flush();
            game.setScreen(new PlayScreen(game, prefs.getString("Level")));
        } else {
            //Play Cutscene or whatever...
        }

    }
}

