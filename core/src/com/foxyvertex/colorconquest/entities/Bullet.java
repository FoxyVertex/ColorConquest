package com.foxyvertex.colorconquest.entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;

/**
 * Created by seth on 1/16/2017.
 */

public class Bullet extends SpriteBody {

    private Vector2 initialImpulse = new Vector2(4, 2);

    Bullet(Vector2 spawnPoint, Color color) {
        super(spawnPoint);
        def();
        Pixmap tex = new Pixmap((int) (bodySize().x * Finals.PPM), (int) (bodySize().y * Finals.PPM), Pixmap.Format.RGBA8888);
        tex.setColor(color);
        tex.fillCircle(tex.getWidth() / 2, tex.getHeight() / 2, (int) (bodySize().x * Finals.PPM) / 2);

        setRegion(new Texture(tex));

    }

    private void def() {
        super.CATIGORY_BIT = Finals.BULLET_BIT;
        CircleShape shape = new CircleShape();
        shape.setRadius(3f / Finals.PPM);
        super.def(shape);
        if (Globals.gameScreen.player.isRunningRight())
            body.applyLinearImpulse(initialImpulse, body.getWorldCenter(), true);
        else
            body.applyLinearImpulse(initialImpulse.scl(-1, 1), body.getWorldCenter(), true);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
    }

}