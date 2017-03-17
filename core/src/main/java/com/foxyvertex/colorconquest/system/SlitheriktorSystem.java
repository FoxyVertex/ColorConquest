package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.ComponentManager;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.component.Slitheriktor;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.VisSprite;

/**
 * Created by aidan on 3/6/17.
 */

public class SlitheriktorSystem extends EntitySystem {
    public Array<EnemyCollision> activeCollisions = new Array<>();

    ComponentMapper<Slitheriktor> slitheriktorCm;

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public SlitheriktorSystem() {
        super(Aspect.all(Slitheriktor.class));
    }

    /**
     * processSystem is called every frame at a max of 60 times per second. It manages the movement of the zombies.
     */
    @Override
    protected void processSystem() {
        for (EnemyCollision ec : activeCollisions) {
            ec.timer -= getWorld().getDelta();
            if (ec.timer < 0) {
                getWorld().getSystem(PlayerSystem.class).healthComp.dealDamage(getWorld().getSystem(PlayerSystem.class).player, 4f);
                ec.timer = 2;
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
