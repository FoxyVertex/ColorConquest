package com.foxyvertex.colorconquest.game;

/**
 * Created by aidan on 1/23/17.
 */

public abstract class GameState {

    public abstract void update(float delta);

    public abstract void render();

    public abstract void start();

    public abstract void stop();

    public abstract void dispose();

}
