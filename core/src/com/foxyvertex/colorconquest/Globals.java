package com.foxyvertex.colorconquest;

import com.foxyvertex.colorconquest.game.GameManager;
import com.foxyvertex.colorconquest.scenes.Hud;
import com.foxyvertex.colorconquest.scenes.Scene;
import com.foxyvertex.colorconquest.screens.GameScreen;
import com.foxyvertex.colorconquest.screens.MenuScreen;
import com.foxyvertex.colorconquest.screens.SplashScreen;
import com.foxyvertex.colorconquest.screens.TutorialScreen;

/**
 * Created by aidan on 11/27/2016.
 */

public class Globals {
    public static boolean isMobileApp = false;

    public static ColorConquest game;
    public static int currentGameSave = 45;

    // Screen Cache:
    public static MenuScreen menuScreen;
    public static GameScreen gameScreen;
    public static SplashScreen splashScreen;
    public static TutorialScreen tutorialScreen;

    // Scene Cache:
    public static Scene titleScreenScene;
    public static Scene pauseMenuScene;
    public static Hud   hudScene;
    public static Scene optionsMenuScreen;
    public static Scene levelSelectScene;
    public static Scene playMenuScene;
    public static Scene gameReadyScene;

    public static GameManager gameMan;
    public static Scene         LoadLevelScreen;
}
