package com.foxyvertex.colorconquest.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.foxyvertex.colorconquest.entities.Player;
import com.foxyvertex.colorconquest.managers.Levels;
import com.foxyvertex.colorconquest.managers.UserPrefs;
import com.foxyvertex.colorconquest.tools.PlayerInputAdapter;
import com.foxyvertex.colorconquest.tools.WorldPhysicsContactListener;
import com.foxyvertex.colorconquest.tools.WorldPhysicsCreator;

/**
 * Created by aidan on 1/23/17.
 */

public class GameManager {

    public static com.foxyvertex.colorconquest.game.GameState gameState;

    public TiledMap tiledMap;
    public Levels.Level currentLevel;
    public Player player;
    public float timeSinceStartLevel = 0;
    //Camera stuff
    public OrthographicCamera cam;
    public Viewport viewport;
    public TiledMapRenderer mapRenderer;
    public World world;
    public Box2DDebugRenderer b2dRenderer;

    Ready ready;
    Running running;
    Paused paused;
    LevelEnd levelEnd;
    GameEnd gameEnd;

    public GameManager() {
        currentLevel = Levels.levels.get(UserPrefs.getLevel(Globals.currentGameSave));

        //Camera stuff
        cam = new OrthographicCamera();
        viewport = new StretchViewport(Finals.V_WIDTH / Finals.PPM, Finals.V_HEIGHT / Finals.PPM, cam);


    }

    public void setup() {
        ready = new Ready();
        running = new Running();
        paused = new Paused();
        levelEnd = new LevelEnd();
        gameEnd = new GameEnd();

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

        switchState(GameState.READY);
    }

    public void tick(float delta) {
        gameState.update(delta);

        gameState.render();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        Globals.hudScene.resize(width, height);
        Globals.pauseMenuScene.resize(width, height);
    }

    public void dispose() {
        world.dispose();
        b2dRenderer.dispose();
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
            Globals.gameScreen.show();
        } else {
            currentLevel = nextLevel;
            UserPrefs.setLevel(Globals.currentGameSave, nextLevel.index);
            Globals.gameScreen.show();
        }

    }

    public void switchState(GameState newState) {
        if (gameState != null) gameState.stop();
        switch (newState) {
            case READY:
                gameState = ready;
                break;
            case RUNNING:
                gameState = running;
                break;
            case PAUSED:
                gameState = paused;
                break;
            case LEVEL_END:
                gameState = levelEnd;
                break;
            case OVER:
                gameState = gameEnd;
                break;
        }
        gameState.start();
    }

    public enum GameState {
        READY,
        RUNNING,
        PAUSED,
        LEVEL_END,
        OVER
    }

}
