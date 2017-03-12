package com.foxyvertex.colorconquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.manager.Levels;
import com.foxyvertex.colorconquest.manager.UserPrefs;
import com.foxyvertex.colorconquest.stages.PauseMenu;
import com.foxyvertex.colorconquest.system.AnimationSystem;
import com.foxyvertex.colorconquest.system.render.B2DLightsRenderSystem;
import com.foxyvertex.colorconquest.system.B2DLightsSetupSystem;
import com.foxyvertex.colorconquest.system.CameraSystem;
import com.foxyvertex.colorconquest.system.ColorTintSystem;
import com.foxyvertex.colorconquest.system.HealthSystem;
import com.foxyvertex.colorconquest.system.HudSystem;
import com.foxyvertex.colorconquest.system.PlayerSystem;
import com.foxyvertex.colorconquest.system.SetBox2DUserDataSystem;
import com.foxyvertex.colorconquest.system.SetColorComponentSystem;
import com.foxyvertex.colorconquest.system.SetupEntityComponentsSystem;
import com.foxyvertex.colorconquest.system.ToDestroySystem;
import com.foxyvertex.colorconquest.system.CollisionSystem;
import com.foxyvertex.colorconquest.system.ZombieSystem;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneConfig;
import com.kotcrab.vis.runtime.scene.SceneFeature;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.VisAssetManager;

import box2dLight.RayHandler;

/**
 * Created by aidan on 2/19/2017.
 */

public class GameScreen implements Screen {

    VisAssetManager manager;

    Levels.Level currentLevel;
    public Scene scene;

    public Array<Drawable> drawables = new Array<>();

    boolean isStopped = false;

    public RayHandler b2dlHandler;

    public GameScreen() {
        super();
        manager = new VisAssetManager(Globals.game.batch);
        manager.getLogger().setLevel(Logger.ERROR);
        Globals.gameScreen = this;
        Globals.pauseMenuStage = new PauseMenu();
    }

    @Override
    public void show() {
        if (!isStopped) {
            currentLevel = Levels.levels.get(UserPrefs.getLevel(Globals.currentGameSave));
            initLevel();
        }
    }

    @Override
    public void render(float delta) {
        if (!isStopped) {
            scene.render();
            if (scene.getEntityEngine().getSystem(PlayerSystem.class).isGamePaused)
                Globals.pauseMenuStage.stage.draw();
            for (Drawable toDraw : drawables) {
                toDraw.draw(Globals.game.batch, 0, 0, 0, 0);
            }
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

    public void initLevel() {
        SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
        parameter.config.addSystem(PlayerSystem.class);
        parameter.config.addSystem(CameraSystem.class);
        parameter.config.addSystem(CollisionSystem.class);
        if (Finals.ENABLE_BOX2D_DEBUG_RENDERER) parameter.config.enable(SceneFeature.BOX2D_DEBUG_RENDER_SYSTEM);
        parameter.config.addSystem(SetupEntityComponentsSystem.class);
        parameter.config.addSystem(SetBox2DUserDataSystem.class);
        parameter.config.addSystem(SetColorComponentSystem.class);
        parameter.config.addSystem(ColorTintSystem.class);
        parameter.config.addSystem(HudSystem.class, SceneConfig.Priority.VIS_RENDERER.toIntValue()-2);
        parameter.config.addSystem(ToDestroySystem.class);
        parameter.config.addSystem(AnimationSystem.class);
        parameter.config.addSystem(ZombieSystem.class);
        parameter.config.addSystem(B2DLightsSetupSystem.class);
        parameter.config.addSystem(HealthSystem.class);
        parameter.config.addSystem(B2DLightsRenderSystem.class, SceneConfig.Priority.VIS_RENDERER.toIntValue()-1);
        scene = manager.loadSceneNow(currentLevel.path, parameter);
    }

    public void resetLevel() {
        Gdx.app.log(Finals.ANSI_PURPLE + "Game" + Finals.ANSI_RESET, "Resetting Level....");
        isStopped = true;
        new GameScreen();
        Globals.game.setScreen(Globals.gameScreen);
    }

    public void nextLevel() {
        Gdx.app.log(Finals.ANSI_PURPLE + "Game" + Finals.ANSI_RESET, "Player achieved next level. Switching to it...");
        UserPrefs.setLevel(Globals.currentGameSave, 0);
        currentLevel = Levels.levels.get(UserPrefs.getLevel(Globals.currentGameSave));
        new GameScreen();
        isStopped = true;
        Globals.game.setScreen(Globals.gameScreen);
    }
}
