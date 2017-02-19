package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.physics.box2d.Filter;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.exception.NoCategoryBitFoundOnPhysicsEntityException;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/16/2017.
 */

public class SetupCategoryBitsSystem extends EntitySystem implements AfterSceneInit {
    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public SetupCategoryBitsSystem() {
        super(Aspect.all(PhysicsBody.class));
    }

    @Override
    protected void processSystem() {
        for (Entity entity : getEntities()) {
            try {
                createCategoryBit(entity);
            } catch (NoCategoryBitFoundOnPhysicsEntityException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterSceneInit() {

    }

    private void createCategoryBit(Entity entity) throws NoCategoryBitFoundOnPhysicsEntityException {
        if (entity.getComponent(Variables.class) == null) {
            throw new NoCategoryBitFoundOnPhysicsEntityException(entity);
        }
        if (entity.getComponent(Variables.class).get("collisionCat") == null) {
            throw new NoCategoryBitFoundOnPhysicsEntityException(entity);
        }
        Filter filter = new Filter();
        String collisionCat = entity.getComponent(Variables.class).get("collisionCat");
        if (collisionCat == "player") {
            filter.categoryBits = Finals.PLAYER_BIT;
        } else if (collisionCat == "environment") {
            filter.categoryBits = Finals.BLOCK_BIT;
        } else if (collisionCat == "endLevel") {
            filter.categoryBits = Finals.END_LEVEL_BIT;
        } else if (collisionCat == "playerFeet") {
            filter.categoryBits = Finals.PLAYER_FEET_BIT;
        } else if (collisionCat == "bullet") {
            filter.categoryBits = Finals.BULLET_BIT;
        }
        entity.getComponent(PhysicsBody.class).body.getFixtureList().get(0).setFilterData(filter);
    }
}
