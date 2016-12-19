package com.thefoxarmy.rainbowwarrior.managers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 12/19/2016.
 */

public class Levels {
    public static List<Level> levels = new ArrayList<Level>();
    public static int currentLevel = 0;
    public static String pathPrefix = "levels/";

    public static void load() {
        Level level1 = new Level();
        Level level2 = new Level();

        level1.path = pathPrefix + "test.tmx";
        level2.path = pathPrefix + "level2.tmx";

        level1.hasCutscene = false;
        level2.hasCutscene = false;

        level1.nextLevel = level2;
        level2.nextLevel = level1;

        level1.index = 0;
        level2.index = 1;

        levels.add(level1);
        levels.add(level2);

        currentLevel = UserPrefs.getLevel();
    }

    public static class Level {
        public String path;
        public Level nextLevel;
        public boolean hasCutscene;
        public int index;
    }
}
