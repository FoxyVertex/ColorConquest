package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.foxyvertex.colorconquest.component.Health;
import com.foxyvertex.colorconquest.component.ToDestroy;

/**
 * Created by aidan on 3/11/17.
 */

public class HealthSystem extends EntitySystem {
    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public HealthSystem() {
        super(Aspect.all(Health.class));
    }

    @Override
    protected void processSystem() {
        for (Entity e : getEntities()) {
            if (e.getComponent(Health.class).currentHealth <= 0 && e.getComponent(ToDestroy.class) == null) {
                e.getComponent(Health.class).deathCallback.run(e);
            }
        }
    }
}
