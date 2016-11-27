package com.thefoxarmy.rainbowwarrior.managers.levels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 11/27/2016.
 */

public class World {
    public List<Level> levels;
    public String name;

    public World(String name) {
        this.name = name;
        levels = new ArrayList<Level>();
    }

    public void addLevel(Level level) {
        levels.add(level);
    }
    public void addLevel(String name, String filePath) {
        levels.add(new Level(name, filePath));
    }
}
