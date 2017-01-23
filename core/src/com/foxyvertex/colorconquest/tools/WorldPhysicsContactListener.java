package com.foxyvertex.colorconquest.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.entities.Player;
import com.foxyvertex.colorconquest.game.GameManager;
import com.foxyvertex.colorconquest.managers.Levels;

/**
 * Created by seth on 11/26/2016.
 * <p>
 * This class handles all collisions between any two box2d fixtures within the world
 */

public class WorldPhysicsContactListener implements ContactListener {

    private GameManager gameManager;

    /**
     * Instantiates an instance of a worldContactlistener for the world to use.
     *
     * @param gameManager Used in order to access other objects within the current tiledMap loaded.
     */
    public WorldPhysicsContactListener(GameManager gameManager) {
        this.gameManager = gameManager;
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
            case Finals.PLAYER_BIT | Finals.END_LEVEL_BIT:
                Levels.Level nextLevel = Globals.gameMan.currentLevel.nextLevel;
                Globals.gameMan.switchLevel(nextLevel);
                break;
            case Finals.PLAYER_BIT | Finals.BLOCK_BIT:
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
                if (blockColor != null) {
                    float RGBColors[] = {blockColor.r, blockColor.g, blockColor.b};
                    //Depending on the maximum color R0G1B2 apply a property to the player
                    switch (Utilities.findBiggestIndex(RGBColors)) {
                        case 0:
                            Globals.gameMan.player.runSpeed = Globals.gameMan.player.maxRunSpeed;
                            break;
                        case 1:
                            Globals.gameMan.player.jumpForce = Globals.gameMan.player.maxJumpForce;
                            break;
                        case 2:
                            block.setRestitution(1f);
                            break;
                    }
                } else {
                    Globals.gameMan.player.runSpeed = Globals.gameMan.player.minRunSpeed;
                    Globals.gameMan.player.jumpForce = Globals.gameMan.player.minJumpFox;
                }
                break;
        }
    }

    //This method is called when collision is finished It basically just resets everything
    @Override
    public void endContact(Contact contact) {
        //Store the fixtures from the collsion
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        //An integer that tells the catagoryBits of the two fixtures
        int collisionDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        //Checks to see if a select two kinds of fixtures collide.
        switch (collisionDefinition) {
            case Finals.PLAYER_BIT | Finals.BLOCK_BIT:
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
                if (blockColor != null) {
                    float RGBColors[] = {blockColor.r, blockColor.g, blockColor.b};


                    switch (Utilities.findBiggestIndex(RGBColors)) {
                        case 0:
                            Globals.gameMan.player.runSpeed = Globals.gameMan.player.minRunSpeed;
                        case 1:
                            Globals.gameMan.player.jumpForce = Globals.gameMan.player.minJumpFox;
                            break;
                        case 2:
                            //block.setRestitution(0);
                    }
                } else {
                    Globals.gameMan.player.runSpeed = Globals.gameMan.player.minRunSpeed;
                    Globals.gameMan.player.jumpForce = Globals.gameMan.player.minJumpFox;
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
