package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.component.Zombie;
import com.kotcrab.vis.runtime.component.PhysicsBody;

import java.util.HashMap;

/**
 * Created by aidan on 3/6/17.
 */

public class ZombieSystem extends EntitySystem {
    public Array<ZombieCollision> activeCollisions = new Array<>();

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public ZombieSystem() {
        super(Aspect.all(Zombie.class));
    }

    /**
     * processSystem is called every frame at a max of 60 times per second. It manages the movement of the zombies.
     */
    @Override
    protected void processSystem() {
        for (Entity e : getEntities()) {
            Body body = e.getComponent(PhysicsBody.class).body;

            float xVel = body.getLinearVelocity().x;
            float yVel = body.getLinearVelocity().y;
            float desiredVel = 0;

            Vector2 position1 = body.getPosition();
            Vector2 position2 = getWorld().getSystem(PlayerSystem.class).body.getPosition();
            float distanceBetweenThem = position1.dst(position2);
            if (distanceBetweenThem < 5) {
                if (position1.x > position2.x)
                    desiredVel = -15;
                else if (position2.x > position1.x)
                    desiredVel = 15;
            }
            float velChange = desiredVel - xVel;
            float impulse = body.getMass() * velChange;
            body.applyForce(impulse, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        }
        for (ZombieCollision zc : activeCollisions) {
            zc.timer -= getWorld().getDelta();
            if (zc.timer < 0) {
                getWorld().getSystem(PlayerSystem.class).healthComp.currentHealth -= 4f;
                zc.timer = 2;
            }
        }
    }



    /**
     * Called when a new zombie is created
     * @param e the new zombie
     */
    public void inserted(Entity e) {

    }
}
