package com.thefoxarmy.rainbowwarrior.managers;

import com.thefoxarmy.rainbowwarrior.managers.levels.Level;
import com.thefoxarmy.rainbowwarrior.managers.levels.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 11/27/2016.
 *
 * The level manager will hold an array of the levels.
 */

public class Levels {
    public static List<World> worlds;

    public static void load() {
        worlds = new ArrayList<World>();

        World testWorld = new World("Test World");
        Level testWorldLevel1 = new Level("Test One", "levels/test.tmx");
        Level testWorldLevel2 = new Level("Test Two", "levels/level2.tmx");
        testWorld.addLevel(testWorldLevel1);
        testWorld.addLevel(testWorldLevel2);

        worlds.add(testWorld);
    }
}
