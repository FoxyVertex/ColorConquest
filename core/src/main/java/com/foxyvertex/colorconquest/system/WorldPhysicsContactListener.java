package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.component.Bullet;
import com.foxyvertex.colorconquest.component.ColorComponent;
import com.foxyvertex.colorconquest.component.Player;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Tint;
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
                Globals.gameScreen.nextLevel();
                break;
            case Finals.PLAYER_BIT | Finals.BLOCK_BIT:
                Entity player1, block1;
                Fixture player1F, block1F;
                if (fixtureA.getFilterData().categoryBits == Finals.PLAYER_BIT) {
                    player1 = (Entity) fixtureA.getUserData();
                    player1F = fixtureA;
                    block1 = (Entity) fixtureB.getUserData();
                    block1F = fixtureB;
                } else if (fixtureB.getFilterData().categoryBits == Finals.PLAYER_BIT) {
                    player1 = (Entity) fixtureB.getUserData();
                    player1F = fixtureB;
                    block1 = (Entity) fixtureA.getUserData();
                    block1F = fixtureA;
                } else {
                    player1 = (Entity) fixtureA.getUserData();
                    player1F = fixtureA;
                    block1 = (Entity) fixtureB.getUserData();
                    block1F = fixtureB;
                }
                Color blockColor = null;
                if (block1.getComponent(ColorComponent.class) != null) blockColor = new Color(block1.getComponent(ColorComponent.class).r, block1.getComponent(ColorComponent.class).g, block1.getComponent(ColorComponent.class).b, 1f);
                Player playerComp = getWorld().getSystem(PlayerSystem.class).player.getComponent(Player.class);
                if (blockColor != null) {
                    if (blockColor.r == 1 && blockColor.g == 1 && blockColor.b == 1) {
                        playerComp.runSpeed = playerComp.minRunSpeed;
                        playerComp.jumpForce = playerComp.minJumpForce;
                    } else if (blockColor.r == 1 && blockColor.g == 0 && blockColor.b == 0) {
                        playerComp.runSpeed = playerComp.maxRunSpeed;
                    } else if (blockColor.r == 0 && blockColor.g == 1 && blockColor.b == 0) {
                        playerComp.jumpForce = playerComp.maxJumpForce;
                    } else if (blockColor.r == 0 && blockColor.g == 0 && blockColor.b == 1) {
                        block1F.setRestitution(0.5f);
                    }
                } else {
                    playerComp.runSpeed = playerComp.minRunSpeed;
                    playerComp.jumpForce = playerComp.minJumpForce;
                }
                break;

            case Finals.BLOCK_BIT | Finals.BULLET_BIT:
                Entity block2;
                Entity bullet2;
                if (fixtureA.getFilterData().categoryBits == Finals.BLOCK_BIT) {
                    block2 = (Entity) fixtureA.getUserData();
                    bullet2 = (Entity) fixtureB.getUserData();
                } else if (fixtureA.getFilterData().categoryBits == Finals.BULLET_BIT) {
                    block2 = (Entity) fixtureB.getUserData();
                    bullet2 = (Entity) fixtureA.getUserData();
                } else {
                    block2 = (Entity) fixtureA.getUserData();
                    bullet2 = (Entity) fixtureB.getUserData();
                }
                if (block2.getComponent(ColorComponent.class) == null) break;
                if (bullet2.getComponent(Bullet.class) == null) break;
                if (bullet2.getComponent(Bullet.class).color == null) break;
                block2.getComponent(ColorComponent.class).r = bullet2.getComponent(Bullet.class).color.r;block2.getComponent(ColorComponent.class).g = bullet2.getComponent(Bullet.class).color.g;block2.getComponent(ColorComponent.class).b = bullet2.getComponent(Bullet.class).color.b;
                if (block2.getComponent(ColorComponent.class) != null) {
                    block2.getComponent(ColorComponent.class).r = bullet2.getComponent(Bullet.class).color.r - 0.6f;
                    block2.getComponent(ColorComponent.class).g = bullet2.getComponent(Bullet.class).color.g;
                    block2.getComponent(ColorComponent.class).b = bullet2.getComponent(Bullet.class).color.b;
                }
                break;

            case Finals.BULLET_BIT:
                // TODO: 2/16/2017 implement bullet-on-bullet collision
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        //An integer that tells the catagoryBits of the two fixtures
        int collisionDefinition = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;
        //Checks to see if a select two kinds of fixtures collide.
        switch (collisionDefinition) {
            case Finals.PLAYER_BIT | Finals.END_LEVEL_BIT:
                break;
            case Finals.PLAYER_BIT | Finals.BLOCK_BIT:
                Entity player1, block1;
                Fixture player1F, block1F;
                if (fixtureA.getFilterData().categoryBits == Finals.PLAYER_BIT) {
                    player1 = (Entity) fixtureA.getUserData();
                    player1F = fixtureA;
                    block1 = (Entity) fixtureB.getUserData();
                    block1F = fixtureB;
                } else if (fixtureB.getFilterData().categoryBits == Finals.PLAYER_BIT) {
                    player1 = (Entity) fixtureB.getUserData();
                    player1F = fixtureB;
                    block1 = (Entity) fixtureA.getUserData();
                    block1F = fixtureA;
                } else {
                    player1 = (Entity) fixtureA.getUserData();
                    player1F = fixtureA;
                    block1 = (Entity) fixtureB.getUserData();
                    block1F = fixtureB;
                }
                Color blockColor = null;
                if (block1.getComponent(ColorComponent.class) != null) blockColor = new Color(block1.getComponent(Tint.class).getTint().r, block1.getComponent(Tint.class).getTint().g, block1.getComponent(Tint.class).getTint().b, 1f);
                Player playerComp = getWorld().getSystem(PlayerSystem.class).player.getComponent(Player.class);
                if (blockColor != null) {
                    if (blockColor.r == 1 && blockColor.g == 1 && blockColor.b == 1) {

                    } else if (blockColor.r == 1 && blockColor.g == 0 && blockColor.b == 0) {
                        playerComp.runSpeed = playerComp.minRunSpeed;
                    } else if (blockColor.r == 0 && blockColor.g == 1 && blockColor.b == 0) {
                        playerComp.jumpForce = playerComp.minJumpForce;
                    } else if (blockColor.r == 0 && blockColor.g == 0 && blockColor.b == 1) {
                        block1F.setRestitution(0.5f);
                    }
                } else {
                    playerComp.runSpeed = playerComp.minRunSpeed;
                    playerComp.jumpForce = playerComp.minJumpForce;
                }
                break;

            case Finals.BLOCK_BIT | Finals.BULLET_BIT:
                Entity block2;
                Entity bullet2;
                if (fixtureA.getFilterData().categoryBits == Finals.BLOCK_BIT) {
                    block2 = (Entity) fixtureA.getUserData();
                    bullet2 = (Entity) fixtureB.getUserData();
                } else if (fixtureA.getFilterData().categoryBits == Finals.BULLET_BIT) {
                    block2 = (Entity) fixtureB.getUserData();
                    bullet2 = (Entity) fixtureA.getUserData();
                } else {
                    block2 = (Entity) fixtureA.getUserData();
                    bullet2 = (Entity) fixtureB.getUserData();
                }
                break;

            case Finals.BULLET_BIT:
                break;
        }
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
