package com.foxyvertex.colorconquest.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.component.Animation;
import com.kotcrab.vis.runtime.component.VisSprite;

/**
 * Created by aidan on 2/15/2017.
 */

/**
 * The AnimationSystem manages all of the textureAtlas-based animation in the game
 */
public class AnimationSystem extends BaseSystem {
    Array<Entity> animatables = new Array<Entity>();
    TextureRegion textureRegionTMP;

    /**
     * processSystem is called every frame at a max of 60 times per second. It handles changing the
     * textureRegion of an animated object every frame.
     */
    @Override
    protected void processSystem() {
        for (Entity entity : animatables) {
            // If the entity has been deleted, or it's Animation component has been removed, remove it from the list of animatable entities
            if (entity == null) {
                animatables.removeValue(entity, true);
                continue;
            } else if (entity.getComponent(Animation.class) == null) {
                animatables.removeValue(entity, true);
                continue;
            }

            // Update the animation timer
            entity.getComponent(Animation.class).timer += world.getDelta();

            // If the current animation is over and there is one that is meant to played after this one ends, play it
            if (entity.getComponent(Animation.class).timer >= entity.getComponent(Animation.class).animations.get(entity.getComponent(Animation.class).currentAnimation).getAnimationDuration() && entity.getComponent(Animation.class).onEnd != null && !entity.getComponent(Animation.class).onEnd.equals("")) {
                changeAnimState(entity, entity.getComponent(Animation.class).onEnd, true);
                entity.getComponent(Animation.class).timer = 0f;
                entity.getComponent(Animation.class).onEnd = null;
            }
            // Otherwise, loop the current one
            else if(entity.getComponent(Animation.class).timer >= entity.getComponent(Animation.class).animations.get(entity.getComponent(Animation.class).currentAnimation).getAnimationDuration()) {
                entity.getComponent(Animation.class).timer = 0f;
            }

            // Figure out which texture region to use
            textureRegionTMP = entity.getComponent(Animation.class).animations.get(entity.getComponent(Animation.class).currentAnimation).getKeyFrame(entity.getComponent(Animation.class).timer, entity.getComponent(Animation.class).loop);

            // Flip the texture region if needed
            if (entity.getComponent(Animation.class).flipX != textureRegionTMP.isFlipX()) {
                textureRegionTMP.flip(true, false);
            }
            if (entity.getComponent(Animation.class).flipY != textureRegionTMP.isFlipY()) {
                textureRegionTMP.flip(false, true);
            }

            // Set the sprite's texture region
            entity.getComponent(VisSprite.class).setRegion(textureRegionTMP);
        }
    }

    /**
     * Called to add an entity to the list of animateable entities
     * @param entity
     */
    public void addEntity(Entity entity) {
        if (entity.getComponent(Animation.class) != null) animatables.add(entity); else return;
        Animation animComp = entity.getComponent(Animation.class);
        animComp.textureAtlas = new TextureAtlas(animComp.path);
        for (String string : animComp.animationNames) {
            animComp.animations.put(string, new com.badlogic.gdx.graphics.g2d.Animation<>(animComp.animationFrameCounts.get(string), animComp.textureAtlas.findRegions(string), animComp.loop ? com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP : com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL));
        }
    }

    /**
     * Called to switch animations on an entity
     * @param entity the entity for changing
     * @param name the name of the desired animation
     * @param flipX whether to flip the regions on the x axis
     * @param flipY whether to flip the regions on the y axis
     * @param loop whether to loop the animation
     */
    public void changeAnimState(Entity entity, String name, boolean flipX, boolean flipY, boolean loop) {
        Animation animComp = entity.getComponent(Animation.class);
        if (!animComp.currentAnimation.equals(name)) {
            animComp.currentAnimation = name;
            animComp.timer = 0f;
            animComp.loop = loop;
        }
        animComp.flipX = flipX;
        animComp.flipY = flipY;
    }

    /**
     * Called to switch animations on an entity
     * @param entity the entity for changing
     * @param name the name of the desired animation
     * @param loop whether to loop the animation
     */
    public void changeAnimState(Entity entity, String name, boolean loop) {
        changeAnimState(entity, name, false, false, loop);
    }

    /**
     * Called to switch animations on an entity
     * @param entity the entity for changing
     * @param name the name of the desired animation
     * @param onEnd the animation to start after the current one ends
     */
    public void changeAnimState(Entity entity, String name, String onEnd) {
        changeAnimState(entity, name, false, false, false);
        entity.getComponent(Animation.class).onEnd = onEnd;
    }
}