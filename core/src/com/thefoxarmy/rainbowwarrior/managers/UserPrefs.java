package com.thefoxarmy.rainbowwarrior.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.thefoxarmy.rainbowwarrior.FinalGlobals;

/**
 * Created by aidan on 11/28/2016.
 *
 * Houses user preferences and manages them.
 */

public class UserPrefs {
    public static Preferences gdxPrefs;
    // Prefs
    private static boolean soundEnabled;

    /**
     * A getter for the soundEnabled variable
     * @return the value of the soundEnabled variable
     */
    public static boolean isSoundEnabled() {
        if (gdxPrefs.contains("soundEnabled")) {
            soundEnabled = gdxPrefs.getBoolean("soundEnabled");
        }
        else{
            gdxPrefs.putBoolean("soundEnabled", true);
            soundEnabled = true;
        }
        return soundEnabled;
    }

    /**
     * A setter for the soundEnabled variable
     * @param soundEnabled value to set the soundEnabled boolean to
     */
    public static void setSoundEnabled(boolean soundEnabled) {
        UserPrefs.soundEnabled = soundEnabled;
        gdxPrefs.putBoolean("soundEnabled", soundEnabled);
        gdxPrefs.flush();
    }

    /**
     * A getter for the tiledMap variable
     * @return the value of the tiledMap variable
     */
    public static int getLevel(int save) {
        int level;
        if (gdxPrefs.contains("tiledMap"))
            level = gdxPrefs.getInteger("tiledMap" + save);
        else{
            gdxPrefs.putInteger("tiledMap" + save, FinalGlobals.firstLevel);
            level = FinalGlobals.firstLevel;
        }
        return level;
    }

    /**
     * A setter for the tiledMap variable
     * @param level the value to set the tiledMap variable to
     */
    public static void setLevel(int save, int level) {
        gdxPrefs.putInteger("tiledMap" + save, level);
        gdxPrefs.flush();
        Levels.currentLevel = level;
    }

    /**
     * Loads preferences into memory
     */
    public static void load() {
        gdxPrefs = Gdx.app.getPreferences("User Data");

        if (gdxPrefs.contains("soundEnabled"))
            soundEnabled = gdxPrefs.getBoolean("soundEnabled");
        else{
            gdxPrefs.putBoolean("soundEnabled", true);
            soundEnabled = true;
        }
    }
}