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
import com.foxyvertex.colorconquest.ColorEffects;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.component.Bullet;
import com.foxyvertex.colorconquest.component.ColorComponent;
import com.foxyvertex.colorconquest.component.Health;
import com.foxyvertex.colorconquest.component.Player;
import com.foxyvertex.colorconquest.component.ToDestroy;
import com.foxyvertex.colorconquest.tools.Utilities;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Tint;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by seth on 11/26/2016.
 * <p>
 * This class handles all collisions between any two box2d fixtures within the world
 */

public class CollisionSystem extends EntitySystem implements ContactListener, AfterSceneInit {
    ComponentMapper<PhysicsBody> physicsBodyCm;
    public static Array<Body> deadBodies = new Array<Body>();

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public CollisionSystem() {
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
                        ColorEffects.WHITE.go(player1, block1);
                    } else if (blockColor.r == 1 && blockColor.g == 0 && blockColor.b == 0) {
                        ColorEffects.RED.go(player1, block1);
                    } else if (blockColor.r == 0 && blockColor.g == 1 && blockColor.b == 0) {
                        ColorEffects.GREEN.go(player1, block1);
                    } else if (blockColor.r == 0 && blockColor.g == 0 && blockColor.b == 1) {
                        ColorEffects.BLUE.go(player1, block1);
                    }
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
                if (block2.getComponent(ColorComponent.class) != null) {
                    ColorComponent cc1 = block2.getComponent(ColorComponent.class);
                    Color bc = bullet2.getComponent(Bullet.class).color;
                    if (bc.r == 1 && bc.g == 1 && bc.b == 1) {
                        cc1.r =+ Utilities.map(10, 0, 255, 0, 1);
                        cc1.g =+ Utilities.map(10, 0, 255, 0, 1);
                        cc1.b =+ Utilities.map(10, 0, 255, 0, 1);
                    } else if (bc.r == 1 && bc.g == 0 && bc.b == 0) {
                        cc1.r = Utilities.clamp(cc1.r + Utilities.map(10, 0, 255, 0, 1), 0, 1);
                    } else if (bc.r == 0 && bc.g == 1 && bc.b == 0) {
                        cc1.g = Utilities.clamp(cc1.g + Utilities.map(10, 0, 255, 0, 1), 0, 1);
                    } else if (bc.r == 0 && bc.g == 0 && bc.b == 1) {
                        cc1.b = Utilities.clamp(cc1.b + Utilities.map(10, 0, 255, 0, 1), 0, 1);
                    }
                }
                bullet2.edit().add(new ToDestroy(100));
                break;

            case Finals.BULLET_BIT:
                // TODO: 2/16/2017 implement bullet-on-bullet collision
                break;
            case Finals.PLAYER_BIT | Finals.ZOMBIE_BIT:
                Entity player4, zombie4;
                Fixture player4F, block4F;
                if (fixtureA.getFilterData().categoryBits == Finals.PLAYER_BIT) {
                    player4 = (Entity) fixtureA.getUserData();
                    player4F = fixtureA;
                    zombie4 = (Entity) fixtureB.getUserData();
                    block4F = fixtureB;
                } else if (fixtureB.getFilterData().categoryBits == Finals.PLAYER_BIT) {
                    player4 = (Entity) fixtureB.getUserData();
                    player4F = fixtureB;
                    zombie4 = (Entity) fixtureA.getUserData();
                    block4F = fixtureA;
                } else {
                    player4 = (Entity) fixtureA.getUserData();
                    player4F = fixtureA;
                    zombie4 = (Entity) fixtureB.getUserData();
                    block4F = fixtureB;
                }

                zombie4.getComponent(Health.class).deathCallback.run(zombie4);
                getWorld().getSystem(AnimationSystem.class).changeAnimState(zombie4, "hit", false, false, true);
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
                if (block1.getComponent(ColorComponent.class) != null) blockColor = new Color(block1.getComponent(ColorComponent.class).r, block1.getComponent(ColorComponent.class).g, block1.getComponent(ColorComponent.class).b, 1f);
                Player playerComp = getWorld().getSystem(PlayerSystem.class).player.getComponent(Player.class);
                if (blockColor != null) {
                    if (blockColor.r == 1 && blockColor.g == 1 && blockColor.b == 1) {
                        ColorEffects.WHITE.og(player1, block1);
                    } else if (blockColor.r == 1 && blockColor.g == 0 && blockColor.b == 0) {
                        ColorEffects.RED.og(player1, block1);
                    } else if (blockColor.r == 0 && blockColor.g == 1 && blockColor.b == 0) {
                        ColorEffects.GREEN.og(player1, block1);
                    } else if (blockColor.r == 0 && blockColor.g == 0 && blockColor.b == 1) {
                        ColorEffects.BLUE.og(player1, block1);
                    }
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
