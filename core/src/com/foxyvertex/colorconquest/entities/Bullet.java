package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.foxyvertex.colorconquest.tools.PlayerInputAdapter;

/**
 * Created by seth on 1/16/2017.
 */

public class Bullet extends Entity {

    Bullet(Vector2 position, int radius, Color color) {
        Pixmap texture = new Pixmap(radius, radius, Pixmap.Format.RGB888);
        texture.setColor(color);
        texture.fillCircle(0,0,radius);

        set(new Sprite(new Texture(texture), (int) position.x, (int) position.y, new Texture(texture).getWidth(), new Texture(texture).getHeight()));
    }

    public void def() {

    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void die(Entity killedBy) {

    }

    @Override
    public void damage(Entity damagedBy, float damageAmount) {

    }

    @Override
    public void dispose() {

    }
}
