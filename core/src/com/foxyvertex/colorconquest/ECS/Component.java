package com.foxyvertex.colorconquest.ECS;

/**
 * Created by aidan on 12/30/2016.
 */

public abstract class Component {
    public ComponentTypes typeID;

    public void setTypeID(ComponentTypes typeID) {
        this.typeID = typeID;
    }

    public void tick(float deltaTime) {
        this.update(deltaTime);
    }

    public abstract void update(float deltaTime);

    public abstract void create();

    public abstract void enable();

    public abstract void disable();

    public abstract void obliterate();
}
