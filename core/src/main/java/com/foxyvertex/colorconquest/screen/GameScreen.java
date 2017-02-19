package com.foxyvertex.colorconquest.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.system.AnimationSystem;
import com.foxyvertex.colorconquest.system.CameraSystem;
import com.foxyvertex.colorconquest.system.PlayerSystem;
import com.foxyvertex.colorconquest.system.SetupCategoryBitsSystem;
import com.foxyvertex.colorconquest.system.WorldPhysicsContactListener;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.kotcrab.vis.runtime.system.physics.Box2dDebugRenderSystem;

/**
 * Created by aidan on 2/19/2017.
 */

public class GameScreen implements Screen {

    VisAssetManager manager;

    String scenePath;
    Scene scene;

    public Array<Drawable> drawables = new Array<>();

    public GameScreen() {
        super();
        Globals.gameScreen = this;
    }

    @Override
    public void show() {
        scenePath = "scene/test.scene";
        manager = new VisAssetManager(Globals.game.batch);
        manager.getLogger().setLevel(Logger.ERROR);
        SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
        parameter.config.addSystem(PlayerSystem.class);
        parameter.config.addSystem(AnimationSystem.class);
        parameter.config.addSystem(CameraSystem.class);
        parameter.config.addSystem(WorldPhysicsContactListener.class);
        parameter.config.addSystem(Box2dDebugRenderSystem.class);
        parameter.config.addSystem(SetupCategoryBitsSystem.class);
        scene = manager.loadSceneNow(scenePath, parameter);
    }

    @Override
    public void render(float delta) {

        scene.render();
        for (Drawable toDraw : drawables) {
            toDraw.draw(Globals.game.batch, 0, 0, 0, 0);
        }
    }

    @Override
    public void resize(int width, int height) {
        scene.resize(width, height);
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
        manager.dispose();
    }

    public void resetLevel() {
        //manager.dispose();
        manager = new VisAssetManager(Globals.game.batch);
        manager.getLogger().setLevel(Logger.ERROR);
        SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
        parameter.config.addSystem(PlayerSystem.class);
        parameter.config.addSystem(AnimationSystem.class);
        parameter.config.addSystem(CameraSystem.class);
        parameter.config.addSystem(WorldPhysicsContactListener.class);
        parameter.config.addSystem(Box2dDebugRenderSystem.class);
        parameter.config.addSystem(SetupCategoryBitsSystem.class);
        scene = manager.loadSceneNow(scenePath, parameter);
    }
}
