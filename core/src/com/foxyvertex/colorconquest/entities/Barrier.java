package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.managers.Assets;

/**
 * Created by seth on 3/27/17.
 */

public class Barrier extends SpriteBody {
    public float requiredBreakingForce = -10;
    MapObject mapObject;

    public Barrier(Vector2 spawnPoint, MapObject mapObject) {
        super(spawnPoint);
        this.mapObject = mapObject;
        def();
        setRegion(Assets.badlogic);
    }

    protected short CATIGORY_BIT() {
        return Finals.BARRIER_BIT;
    }

    protected BodyDef.BodyType bodyType() {
        return BodyDef.BodyType.KinematicBody;
    }

    public void def() {
        PolygonShape shape = new PolygonShape();
        Vector2[] verticies = {
                new Vector2(0, 0).scl(1 / Finals.PPM),
                new Vector2(mapObject.getProperties().get("width", Float.class), 0).scl(1 / Finals.PPM),
                new Vector2(0, mapObject.getProperties().get("height", Float.class)).scl(1 / Finals.PPM),
                new Vector2(mapObject.getProperties().get("width", Float.class), mapObject.getProperties().get("height", Float.class)).scl(1 / Finals.PPM)
        };
        shape.set(verticies);
        super.def(shape, spawnPoint);
    }
}