package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.foxyvertex.colorconquest.component.ToDestroy;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;

/**
 * Created by aidan on 3/4/2017.
 */

/**
 * ToDestroySystem handles all of the entities with the ToDestroy component and destroys them at the appropriate time
 */
public class ToDestroySystem extends EntitySystem {
    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public ToDestroySystem() {
        super(Aspect.all(ToDestroy.class));
    }

    /**
     * processSystem is called every frame at a max of 60 times per second. It manages the obliteration of entities.
     */
    @Override
    protected void processSystem() {
        for (Entity e : getEntities()) {
            ToDestroy tdc = e.getComponent(ToDestroy.class);
            tdc.currentTimer -= (getWorld().getDelta()*1000);
            if (tdc.currentTimer <= 0) {
                if (e.getComponent(PhysicsBody.class) != null) getWorld().getSystem(PhysicsSystem.class).getPhysicsWorld().destroyBody(e.getComponent(PhysicsBody.class).body);
                e.deleteFromWorld();
            }
        }
    }
}
