package com.foxyvertex.colorconquest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.foxyvertex.colorconquest.system.AnimationSystem;
import com.foxyvertex.colorconquest.system.CameraSystem;
import com.foxyvertex.colorconquest.system.PlayerSystem;
import com.foxyvertex.colorconquest.system.SetupCategoryBitsSystem;
import com.foxyvertex.colorconquest.tools.ConsoleCommandExecutor;
import com.foxyvertex.colorconquest.system.WorldPhysicsContactListener;
import com.kotcrab.vis.runtime.scene.Scene;
import com.kotcrab.vis.runtime.scene.SceneLoader;
import com.kotcrab.vis.runtime.scene.VisAssetManager;
import com.kotcrab.vis.runtime.system.physics.Box2dDebugRenderSystem;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

public class ColorConquest extends ApplicationAdapter {
	public SpriteBatch batch;
	VisAssetManager manager;

	String scenePath;
	Scene scene;

	public Array<Drawable> drawables = new Array<>();

	public Console console;

	@Override
	public void create () {
		scenePath = "scene/test.scene";
		Globals.game = this;
		console = new GUIConsole(new Skin(Gdx.files.internal("skin/clean-crispy-ui.json")));
		console.setCommandExecutor(new ConsoleCommandExecutor());
		console.setKeyID(Input.Keys.GRAVE);

		batch = new SpriteBatch();

		manager = new VisAssetManager(batch);
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
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		scene.render();
		console.draw();
		for (Drawable toDraw : drawables) {
			toDraw.draw(batch, 0, 0, 0, 0);
		}
	}

	@Override
	public void resize (int width, int height) {
		scene.resize(width, height);
	}

	@Override
	public void dispose () {
		batch.dispose();
		manager.dispose();
		console.dispose();
	}

	public void resetLevel() {
		manager.dispose();
		manager = new VisAssetManager(batch);
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
