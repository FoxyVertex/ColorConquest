package com.foxyvertex.colorconquest.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.entities.Barrier;
import com.foxyvertex.colorconquest.entities.Block;
import com.foxyvertex.colorconquest.entities.Bullet;
import com.foxyvertex.colorconquest.entities.Interactant;
import com.foxyvertex.colorconquest.entities.Player;
import com.foxyvertex.colorconquest.entities.Slitherikter;
import com.foxyvertex.colorconquest.entities.SpriteBody;
import com.foxyvertex.colorconquest.game.GameManager;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.managers.Levels;

/**
 * Created by seth on 11/26/2016.
 * <p>
 * This class handles all collisions between any two box2d fixtures within the world
 */

public class WorldPhysicsContactListener implements ContactListener {

    public static Array<Body> deadBodies;

    /**
     * Instantiates an instance of a worldContactlistener for the world to use.
     *
     * @param gameManager Used in order to access other objects within the current tiledMap loaded.
     */
    public WorldPhysicsContactListener(GameManager gameManager) {
        deadBodies = new Array<Body>( );
    }

    /**
     * This method is called by the world at the beginning of a between any two fixtures
     *
     * @param contact this is all of the data for two fixtures.
     */
    @Override
    public void beginContact(Contact contact) {

        //Store the fixtures from the collsion
        Fixture fixtureA = contact.getFixtureA( );
        Fixture fixtureB = contact.getFixtureB( );
        //An integer that tells the catagoryBits of the two fixtures
        int collisionDefinition = fixtureA.getFilterData( ).categoryBits | fixtureB.getFilterData( ).categoryBits;
        //Checks to see if a select two kinds of fixtures collide.
        switch (collisionDefinition) {
            case Finals.PLAYER_BIT | Finals.END_LEVEL_BIT:

                Levels.Level nextLevel = Globals.gameMan.currentLevel.nextLevel;
                if (nextLevel != null) Globals.gameMan.nextLevel( nextLevel );
                else {
                    Assets.playMusic( Assets.menuMusic );
                    Globals.game.setScreen( Globals.menuScreen );
                }
                break;
            case Finals.SLITHERIKTER_BIT | Finals.BLOCK_BIT:
            case Finals.PLAYER_BIT | Finals.SLIME_BIT:
            case Finals.PLAYER_BIT | Finals.BLOCK_BIT:
                //Determines which fixture is the player and which is the block
                // This can be shortened to 1 line!
                Fixture objectCollidedWith = (fixtureA.getUserData( ) instanceof Interactant) ? fixtureB : fixtureA;
                Interactant objectTouched = (fixtureA.getUserData( ) instanceof Interactant) ? (Interactant) fixtureA.getUserData( ) : (Interactant) fixtureB.getUserData( );
                Color blockColor = ((SpriteBody) objectCollidedWith.getUserData( )).color;
                if (blockColor != null) {

                    //Red Damage
                    if ((blockColor.r > 0f && blockColor.g < 0.5f && blockColor.b < 0.5f)) {
                        objectTouched.isBeingDamaged = true;
                        objectTouched.DPS = objectTouched.minDPS + blockColor.r * (objectTouched.maxDPS - objectTouched.minDPS);
                    }
                    //Green Jump
                    if ((blockColor.g > 0f && blockColor.r < 0.5f && blockColor.b < 0.5f)) {
                        objectTouched.jumpForce = objectTouched.minJumpForce + blockColor.g * (objectTouched.maxJumpForce - objectTouched.minJumpForce);

                    }
                        //Blue Restitution
                    if ((blockColor.b > 0f && blockColor.r < 0.5f && blockColor.g < 0.5f)) {
                        //objectTouched.primaryFixture.setRestitution( ((SpriteBody) objectCollidedWith.getUserData( )).minResitution + blockColor.g * (((SpriteBody) objectCollidedWith.getUserData( )).maxResitution - ((SpriteBody) objectCollidedWith.getUserData( )).minResitution) );
                        objectTouched.primaryFixture.setRestitution( 10f );
                        Gdx.app.log( "", "" + blockColor );
                    }
                        //Yellow Speed
                    if (blockColor.r > 0.5f && blockColor.g > 0.5f && blockColor.b < 0.5f)
                        objectTouched.runSpeed = objectTouched.minRunSpeed + (blockColor.g + blockColor.r)/2 * (objectTouched.maxRunSpeed - objectTouched.minJumpForce);
                    //Purple Slow Speed + No Fall Damage
                    if (blockColor.r > 0.5f && blockColor.b > 0.5f && blockColor.g < 0.5f) {
                        objectTouched.runSpeed /= 2;
                        objectTouched.doFallDamage = false;
                    }
                    //Teal Invincibility + no shoot + ammo reduction
                    if (blockColor.g > 0.5f && blockColor.b > 0.5f && blockColor.r < 0.5f) {
                        objectTouched.isInvulnerable = true;
                        if (objectTouched instanceof Player) {
                            ((Player) objectTouched).reduceAmmo = true;
                            ((Player) objectTouched).canShoot = false;
                        }
                    }
                }
                break;

            case Finals.BLOCK_BIT | Finals.BULLET_BIT:
                //initialize the objects to their proper collision fixtures
                Block attackedBlock = (fixtureA.getUserData( ) instanceof Block) ? (Block) fixtureA.getUserData( ) : (Block) fixtureB.getUserData( );
                Bullet bullet = (fixtureA.getUserData( ) instanceof Bullet) ? (Bullet) fixtureA.getUserData( ) : (Bullet) fixtureB.getUserData( );

                //Determine which color the bullet is (Because Java sucks (Because there is no pass by reference))
                float RGBColors[] = {bullet.color.r, bullet.color.g, bullet.color.b};
                switch (Utilities.findBiggestIndex( RGBColors )) {
                    case 0:
                        attackedBlock.color.r = Utilities.clamp( attackedBlock.color.r + 0.1f, 0, 1 );
                        if (attackedBlock.color.g >= 0.5f)
                            attackedBlock.color.g = Utilities.clamp( attackedBlock.color.g - 0.1f, 0, 1 );
                        if (attackedBlock.color.b >= 0.5f)
                            attackedBlock.color.b = Utilities.clamp( attackedBlock.color.b - 0.1f, 0, 1 );

                        break;
                    case 1:
                        attackedBlock.color.g = Utilities.clamp( attackedBlock.color.g + 0.1f, 0, 1 );
                        if (attackedBlock.color.r >= 0.5f)
                            attackedBlock.color.r = Utilities.clamp( attackedBlock.color.r - 0.1f, 0, 1 );
                        if (attackedBlock.color.b >= 0.5f)
                            attackedBlock.color.b = Utilities.clamp( attackedBlock.color.b - 0.1f, 0, 1 );
                        break;
                    case 2:
                        attackedBlock.color.b = Utilities.clamp( attackedBlock.color.b + 0.1f, 0, 1 );
                        if (attackedBlock.color.r >= 0.5f)
                            attackedBlock.color.r = Utilities.clamp( attackedBlock.color.r - 0.1f, 0, 1 );
                        if (attackedBlock.color.g >= 0.5f)
                            attackedBlock.color.g = Utilities.clamp( attackedBlock.color.g - 0.1f, 0, 1 );
                        break;
                }
                attackedBlock.color.a = Utilities.clamp( attackedBlock.color.a + 0.05f, 0, 1 );

                attackedBlock.rainbow = (attackedBlock.color.r == 1 && attackedBlock.color.g == 1 && attackedBlock.color.b == 1 && attackedBlock.color.a == 1 || attackedBlock.rainbow);

                attackedBlock.tintTexture( );
                bullet.setToDestroy = true;

                bullet.body.applyLinearImpulse( new Vector2( 0, 5 ), bullet.body.getWorldCenter( ), false );


                break;

            case Finals.BULLET_BIT:
                if (fixtureA.getShape( ).getRadius( ) > fixtureB.getShape( ).getRadius( )) {
                    fixtureA.getShape( ).setRadius( fixtureA.getShape( ).getRadius( ) + fixtureB.getShape( ).getRadius( ) );
                    ((Bullet) fixtureA.getUserData( )).reDraw( );
                    deadBodies.add( fixtureB.getBody( ) );
                } else {
                    fixtureB.getShape( ).setRadius( fixtureA.getShape( ).getRadius( ) + fixtureB.getShape( ).getRadius( ) );
                    ((Bullet) fixtureB.getUserData( )).reDraw( );
                    deadBodies.add( fixtureA.getBody( ) );
                }
                break;
            case Finals.BULLET_BIT | Finals.SLITHERIKTER_BIT:
                if (fixtureA.getUserData( ) instanceof Slitherikter)
                    ((Slitherikter) fixtureA.getUserData( )).attacked( (Bullet) fixtureB.getUserData( ) );
                else
                    ((Slitherikter) fixtureB.getUserData( )).attacked( (Bullet) fixtureA.getUserData( ) );
                break;
            case Finals.ENEMY_BUFFER_BIT | Finals.BLOCK_BIT:

                if (fixtureA.getUserData( ) instanceof Slitherikter) {
                    ((Slitherikter) fixtureA.getUserData( )).shouldFlip = !((Slitherikter) fixtureA.getUserData( )).shouldFlip;
                } else {
                    ((Slitherikter) fixtureB.getUserData( )).shouldFlip = !((Slitherikter) fixtureB.getUserData( )).shouldFlip;
                }
                break;
            case Finals.SLITHERIKTER_BIT | Finals.SLIME_BIT:
                ((Slitherikter) ((fixtureA.getFilterData( ).categoryBits == Finals.SLITHERIKTER_BIT) ? fixtureA.getUserData( ) : fixtureB.getUserData( ))).isColidingWithSlime = true;
                break;
            case Finals.PLAYER_BIT | Finals.BARRIER_BIT:
                //IF the player's Downward force is greater than or equal to the force required to break the barrier
                if (((Player) ((fixtureA.getUserData( ) instanceof Player) ? fixtureA.getUserData( ) : fixtureB.getUserData( ))).body.getLinearVelocity( ).y <= ((Barrier) ((fixtureA.getUserData( ) instanceof Barrier) ? fixtureA.getUserData( ) : fixtureB.getUserData( ))).requiredBreakingForce) {    //Break the Barrier
                     ((Barrier) ((fixtureA.getUserData( ) instanceof Barrier) ? fixtureA.getUserData( ) : fixtureB.getUserData( ))).setToDestroy = true;
                }
                break;
            case Finals.PLAYER_BIT | Finals.ENEMY_BUFFER_BIT:
            case Finals.PLAYER_BIT | Finals.SLITHERIKTER_BIT:
                Interactant inter1 = (Interactant) fixtureA.getUserData(), inter2 = (Interactant) fixtureB.getUserData();
                if (inter1 instanceof Player || inter2 instanceof Player) {
                    (((inter1 instanceof Player) ? (Player) inter1 : (Player) inter2)).attacked( (inter1 instanceof Player) ? inter2 : inter1 );
                }
                if(inter1.body.getPosition().x > inter2.body.getPosition().x) {
                    inter1.body.applyLinearImpulse(new Vector2(2, 1), inter1.body.getWorldCenter(), true);
                    inter2.body.applyLinearImpulse(new Vector2(-2, 1), inter1.body.getWorldCenter(), true);
                } else {
                    inter1.body.applyLinearImpulse(new Vector2(-2, 1), inter1.body.getWorldCenter(), true);
                    inter2.body.applyLinearImpulse(new Vector2(2, 1), inter1.body.getWorldCenter(), true);
                }
                break;
        }
    }

    //This method is called when collision is finished It basically just resets everything
    @Override
    public void endContact(Contact contact) {
        //Store the fixtures from the collsion
        Fixture fixtureA = contact.getFixtureA( ), fixtureB = contact.getFixtureB( );
        //An integer that tells the catagoryBits of the two fixtures
        int collisionDefinition = fixtureA.getFilterData( ).categoryBits | fixtureB.getFilterData( ).categoryBits;
        //Checks to see if a select two kinds of fixtures collide.
        switch (collisionDefinition) {
            case Finals.PLAYER_BIT | Finals.SLIME_BIT:
            case Finals.PLAYER_BIT | Finals.BLOCK_BIT:
            case Finals.SLITHERIKTER_BIT | Finals.BLOCK_BIT:
                Fixture objectCollidedWith = (fixtureA.getUserData( ) instanceof Interactant) ? fixtureB : fixtureA;
                Interactant objectTouched = (fixtureA.getUserData( ) instanceof Interactant) ? (Interactant) fixtureA.getUserData( ) : (Interactant) fixtureB.getUserData( );
                objectTouched.DPS = objectTouched.minDPS; //Negate Red
                objectTouched.isBeingDamaged = false;    //
                objectCollidedWith.setRestitution( 0 ); //Negate Blue
                objectTouched.jumpForce = objectTouched.minJumpForce; //Negate Green
                objectTouched.runSpeed = objectTouched.minRunSpeed; // Negate Yellow
                objectTouched.isInvulnerable = true;   //Negate Teal
                if (objectTouched instanceof Player) {//
                    ((Player) objectTouched).reduceAmmo = false;
                    ((Player) objectTouched).canShoot = true;
                }
                objectTouched.doFallDamage = true; //Negate Purple

                break;
            case Finals.SLITHERIKTER_BIT | Finals.SLIME_BIT:
                ((Slitherikter) ((fixtureA.getFilterData( ).categoryBits == Finals.SLITHERIKTER_BIT) ? fixtureA.getUserData( ) : fixtureB.getUserData( ))).isColidingWithSlime = false;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}