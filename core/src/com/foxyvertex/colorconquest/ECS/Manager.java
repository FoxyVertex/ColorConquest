package com.foxyvertex.colorconquest.ECS;

import com.badlogic.gdx.utils.Array;

/**
 * Created by aidan on 12/31/2016.
 */

public class Manager {

    private static Array<Object> gameObjects = new Array<Object>();

    public static void addObject(Object object) {
        gameObjects.add(object);
    }

    public static void addObjects(Object... objects) {
        for (Object object : objects) {
            addObject(object);
        }
    }

    public static void setObjects(Array<Object> objects) {
        gameObjects = objects;
    }

    public static Object removeObject(Object object) {
        for (int i = 0; i < gameObjects.size; i++) {
            Object obj = gameObjects.get(i);
            if (obj == object) {
                gameObjects.removeIndex(i);
                obj.obliterate();
                return obj;
            }
        }
        return null;
    }

    public static void removeAllObjects() {
        for (int i = 0; i < gameObjects.size; i++) {
            Object obj = gameObjects.get(i);
            obj.obliterate();
        }
    }

    public static Array<Object> getGameObjects() {
        return gameObjects;
    }

    public static void tick(float delta) {
        for (int i = 0; i < gameObjects.size; i++) {
            gameObjects.get(i).tick(delta);
        }
    }
}
