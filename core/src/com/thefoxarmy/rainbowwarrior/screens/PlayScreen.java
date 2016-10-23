package com.thefoxarmy.rainbowwarrior.screens;

import com.badlogic.gdx.Screen;
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
import com.thefoxarmy.rainbowwarrior.Globals;
import com.thefoxarmy.rainbowwarrior.RainbowWarrior;
import com.thefoxarmy.rainbowwarrior.sprites.Player;
import com.thefoxarmy.rainbowwarrior.tools.WorldPhysicsCreator;


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

    public PlayScreen(RainbowWarrior game, String path) {

        this.game = game;

        //Camera stuff
        cam = new OrthographicCamera();
        viewport = new StretchViewport(Globals.V_WIDTH / Globals.PPM, Globals.V_HEIGHT / Globals.PPM, cam);

        level = new TmxMapLoader().load(path);
        mapRenderer = new OrthogonalTiledMapRenderer(level, 1 / Globals.PPM);

        cam.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        world = new World(new Vector2(0, -10f), true);
        b2dRenderer = new Box2DDebugRenderer();

        new WorldPhysicsCreator(this);

        player = new Player(this);

    }

    @Override
    public void show() {

    }

    private void tick(float delta) {
        world.step(1 / 60f, 6, 2);
        player.tick(delta);
        cam.position.set(player.body.getPosition().x, player.body.getPosition().y, 0);
        cam.update();
        mapRenderer.setView(cam);
    }

    @Override
    public void render(float delta) {
        tick(delta);
        game.batch.setProjectionMatrix(cam.combined);
        game.batch.begin();
        b2dRenderer.render(world, cam.combined);
        mapRenderer.render();
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        world.dispose();
        b2dRenderer.dispose();

    }

    public World getWorld() {
        return world;
    }
}

