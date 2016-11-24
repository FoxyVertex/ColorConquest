package com.thefoxarmy.rainbowwarrior.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.thefoxarmy.rainbowwarrior.Globals;
import com.thefoxarmy.rainbowwarrior.screens.PlayScreen;
import com.thefoxarmy.rainbowwarrior.tools.PlayerInputAdapter;

public class Player extends Sprite {
    public Body body;
    FixtureDef fdef;
    private World world;
    private PlayerInputAdapter input;
    private float timer;
    private Animation idleAnim;
    private State currentAnimation;
    private State previousAnimation;
    private Animation walkAnim;
    private Animation jumpAnim;
    private Animation fallAnim;
    private boolean moving = false;
    private boolean facingLeft = false;

    public Player(PlayScreen screen, PlayerInputAdapter input) {
        super(screen.mainAtlas.findRegion("idle"));

        this.world = screen.getWorld();
        def();


        setBounds(0, 0, getRegionWidth() / 8.5f / Globals.PPM, getRegionHeight() / 8.5f / Globals.PPM);


        idleAnim = new Animation(1 / 16f, screen.mainAtlas.findRegions("idle"), Animation.PlayMode.LOOP);
        walkAnim = new Animation(1 / 16f, screen.mainAtlas.findRegions("walk"), Animation.PlayMode.LOOP);
        fallAnim = new Animation(1 / 16f, screen.mainAtlas.findRegions("fall"), Animation.PlayMode.LOOP);
        currentAnimation = State.IDLE;

        this.input = new PlayerInputAdapter(screen);
        Gdx.input.setInputProcessor(input);
    }

    private void def() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(100 / Globals.PPM, 500 / Globals.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / Globals.PPM);
        fdef.shape = shape;
        final Fixture fixture = body.createFixture(fdef);


        FixtureDef top = new FixtureDef();
        shape.setRadius(61 / Globals.PPM);
        shape.setPosition(new Vector2(0, 5).scl(Globals.PPM));
        top.shape = shape;
        body.createFixture(top);
    }

    public void tick(float delta) {
        input.handleInput(delta);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
        timer += delta;
        setRegion(getFrame(delta));
    }

    public void dispose() {
        getTexture().dispose();
    }

    public void jump(float delta) {
        body.applyLinearImpulse(new Vector2(0, 4), body.getWorldCenter(), true);
    }

    private State getMotionAnimationState() {

        if (body.getLinearVelocity().y > 0 || body.getLinearVelocity().y < 0 && previousAnimation == State.JUMP) {
            return State.JUMP;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0 && previousAnimation != State.JUMP) {
            return State.WALKING;
        } else {
            return State.IDLE;
        }

    }

    private TextureRegion getFrame(float delta) {
        currentAnimation = getMotionAnimationState();
        TextureRegion region;

        switch (currentAnimation) {
            case JUMP:
                //region = jumpAnim.getKeyFrame(timer);
                //break;
            case WALKING:
                region = walkAnim.getKeyFrame(timer, true);
                break;
            case FALLING:
                region = fallAnim.getKeyFrame(timer, true);
                break;
            default:
                region = idleAnim.getKeyFrame(timer, true);
        }

        if ((body.getLinearVelocity().x < 0) && !region.isFlipX()) {
            region.flip(true, false);
            moving = false;
        } else if ((body.getLinearVelocity().x > 0 || moving) && region.isFlipX()) {
            region.flip(true, false);
            moving = true;
        }
        timer = currentAnimation == previousAnimation ? timer + delta : 0;
        previousAnimation = currentAnimation;
        return region;
    }

    private enum State {
        IDLE, JUMP, WALKING, FALLING
    }

}