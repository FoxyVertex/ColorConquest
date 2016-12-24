package com.thefoxarmy.rainbowwarrior;

import com.thefoxarmy.rainbowwarrior.scenes.Scene;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;
import com.thefoxarmy.rainbowwarrior.screens.MenuScreen;

/**
 * Created by aidan on 11/27/2016.
 */

public class Globals {
    public static RainbowWarrior game;
    public static int currentGameSave = 45;

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
