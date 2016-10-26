package com.thefoxarmy.rainbowwarrior.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.thefoxarmy.rainbowwarrior.Globals;
import com.thefoxarmy.rainbowwarrior.screens.PlayScreen;
import com.thefoxarmy.rainbowwarrior.tools.PlayerInputAdapter;

public class Player extends Sprite {
    public Body body;
    private PlayerInputAdapter inputAdapter;
    private World world;

    public Player(PlayScreen screen, PlayerInputAdapter input) {
        super(new TextureAtlas("test.pack").findRegion("badlogic"));

        this.world = screen.getWorld();
        def();
        TextureRegion badlogic = new TextureRegion(getTexture(), 515, 533, 256, 256);
        setBounds(0, 0, 16 / Globals.PPM, 16 / Globals.PPM);
        setRegion(badlogic);
        inputAdapter = new PlayerInputAdapter(screen);
        Gdx.input.setInputProcessor(inputAdapter);
    }

    private void def() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(100 / Globals.PPM, 100 / Globals.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(9 / Globals.PPM);
        fdef.shape = shape;
        body.createFixture(fdef);
    }

    public void tick(float delta) {
        inputAdapter.handleInput();
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
    }

    public void dispose() {
        getTexture().dispose();
    }
}