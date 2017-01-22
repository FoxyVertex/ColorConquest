package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.foxyvertex.colorconquest.Globals;

/**
 * Created by seth on 1/19/17.
 */

public abstract class SpriteBody extends Sprite {

    protected Body body;
    protected Vector2 spawnPoint;
    protected Fixture primaryFixture;
    protected short CATIGORY_BIT = 0;

    protected SpriteBody(Vector2 spawnpoint) {
        this.spawnPoint = spawnpoint;
    }

    protected void def(Shape shape) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(spawnPoint);
        body = Globals.gameScreen.getWorld().createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = this.CATIGORY_BIT;
        fdef.shape = shape;
        primaryFixture = body.createFixture(fdef);
    }

    public void tick(float delta) {
        setBounds(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, bodySize().x, bodySize().y);
        setRotation((float) Math.toDegrees(body.getAngle()));
    }

    public Vector2 bodySize() {
        Vector2 vect = new Vector2(0, 0);
        for (Fixture f : body.getFixtureList()) {
            CircleShape shape = (CircleShape) f.getShape();
            vect = new Vector2(shape.getRadius() * 2, shape.getRadius() * 2);
        }
        return vect;
    }
}