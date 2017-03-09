package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/28/2017.
 */

/**
 * SetBox2DUserDataSystem sets the user data of each box2d body to it's corresponding artemis entity
 */
public class SetBox2DUserDataSystem extends BaseEntitySystem {
    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public SetBox2DUserDataSystem() {
        super(Aspect.all(PhysicsBody.class));
    }

    /**
     * processSystem is called every frame at a max of 60 times per second. It manages the movement of the zombies.
     */
    @Override
    protected void processSystem() {

    }

    /**
     * inserted gets called every time an entity is created that has a body
     * @param entities
     */
    @Override
    public void inserted(IntBag entities) {
        for (int i = 0; i < entities.size(); i++) {
            Body tmp = getWorld().getEntity(entities.get(i)).getComponent(PhysicsBody.class).body;
            for (Fixture f : tmp.getFixtureList()) {
                f.setUserData(getWorld().getEntity(entities.get(i)));
            }
        }
    }
}
