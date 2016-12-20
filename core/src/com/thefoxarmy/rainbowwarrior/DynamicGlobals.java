package com.thefoxarmy.rainbowwarrior;

import com.thefoxarmy.rainbowwarrior.scenes.Scene;
import com.thefoxarmy.rainbowwarrior.screens.Screen;

/**
 * Created by aidan on 11/27/2016.
 */

public class DynamicGlobals {
    public static RainbowWarrior game;
    public static int currentGameSave = 19;

    // Screen Cache:
    public static Screen menuScreen;
    public static Screen gameScreen;

    // Scene Cache:
    public static Scene titleScreenScene;
    public static Scene pauseMenuScene;
    public static Scene hudScene;
    public static Scene optionsMenuScreen;
    public static Scene playMenuScene;
}
