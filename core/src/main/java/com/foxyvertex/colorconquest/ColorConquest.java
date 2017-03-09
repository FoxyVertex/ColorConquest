package com.foxyvertex.colorconquest;

import com.badlogic.gdx.Application;
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
import com.foxyvertex.colorconquest.tools.Utilities;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

/**
 * ColorConquest is a game all about color
 */
public class ColorConquest extends Game {
	public SpriteBatch batch;

	public Console console;

    /** Called when the game is first created. */
	@Override
	public void create () {
        System.out.println(Finals.ANSI_RED   + "########################################" + Finals.ANSI_RESET);
        System.out.println(Finals.ANSI_GREEN + "#                                      #" + Finals.ANSI_RESET);
        System.out.println(Finals.ANSI_BLUE  + "#            Color Conquest            #" + Finals.ANSI_RESET);
        System.out.println(Finals.ANSI_GREEN + "#                                      #" + Finals.ANSI_RESET);
        System.out.println(Finals.ANSI_RED   + "########################################" + Finals.ANSI_RESET);

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

        Globals.systemsToDisableOnPause.add(PhysicsSystem.class);
		Globals.game = this;
        UserPrefs.load();
        Levels.load();
        Assets.load();
        ColorEffects.load();
		console = new GUIConsole(Assets.guiSkin);
		console.setCommandExecutor(new ConsoleCommandExecutor());
		console.setKeyID(Input.Keys.GRAVE);

		batch = new SpriteBatch();
        if (Finals.SKIP_TO_GAME) {
            new SplashScreen();
            new TitleMenu();
			Gdx.app.log(Finals.ANSI_CYAN + "Game" + Finals.ANSI_RESET, "Skipping the splash and menu.");
            setScreen(new GameScreen());
        } else {
            new GameScreen();
            new TitleMenu();
            setScreen(new SplashScreen());
        }
	}

    /**
     * The render method is called every frame and delegates the task of rendering to Vis
     */
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
		console.draw();
	}

    /**
     * called when the window gets resized
     * @param width new width
     * @param height new height
     */
	@Override
	public void resize (int width, int height) {
        super.resize(width, height);
	}

    /**
     * Called to clean up the game
     */
	@Override
	public void dispose () {
		batch.dispose();
        super.dispose();
		console.dispose();
	}

    /**
     * A simple debug log method meant for more simplistic debuging
     * @param message
     */
	public static void debugLog(String message) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        Gdx.app.log(stackTraceElements[1].getClassName(), message);
        Globals.game.console.log(stackTraceElements[1].getClassName() + ": " + message);
	}
}
