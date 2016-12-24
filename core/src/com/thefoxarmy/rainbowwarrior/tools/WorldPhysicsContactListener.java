package com.thefoxarmy.rainbowwarrior.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.FinalGlobals;
import com.thefoxarmy.rainbowwarrior.managers.Levels;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;
import com.thefoxarmy.rainbowwarrior.sprites.Player;

/**
 * Created by seth on 11/26/2016.
 * <p>
 * This class handles all collisions between any two box2d fixtures within the world
 */

public class WorldPhysicsContactListener implements ContactListener {

    private GameScreen screen;

    /**
     * Instantiates an instance of a worldContactlistener for the world to use.
     *
     * @param screen Used in order to access other objects within the current tiledMap loaded.
     */
    public WorldPhysicsContactListener(GameScreen screen) {
        this.screen = screen;
    }

    /**
     * This method is called by the world at the beginning of a between any two fixtures
     *
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
                Levels.Level nextLevel = screen.currentLevel.nextLevel;
                screen.switchLevel(nextLevel);
                break;
            case FinalGlobals.PLAYER_BIT | FinalGlobals.BLOCK_BIT:
                Fixture player, block;
                //Determines which fixture is the player and which is the block
                // This can be shortened to 1 line!
                if (fixtureA.getUserData() instanceof Player) {
                    player = fixtureA;
                    block = fixtureB;
                } else {
                    player = fixtureB;
                    block = fixtureA;
                }
                Color blockColor = (Color) block.getUserData();
                float RGBColors[] = {blockColor.r, blockColor.g, blockColor.b};
                //Depending on the maximum color R0G1B2 apply a property to the player
                switch (Utilities.findBiggestIndex(RGBColors)) {
                    case 0:
                        DynamicGlobals.gameScreen.player.runSpeed = DynamicGlobals.gameScreen.player.maxRunSpeed;
                        break;
                    case 1:
                        DynamicGlobals.gameScreen.player.jumpForce = DynamicGlobals.gameScreen.player.maxJumpForce;
                        break;
                    case 2:
                        block.setRestitution(1f);
                        break;
                }
        }
    }

    //This method is called when collision is finished It basically just resets everthing
    @Override
    public void endContact(Contact contact) {
        //Store the fixtures from the collsion
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        //An integer that tells the catagoryBits of the two fixtures
        int collisionDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        //Checks to see if a select two kinds of fixtures collide.
        switch (collisionDefinition) {
            case FinalGlobals.PLAYER_BIT | FinalGlobals.BLOCK_BIT:
                Fixture player, block;
                //Determines which fixture is the player and which is the block
                // This can be shortened to 1 line!
                if (fixtureA.getUserData() instanceof Player) {
                    player = fixtureA;
                    block = fixtureB;
                } else {
                    player = fixtureB;
                    block = fixtureA;
                }
                Color blockColor = (Color) block.getUserData();
                float RGBColors[] = {blockColor.r, blockColor.g, blockColor.b};


                switch (Utilities.findBiggestIndex(RGBColors)) {
                    case 0:
                        DynamicGlobals.gameScreen.player.runSpeed = DynamicGlobals.gameScreen.player.minRunSpeed;
                    case 1:
                        DynamicGlobals.gameScreen.player.jumpForce = DynamicGlobals.gameScreen.player.minJumpFox;
                        break;
                    case 2:
                        //´´block.setRestitution(0);
                }

                break;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
