package com.foxyvertex.colorconquest.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.foxyvertex.colorconquest.Finals;

/**
 * Created by aidan on 11/28/2016.
 * Houses user preferences and manages them.
 */

public class UserPrefs {
    public static  Preferences gdxPrefs;
    // Prefs
    private static boolean soundEnabled;
    private static boolean musicEnabled;
    private static float masterVolume;
    private static float soundVolume;
    private static float musicVolume;

    /**
     * A getter for the soundEnabled variable
     *
     * @return the value of the soundEnabled variable
     */
    public static boolean isSoundEnabled() {
        if (gdxPrefs.contains("soundEnabled")) {
            soundEnabled = gdxPrefs.getBoolean("soundEnabled");
        } else {
            gdxPrefs.putBoolean("soundEnabled", true);
            soundEnabled = true;
        }
        return soundEnabled;
    }

    /**
     * A getter for the musicEnabled variable
     *
     * @return the value of the soundEnabled variable
     */
    public static boolean isMusicEnabled() {
        if (gdxPrefs.contains("musicEnabled")) {
            musicEnabled = gdxPrefs.getBoolean("musicEnabled");
        } else {
            gdxPrefs.putBoolean("musicEnabled", true);
            musicEnabled = true;
        }
        return musicEnabled;
    }

    /**
     * A setter for the musicEnabled variable
     *
     * @param musicEnabled value to set the soundEnabled boolean to
     */
    public static void setMusicEnabled(boolean musicEnabled) {
        UserPrefs.musicEnabled = musicEnabled;
        gdxPrefs.putBoolean("musicEnabled", musicEnabled);
        gdxPrefs.flush();
        if (!musicEnabled) {
            Assets.stopPlayingMusic();
        }
    }

    /**
     * A setter for the soundEnabled variable
     *
     * @param soundEnabled value to set the soundEnabled boolean to
     */
    public static void setSoundEnabled(boolean soundEnabled) {
        UserPrefs.soundEnabled = soundEnabled;
        gdxPrefs.putBoolean("soundEnabled", soundEnabled);
        gdxPrefs.flush();
    }

    public static void setSoundVolume(float percent) {
        UserPrefs.soundVolume = percent;
        gdxPrefs.putFloat("soundVolume", percent);
        gdxPrefs.flush();
        Assets.volumeChanged();
    }

    public static float getSoundVolume() {
        if (gdxPrefs.contains("soundVolume")) {
            soundVolume = gdxPrefs.getFloat("soundVolume");
        } else {
            gdxPrefs.putFloat("soundVolume", 100);
            soundVolume = 100;
        }
        return soundVolume;
    }

    public static void setMusicVolume(float percent) {
        UserPrefs.musicVolume = percent;
        gdxPrefs.putFloat("musicVolume", percent);
        gdxPrefs.flush();
        Assets.volumeChanged();
    }

    public static float getMusicVolume() {
        if (gdxPrefs.contains("musicVolume")) {
            musicVolume = gdxPrefs.getFloat("musicVolume");
        } else {
            gdxPrefs.putFloat("musicVolume", 100);
            musicVolume = 100;
        }
        return musicVolume;
    }

    public static void setMasterVolume(float percent) {
        UserPrefs.masterVolume = percent;
        gdxPrefs.putFloat("masterVolume", percent);
        gdxPrefs.flush();
        Assets.volumeChanged();
    }

    public static float getMasterVolume() {
        if (gdxPrefs.contains("masterVolume")) {
            masterVolume = gdxPrefs.getFloat("masterVolume");
        } else {
            gdxPrefs.putFloat("masterVolume", 100);
            masterVolume = 100;
        }
        return masterVolume;
    }

    /**
     * A getter for the tiledMap variable
     *
     * @return the value of the tiledMap variable
     */
    public static int getLevel(int save) {
        int level;
        if (gdxPrefs.contains("tiledMap" + save))
            level = gdxPrefs.getInteger("tiledMap" + save);
        else {
            gdxPrefs.putInteger("tiledMap" + save, Finals.firstLevel);
            level = Finals.firstLevel;
        }
        return level;
    }

    /**
     * A setter for the tiledMap variable
     *
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
    }

    public static void dispose() {

    }
}