package com.thefoxarmy.rainbowwarrior.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.thefoxarmy.rainbowwarrior.Globals;
import com.thefoxarmy.rainbowwarrior.screens.PlayScreen;

public class Player extends Sprite {
    public Body body;
    private World world;


    public Player(PlayScreen screen) {
        super(new Texture(Gdx.files.internal("badlogic.jpg")));
        this.world = screen.getWorld();
        def();
    }

    private void def() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(90 / Globals.PPM, 100 / Globals.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / Globals.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }

    public void tick(float delta) {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
    }
}
