package com.foxyvertex.colorconquest.entities;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;

/**
 * Created by seth on 1/16/2017.
 */

public class Bullet extends SpriteBody {

    public Vector2 spawnPoint;
    public float destructionTime = 2f;
    Pixmap tex;
    private Vector2 initialImpulse = new Vector2(4, 2);
    private float initialRadius = 2f;
    public float radius = initialRadius;
    private Color color;

    Bullet(Vector2 spawnPoint, Color color) {
        super(spawnPoint);
        this.spawnPoint = spawnPoint;
        this.color = color;
        def();
        reDraw();
        super.destructionTime = this.destructionTime;
    }

    public void def() {
        super.CATIGORY_BIT = Finals.BULLET_BIT;
        super.bodyType = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / Finals.PPM);
        super.def(shape);

        primaryFixture.setUserData(this);

        if (Globals.gameMan.player.isRunningRight())
            body.applyLinearImpulse(initialImpulse, body.getWorldCenter(), true);
        else
            body.applyLinearImpulse(initialImpulse.scl(-1, 1), body.getWorldCenter(), true);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);

    }

    public void reDraw() {
        tex = new Pixmap((int) (bodySize().x * Finals.PPM), (int) (bodySize().y * Finals.PPM), Pixmap.Format.RGBA8888);
        tex.setColor(color);
        tex.fillCircle(tex.getWidth() / 2, tex.getHeight() / 2, (int) (bodySize().x * Finals.PPM) / 2);

        setRegion(new Texture(tex));
    }

    public void dispose() {
        this.dispose();
        tex.dispose();
    }
}