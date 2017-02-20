package com.foxyvertex.colorconquest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.foxyvertex.colorconquest.manager.Assets;
import com.foxyvertex.colorconquest.manager.Levels;
import com.foxyvertex.colorconquest.manager.UserPrefs;
import com.foxyvertex.colorconquest.screen.GameScreen;
import com.foxyvertex.colorconquest.screen.SplashScreen;
import com.foxyvertex.colorconquest.screen.TitleMenu;
import com.foxyvertex.colorconquest.tools.ConsoleCommandExecutor;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

public class ColorConquest extends Game {
	public SpriteBatch batch;


	public Console console;

	@Override
	public void create () {
        Globals.systemsToDisableOnPause.add(PhysicsSystem.class);

		Globals.game = this;
        UserPrefs.load();
        Levels.load();
        Assets.load();
		console = new GUIConsole(Assets.guiSkin);
		console.setCommandExecutor(new ConsoleCommandExecutor());
		console.setKeyID(Input.Keys.GRAVE);
		batch = new SpriteBatch();
        if (Finals.SKIP_TO_GAME) {
            new SplashScreen();
            new TitleMenu();
            setScreen(new GameScreen());
        } else {
            new GameScreen();
            new TitleMenu();
            setScreen(new SplashScreen());
        }
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
		console.draw();
	}

	@Override
	public void resize (int width, int height) {

	}

	@Override
	public void dispose () {
		batch.dispose();

		console.dispose();
	}


}
