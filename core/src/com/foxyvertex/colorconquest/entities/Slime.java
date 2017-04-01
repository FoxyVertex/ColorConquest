package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.foxyvertex.colorconquest.Finals;

/**
 * Created by seth on 3/23/2017.
 */

public class Slime extends SpriteBody {

    Vector2   spawnPoint;
    Pixmap    tex;
    Vector2[] verts;

    Slime(Vector2 spawnPoint, Color color) {
        super(spawnPoint);
        this.spawnPoint = spawnPoint;
        super.color = color;
        def();
        tex = new Pixmap(25, 5, Pixmap.Format.RGBA8888);
        tex.setColor(super.color);
        tex.fill();

        setRegion(new Texture(tex));
    }

    protected short CATIGORY_BIT() {
        return Finals.SLIME_BIT;
    }

    protected short MASKED_BIT() {
        return Finals.BLOCK_BIT;
    }

    protected BodyDef.BodyType bodyType() {
        return BodyDef.BodyType.StaticBody;
    }

    protected boolean isSensor() {
        return true;
    }

    private void def() {
        PolygonShape shape = new PolygonShape();
        Vector2[] verts = {new Vector2(0, 0).scl(1 / Finals.PPM),
                new Vector2(30, 0).scl(1 / Finals.PPM),
                new Vector2(0, 5).scl(1 / Finals.PPM),
                new Vector2(30, 5).scl(1 / Finals.PPM)};
        this.verts = verts;
        shape.set(verts);
        super.def(shape);
    }

    public void tick(float delta) {
        //super.tick(delta);
        setPosition(body.getPosition().x, body.getPosition().y);
    }
}