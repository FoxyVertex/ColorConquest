package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.physics.box2d.Filter;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.component.Animation;
import com.foxyvertex.colorconquest.component.Health;
import com.foxyvertex.colorconquest.component.ToDestroy;
import com.foxyvertex.colorconquest.component.Zombie;
import com.foxyvertex.colorconquest.exception.NoCategoryBitFoundOnPhysicsEntityException;
import com.foxyvertex.colorconquest.tools.DeathRunnable;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/16/2017.
 */

public class SetupEntityComponentsSystem extends EntitySystem implements AfterSceneInit {
    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public SetupEntityComponentsSystem() {
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
        if (collisionCat.equals("player")) {
            filter.categoryBits = Finals.PLAYER_BIT;
        } else if (collisionCat.equals("environment")) {
            filter.categoryBits = Finals.BLOCK_BIT;
        } else if (collisionCat.equals("endLevel")) {
            filter.categoryBits = Finals.END_LEVEL_BIT;
        } else if (collisionCat.equals("playerFeet")) {
            filter.categoryBits = Finals.PLAYER_FEET_BIT;
        } else if (collisionCat.equals("bullet")) {
            filter.categoryBits = Finals.BULLET_BIT;
        } else if (collisionCat.equals("zombie")) {
            filter.categoryBits = Finals.ZOMBIE_BIT;
            if (entity.getComponent(Zombie.class) == null) entity.edit().add(new Zombie());
            if (entity.getComponent(Health.class) == null) entity.edit().add(new Health(new DeathRunnable() {
                @Override
                public void run(Entity e) {
                    e.edit().add(new ToDestroy(1000));
                }
            }, 20f));
            if (entity.getComponent(Animation.class) == null) {
                Animation newAnimComp = new Animation();
                newAnimComp.path = "gfx/zombie.atlas";
                newAnimComp.animationType = Animation.AnimType.ATLAS;
                newAnimComp.loop = true;
                newAnimComp.animationNames.add("idle");
                newAnimComp.animationFrameCounts.put("idle", 1f / 2f);
                newAnimComp.animationNames.add("hit");
                newAnimComp.animationFrameCounts.put("hit", 1f / 2f);
                newAnimComp.currentAnimation = "idle";
                entity.edit().add(newAnimComp);
                getWorld().getSystem(AnimationSystem.class).addEntity(entity);
            }
        }
        entity.getComponent(PhysicsBody.class).body.getFixtureList().get(0).setFilterData(filter);
    }
}
