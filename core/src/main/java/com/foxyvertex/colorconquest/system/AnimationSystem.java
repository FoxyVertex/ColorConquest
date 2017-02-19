package com.foxyvertex.colorconquest.system;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.component.Animation;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/15/2017.
 */

public class AnimationSystem extends BaseSystem implements AfterSceneInit {
    VisIDManager idManager;

    Array<Entity> animatables = new Array<Entity>();
    TextureRegion textureRegionTMP;
    Animation animationCompTMP;
    @Override
    protected void processSystem() {
        for (Entity entity : animatables) {
            entity.getComponent(Animation.class).timer += world.getDelta();
            if (entity.getComponent(Animation.class).timer >= entity.getComponent(Animation.class).animations.get(entity.getComponent(Animation.class).currentAnimation).getAnimationDuration() && entity.getComponent(Animation.class).onEnd != null && !entity.getComponent(Animation.class).onEnd.equals("")) {
                changeAnimState(entity, entity.getComponent(Animation.class).onEnd, true);
                entity.getComponent(Animation.class).timer = 0f;
                entity.getComponent(Animation.class).onEnd = null;
            } else if(entity.getComponent(Animation.class).timer >= entity.getComponent(Animation.class).animations.get(entity.getComponent(Animation.class).currentAnimation).getAnimationDuration()) {
                entity.getComponent(Animation.class).timer = 0f;
            }
            textureRegionTMP = entity.getComponent(Animation.class).animations.get(entity.getComponent(Animation.class).currentAnimation).getKeyFrame(entity.getComponent(Animation.class).timer, entity.getComponent(Animation.class).loop);
            //textureRegionTMP.flip(entity.getComponent(Animation.class).flipX, entity.getComponent(Animation.class).flipY);

            if (entity.getComponent(Animation.class).flipX != textureRegionTMP.isFlipX()) {
                textureRegionTMP.flip(true, false);
            }
            if (entity.getComponent(Animation.class).flipY != textureRegionTMP.isFlipY()) {
                textureRegionTMP.flip(false, true);
            }

            entity.getComponent(VisSprite.class).setRegion(textureRegionTMP);
        }
    }

    @Override
    public void afterSceneInit() {

    }

    public void addEntity(Entity entity) {
        if (entity.getComponent(Animation.class) != null) animatables.add(entity); else return;
        Animation animComp = entity.getComponent(Animation.class);
        animComp.textureAtlas = new TextureAtlas(animComp.path);
        for (String string : animComp.animationNames) {
            animComp.animations.put(string, new com.badlogic.gdx.graphics.g2d.Animation<>(animComp.animationFrameCounts.get(string), animComp.textureAtlas.findRegions(string), animComp.loop ? com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP : com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL));
        }
    }

    public void changeAnimState(Entity entity, String name, boolean flipX, boolean flipY, boolean loop) {
        Animation animComp = entity.getComponent(Animation.class);
        if (animComp.currentAnimation != name) {
            animComp.currentAnimation = name;
            animComp.timer = 0f;
            animComp.loop = loop;
        }
        animComp.flipX = flipX;
        animComp.flipY = flipY;
    }

    public void changeAnimState(Entity entity, String name, boolean loop) {
        changeAnimState(entity, name, false, false, loop);
    }

    public void changeAnimState(Entity entity, String name, String onEnd) {
        changeAnimState(entity, name, false, false, false);
        entity.getComponent(Animation.class).onEnd = onEnd;
    }
}
