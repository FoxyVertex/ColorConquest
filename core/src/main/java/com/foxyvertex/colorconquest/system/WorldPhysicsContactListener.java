package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by seth on 11/26/2016.
 * <p>
 * This class handles all collisions between any two box2d fixtures within the world
 */

public class WorldPhysicsContactListener extends EntitySystem implements ContactListener, AfterSceneInit {
    ComponentMapper<PhysicsBody> physicsBodyCm;
    public static Array<Body> deadBodies = new Array<Body>();

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public WorldPhysicsContactListener() {
        super(Aspect.all(PhysicsBody.class));
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        //An integer that tells the catagoryBits of the two fixtures
        int collisionDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        //Checks to see if a select two kinds of fixtures collide.
        switch (collisionDefinition) {
            case Finals.PLAYER_BIT | Finals.END_LEVEL_BIT:
                Gdx.app.log("Contact Listener", "Player collided with the end level trigger");
                Globals.gameScreen.resetLevel();
                // TODO: 2/16/2017 implement player-on-endLevel collision
                break;
            case Finals.PLAYER_BIT | Finals.BLOCK_BIT:
                Entity player;
                Entity block;
                if (fixtureA.getFilterData().categoryBits == Finals.PLAYER_BIT) {
                    player = (Entity) fixtureA.getUserData();
                    block = (Entity) fixtureB.getUserData();
                } else if (fixtureB.getFilterData().categoryBits == Finals.PLAYER_BIT) {
                    player = (Entity) fixtureB.getUserData();
                    block = (Entity) fixtureA.getUserData();
                }
                Gdx.app.log("Contact Listener", "Player collided with the environment");
                // TODO: 2/18/2017 Do what we do when the player collides with this block
                break;

            case Finals.BLOCK_BIT | Finals.BULLET_BIT:
                // TODO: 2/16/2017 implement bullet-on-block collision
                break;

            case Finals.BULLET_BIT:
                // TODO: 2/16/2017 implement bullet-on-bullet collision
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    @Override
    protected void processSystem() {
        for (Entity entity : getEntities()) {
            entity.getComponent(PhysicsBody.class).body.setUserData(entity);
        }
    }

    @Override
    public void afterSceneInit() {
        world.getSystem(PhysicsSystem.class).getPhysicsWorld().setContactListener(this);
    }
}
