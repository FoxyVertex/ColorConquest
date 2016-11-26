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

/**
 * The Player class represents the local player.
 */

public class Player extends Sprite {
    public Body body;
    //FixtureDef fdef;
    private World world;
    private PlayerInputAdapter input;

    private float timer;
    private Animation idleAnim;
    private State currentAnimation;
    private State previousAnimation;
    private Animation walkAnim;
    private Animation jumpAnim;
    private Animation fallAnim;

    private boolean runningRight = false;
    public Vector2 spawnPoint;

    /**
     * Sets up animation, input, and physics for the player.
     * @param screen Used by the player to access things beyond the player class such as the world
     * @param input Used for having multiple local users
     * @param spawnPoint Used to spawn the player and for the `teleport to spawn` debug key
     */

    public Player(PlayScreen screen, PlayerInputAdapter input, Vector2 spawnPoint) {
        super(screen.mainAtlas.findRegion("idle"));

        this.spawnPoint = spawnPoint;
        this.world = screen.getWorld();
        def();


        setBounds(0, 0, getRegionWidth() / 8.5f / Globals.PPM, getRegionHeight() / 8.5f / Globals.PPM);


        idleAnim = new Animation(1 / 16f, screen.mainAtlas.findRegions("idle"), Animation.PlayMode.LOOP);
        walkAnim = new Animation(1 / 16f, screen.mainAtlas.findRegions("walk"), Animation.PlayMode.LOOP);
        fallAnim = new Animation(1 / 16f, screen.mainAtlas.findRegions("fall"), Animation.PlayMode.LOOP);
        currentAnimation = State.IDLE;

        this.input = new PlayerInputAdapter(this);
        Gdx.input.setInputProcessor(input);
    }

    /**
     * Defines all fixtures to create the body of the player.
     */
    private void def() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnPoint.scl(1 / Globals.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / Globals.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = Globals.PLAYER_BIT;
        final Fixture fixture = body.createFixture(fdef);
        body.setLinearDamping(5f);
    }

    /**
     * Calculates all physics stuff for the player.
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public void tick(float delta) {
        input.handleInput(delta);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
        timer += delta;
        setRegion(getFrame(delta));
    }

    /**
     * Discards the player's texture from memory.
     */
    public void dispose() {
        getTexture().dispose();
    }

    /**
     * Makes the player jump when called
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public void jump(float delta) {
        body.applyLinearImpulse(new Vector2(0, 7), body.getWorldCenter(), true);
    }

    /**
     * Calculates and returns the current active player animation.
     * @return the state in which the player's animation is.
     */
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

    /**
     * Gets the current frames of the current animation and modifies the animation timer
     * @param delta a float that is the amount of time in seconds since the last frame
     * @return a TextureRegion to set the player's animation
     */
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

        //Flip the character to the direction they're funning
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer.
        //otherwise the state has changed and we need to reset timer.
        timer = currentAnimation == previousAnimation ? timer + delta : 0;
        //update previous state
        previousAnimation = currentAnimation;

        return region;
    }

    /**
     * A bunch of motion states the player could be in
     */
    private enum State {
        IDLE, JUMP, WALKING, FALLING
    }

}