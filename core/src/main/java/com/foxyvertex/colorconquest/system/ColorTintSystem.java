package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.foxyvertex.colorconquest.component.ColorComponent;
import com.kotcrab.vis.runtime.component.Tint;

/**
 * Created by aidan on 2/28/2017.
 */

public class ColorTintSystem extends EntitySystem {
    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public ColorTintSystem() {
        super(Aspect.all(Tint.class));
    }

    @Override
    protected void processSystem() {
        for (Entity e : getEntities()) {
            if (e.getComponent(ColorComponent.class) != null) if (e.getComponent(ColorComponent.class).color != null) e.getComponent(Tint.class).setTint(e.getComponent(ColorComponent.class).color);
        }
    }
}
