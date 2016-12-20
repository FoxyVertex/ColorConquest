package com.thefoxarmy.rainbowwarrior;

import com.thefoxarmy.rainbowwarrior.scenes.Scene;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;
import com.thefoxarmy.rainbowwarrior.screens.MenuScreen;
import com.thefoxarmy.rainbowwarrior.screens.Screen;

/**
 * Created by aidan on 11/27/2016.
 */

public class DynamicGlobals {
    public static RainbowWarrior game;
    public static int currentGameSave = 0;

    // Screen Cache:
    public static MenuScreen menuScreen;
    public static GameScreen gameScreen;

    // Scene Cache:
    public static Scene titleScreenScene;
    public static Scene pauseMenuScene;
    public static Scene hudScene;
    public static Scene optionsMenuScreen;
    public static Scene playMenuScene;
    public static Scene gameReadyScene;
}
