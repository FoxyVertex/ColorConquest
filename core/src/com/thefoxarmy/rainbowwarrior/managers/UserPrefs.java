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

    // Game progress
    private static String level;

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
     * A getter for the level variable
     * @return the value of the level variable
     */
    public static String getLevel() {
        if (gdxPrefs.contains("level"))
            level = gdxPrefs.getString("level");
        else{
            gdxPrefs.putString("level", FinalGlobals.firstLevel);
            level = FinalGlobals.firstLevel;
        }
        return level;
    }

    /**
     * A setter for the level variable
     * @param level the value to set the level variable to
     */
    public static void setLevel(String level) {
        UserPrefs.level = level;
        gdxPrefs.putString("level", level);
        gdxPrefs.flush();
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