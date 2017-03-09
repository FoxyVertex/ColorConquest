package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.foxyvertex.colorconquest.component.ColorComponent;
import com.kotcrab.vis.runtime.component.Tint;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/28/2017.
 */

/**
 * SetColorComponentSystem sets the ColorComponent of all the entities who need it
 */
public class SetColorComponentSystem extends BaseEntitySystem {
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

    /**
     * processSystem is called every frame at a max of 60 times per second. It manages the movement of the zombies.
     */
    @Override
    protected void processSystem() {

    }

    /**
     * inserted is called every time an entity is added to the scene
     * @param entities
     */
    @Override
    public void inserted(IntBag entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = getWorld().getEntity(entities.get(i));
            if (variablesCm.get(e) != null) {
                Color tint = tintCm.get(e).getTint();
                if (variablesCm.get(e).get("color") == null) {
                    e.edit().add(new ColorComponent(tint));
                }
            }
        }

    }
}
