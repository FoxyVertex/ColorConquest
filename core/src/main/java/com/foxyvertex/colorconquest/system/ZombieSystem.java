package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.component.Zombie;
import com.kotcrab.vis.runtime.component.PhysicsBody;

/**
 * Created by aidan on 3/6/17.
 */

public class ZombieSystem extends EntitySystem {
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
            Vector2 position1 = e.getComponent(PhysicsBody.class).body.getPosition();
            Vector2 position2 = getWorld().getSystem(PlayerSystem.class).body.getPosition();
            float distanceBetweenThem = position1.dst(position2);
            if (distanceBetweenThem < 5) {
                if (position1.x > position2.x)
                    e.getComponent(PhysicsBody.class).body.applyLinearImpulse(new Vector2(-55f * 0.0065f, 0), e.getComponent(PhysicsBody.class).body.getWorldCenter(), true);
                else if (position2.x > position1.x)
                    e.getComponent(PhysicsBody.class).body.applyLinearImpulse(new Vector2(55f * 0.0065f, 0), e.getComponent(PhysicsBody.class).body.getWorldCenter(), true);
            }
        }
    }

    /**
     * Called when a new zombie is created
     * @param e the new zombie
     */
    public void inserted(Entity e) {
        Gdx.app.log(Finals.ANSI_CYAN + "Zombie System" + Finals.ANSI_RESET, "New zombie created.");
    }
}
