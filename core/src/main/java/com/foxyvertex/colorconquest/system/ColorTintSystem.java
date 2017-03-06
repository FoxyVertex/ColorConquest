package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Color;
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
            if (e.getComponent(ColorComponent.class) != null) {
                ColorComponent cc = e.getComponent(ColorComponent.class);
                if (!(cc.r == 0 && cc.g == 0 && cc.b == 0)) {
                    e.getComponent(Tint.class).setTint(new Color(e.getComponent(ColorComponent.class).r, e.getComponent(ColorComponent.class).g, e.getComponent(ColorComponent.class).b, 1f));
                }
            }
        }
    }
}
