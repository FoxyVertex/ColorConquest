package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
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
    public boolean setToDestroy = false;
    public Fixture primaryFixture;
    public Color   color;
    Vector2 spawnPoint;
    float destructionTime = 0;
    private boolean wasDestroyed = false;
    private boolean shouldFlip;
    public float maxResitution = 3f;
    public float minResitution = 0.05f;

    public float resitituion = 0;

    SpriteBody(Vector2 spawnPoint) {
        this.spawnPoint = spawnPoint;
        EntityController.entities.add(this);
    }

    protected BodyDef.BodyType bodyType() {
        return BodyDef.BodyType.DynamicBody;
    }

    protected short CATIGORY_BIT() {
        return 0;
    }

    protected short MASKED_BIT() {
        return Finals.EVERYTHING_BIT;
    }

    protected boolean isSensor() {
        return false;
    }

    void def(Shape shape) {
        BodyDef bdef = new BodyDef();
        bdef.type = bodyType();
        bdef.position.set(spawnPoint);
        body = Globals.gameMan.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = this.CATIGORY_BIT();
        //fdef.filter.categoryBits = this.MASKED_BIT();
        fdef.shape = shape;
        fdef.isSensor = this.isSensor();
        primaryFixture = body.createFixture(fdef);
        primaryFixture.setUserData(this);
    }

    void def(Shape shape, Vector2 newSpawnPoint) {
        BodyDef bdef = new BodyDef();
        bdef.type = bodyType();
        bdef.position.set(newSpawnPoint);
        body = Globals.gameMan.world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        fdef.filter.categoryBits = this.CATIGORY_BIT();
        //fdef.filter.maskBits = this.MASKED_BIT();
        fdef.isSensor = isSensor();
        fdef.shape = shape;
        primaryFixture = body.createFixture(fdef);
        primaryFixture.setUserData(this);
    }

    public void tick(float delta) {

        if (    setToDestroy && !wasDestroyed && Globals.gameMan.running.hasWorldStepped) {
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

        setBounds(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2, primaryFixture.getShape().getRadius() * 2, primaryFixture.getShape().getRadius() * 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
    }


    protected void dispose() {
        //this.getTexture().dispose();
        //this.shape.dispose();
    }


}