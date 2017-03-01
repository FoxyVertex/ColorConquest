package com.foxyvertex.colorconquest;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.foxyvertex.colorconquest.component.ColorComponent;
import com.kotcrab.vis.runtime.component.Tint;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/28/2017.
 */

public class SetColorComponentSystem extends BaseEntitySystem implements AfterSceneInit {
    VisIDManager idManager;

    ComponentMapper<Variables> variablesCm;
    ComponentMapper<Tint> tintCm;

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public SetColorComponentSystem() {
        super(Aspect.all(Variables.class, Tint.class).exclude(ColorComponent.class));
    }

    @Override
    protected void processSystem() {

    }

    @Override
    public void inserted(IntBag entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = getWorld().getEntity(entities.get(i));
            if (variablesCm.get(e) != null) {
                if (variablesCm.get(e).get("color") == null) {
                    e.edit().add(new ColorComponent(tintCm.get(e).getTint()));
                }
            }
        }
    }

    @Override
    public void afterSceneInit() {

    }
}
