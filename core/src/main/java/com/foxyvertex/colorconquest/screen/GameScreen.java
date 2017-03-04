package com.foxyvertex.colorconquest.screen;

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
import com.foxyvertex.colorconquest.system.CameraSystem;
import com.foxyvertex.colorconquest.system.ColorTintSystem;
import com.foxyvertex.colorconquest.system.HudSystem;
import com.foxyvertex.colorconquest.system.PlayerSystem;
import com.foxyvertex.colorconquest.system.SetBox2DUserDataSystem;
import com.foxyvertex.colorconquest.system.SetColorComponentSystem;
import com.foxyvertex.colorconquest.system.SetupCategoryBitsSystem;
import com.foxyvertex.colorconquest.system.ToDestroySystem;
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

    Levels.Level currentLevel;
    public Scene scene;

    public Array<Drawable> drawables = new Array<>();

    public GameScreen() {
        super();
        manager = new VisAssetManager(Globals.game.batch);
        manager.getLogger().setLevel(Logger.ERROR);
        Globals.gameScreen = this;
        Globals.pauseMenuStage = new PauseMenu();
    }

    @Override
    public void show() {
        currentLevel = Levels.levels.get(UserPrefs.getLevel(Globals.currentGameSave));
        initLevel();
    }

    @Override
    public void render(float delta) {
        scene.render();
        if (scene.getEntityEngine().getSystem(PlayerSystem.class).isGamePaused) Globals.pauseMenuStage.stage.draw();
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

    public void initLevel() {
        SceneLoader.SceneParameter parameter = new SceneLoader.SceneParameter();
        parameter.config.addSystem(AnimationSystem.class);
        parameter.config.addSystem(PlayerSystem.class);
        parameter.config.addSystem(CameraSystem.class);
        parameter.config.addSystem(WorldPhysicsContactListener.class);
        if (Finals.ENABLE_BOX2D_DEBUG_RENDERER) parameter.config.addSystem(Box2dDebugRenderSystem.class);
        parameter.config.addSystem(SetupCategoryBitsSystem.class);
        parameter.config.addSystem(SetBox2DUserDataSystem.class);
        parameter.config.addSystem(SetColorComponentSystem.class);
        parameter.config.addSystem(ColorTintSystem.class);
        parameter.config.addSystem(HudSystem.class);
        parameter.config.addSystem(ToDestroySystem.class);
        scene = manager.loadSceneNow(currentLevel.path, parameter);
    }

    public void resetLevel() {
        scene.dispose();
        initLevel();
    }

    public void nextLevel() {
        UserPrefs.setLevel(Globals.currentGameSave, currentLevel.nextLevel.index);
        currentLevel = Levels.levels.get(UserPrefs.getLevel(Globals.currentGameSave));
        initLevel();
    }
}
