package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

/**
 * Created by seth on 3/28/2017.
 */

public class EntityController {
    public static Array<SpriteBody> entities = new Array<SpriteBody>();

    public static void tick(float delta) {
        for (SpriteBody sprite : entities) {
            sprite.tick(delta);
        }
    }

    public static void render(Batch batch) {
        for (SpriteBody sprite : entities) {

            sprite.draw(batch);
        }
    }

    public static void reset() {
        entities = new Array<SpriteBody>();
    }
}
