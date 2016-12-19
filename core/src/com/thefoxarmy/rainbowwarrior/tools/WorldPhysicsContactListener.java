package com.thefoxarmy.rainbowwarrior.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.thefoxarmy.rainbowwarrior.FinalGlobals;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;

/**
 * Created by seth on 11/26/2016.
 *
 * This class handles all collisions between any two box2d fixtures within the world
 */

public class WorldPhysicsContactListener implements ContactListener {

    private GameScreen screen;
    /**
     * Instantiates an instance of a worldContactlistener for the world to use.
     * @param screen Used in order to access other objects within the current tiledMap loaded.
     */
    public WorldPhysicsContactListener(GameScreen screen) {
        this.screen = screen;
    }

    /**
     * This method is called by the world at the beginning of a between any two fixtures
     * @param contact this is all of the data for two fixtures.
     */
    @Override
    public void beginContact(Contact contact) {
        //Store the fixtures from the collsion
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        //An integer that tells the catagoryBits of the two fixtures
        int collisionDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        //Checks to see if a select two kinds of fixtures collide.
        switch (collisionDefinition) {
            case FinalGlobals.PLAYER_BIT | FinalGlobals.END_LEVEL_BIT:
                screen.switchLevel();
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
}
