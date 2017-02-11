package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;

/**
 * Created by seth on 1/19/17.
 */

public abstract class SpriteBody extends Sprite {

    public Body body;
    public Vector2 spawnPoint;
    public boolean setToDestroy = false;
    public boolean wasDestroyed = false;

    public boolean setToRedifine = false;
    public boolean Redifined = false;

    protected float destructionTime = 0;

    protected Fixture primaryFixture;
    protected BodyDef.BodyType bodyType = BodyDef.BodyType.DynamicBody;
    protected short CATIGORY_BIT = 0;
    protected short MASKED_BIT = Finals.EVERYTHING_BIT;
    protected Shape shape;

    protected SpriteBody(Vector2 spawnpoint) {
        this.spawnPoint = spawnpoint;
    }

    protected void def(Shape shape) {
        BodyDef bdef = new BodyDef();
        bdef.type = bodyType;
        bdef.position.set(spawnPoint);
        body = Globals.gameMan.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = this.CATIGORY_BIT;
        //fdef.filter.categoryBits = this.MASKED_BIT;
        fdef.shape = shape;
        primaryFixture = body.createFixture(fdef);
    }

    protected void def(Shape shape, Vector2 newSpawnPoint, boolean isStaticBody) {
        BodyDef bdef = new BodyDef();
        bdef.type = bodyType;
        bdef.position.set(newSpawnPoint);
        body = Globals.gameMan.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = this.CATIGORY_BIT;
        //fdef.filter.maskBits = this.MASKED_BIT;
        fdef.shape = shape;
        primaryFixture = body.createFixture(fdef);
    }

    public void tick(float delta) {

        if (setToDestroy && !wasDestroyed && Globals.gameMan.running.hasWorldStepped) {
            //SCHEDULING WILL CRASH THE GAME!
//            Timer.schedule(new Timer.Task() {
//                @Override
//                public void run() {
//                    Globals.gameMan.world.destroyBody(body);
//                }
//            }, destructionTime);
            Globals.gameMan.world.destroyBody(body);
            wasDestroyed = true;
        }

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

    protected void dispose() {
        //this.getTexture().dispose();
        //this.shape.dispose();
    }

}