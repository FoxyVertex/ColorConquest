package com.thefoxarmy.rainbowwarrior.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.thefoxarmy.rainbowwarrior.FinalGlobals;
import com.thefoxarmy.rainbowwarrior.managers.Assets;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;
import com.thefoxarmy.rainbowwarrior.tools.PlayerInputAdapter;

import jdk.nashorn.internal.runtime.regexp.joni.ast.BackRefNode;

/**
 * This class handles all of the input, animation, and physics for the local player.
 */

public class Player extends Sprite {
    public Body body;
    public PlayerInputAdapter input;
    public Vector2 spawnPoint;
    //FixtureDef fdef;
    private World world;
    private float timer;
    private State currentState;
    private State previousState;
    private boolean runningRight = false;
    public float jumpForce = 55;
    public float maxJumpForce = 100;
    public float minJumpFox = 55;

    public float runSpeed = 0.125f;
    public float maxRunSpeed = 0.2f;
    public float minRunSpeed = 0.125f;
    /**
     * Sets up animation, input, and physics for the player.
     *
     * @param screen     Used by the player to access things beyond the player class such as the world
     * @param input      Used for having multiple local users
     * @param spawnPoint Used to predefine the players spawn location for each map.
     */

    public Player(GameScreen screen, PlayerInputAdapter input, Vector2 spawnPoint) {
        super(Assets.mainAtlas.findRegion("idle"));

        this.spawnPoint = spawnPoint;
        this.world = screen.getWorld();
        def();

        setBounds(0, 0, getRegionWidth() / 8.5f / FinalGlobals.PPM, getRegionHeight() / 8.5f / FinalGlobals.PPM);

        currentState = State.IDLE;

        this.input = new PlayerInputAdapter();
        Gdx.input.setInputProcessor(input);
    }

    /**
     * Defines all fixtures to create the body of the player.
     */
    private void def() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(spawnPoint.scl(1 / FinalGlobals.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;

        body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(13 / FinalGlobals.PPM);
        fdef.shape = shape;
        fdef.filter.categoryBits = FinalGlobals.PLAYER_BIT;
        final Fixture fixture = body.createFixture(fdef);
        body.setLinearDamping(5f);
        body.setUserData(this);
    }

    /**
     * Links the player's sprite's regions to the body, and handles physics caluclations.
     *
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
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     */
    public void jump(float delta) {
        body.applyLinearImpulse(new Vector2(0, 7), body.getWorldCenter(), true);
    }

    /**
     * Determent the cornet state in which to the player is, based on the player's current state of motion.
     *
     * @return the current motion state of the player.
     */
    private State getMotionAnimationState() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            return State.JUMP_START;
        if (body.getLinearVelocity().y > 0 || body.getLinearVelocity().y < 0 && previousState == State.JUMP_LOOP) {
            return State.JUMP_LOOP;
        } else if (body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (body.getLinearVelocity().x != 0 && previousState != State.JUMP_LOOP) {
            return State.WALKING;
        } else {
            return State.IDLE;
        }

    }

    /**
     * Gets the current frames of the current animation and modifies the animation timer
     *
     * @param delta a float that is the amount of time in seconds since the last frame
     * @return a TextureRegion to set the player's animation
     */
    private TextureRegion getFrame(float delta) {
        currentState = getMotionAnimationState();
        TextureRegion region;

        switch (currentState) {
            case JUMP_START:
                region = Assets.playerJumpStartAnimation.getKeyFrame(timer, false);
                break;
            case JUMP_LOOP:
                region = Assets.playerJumpLoopAnimation.getKeyFrame(timer, true);
                break;
            case WALKING:
                region = Assets.playerWalkAnim.getKeyFrame(timer, true);
                break;
            case FALLING:
                region = Assets.playerFallAnim.getKeyFrame(timer, true);
                break;
            default:
                region = Assets.playerIdleAnim.getKeyFrame(timer, true);
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
        timer = currentState == previousState ? timer + delta : 0;
        //update previous state
        previousState = currentState;

        return region;
    }

    /**
     * A bunch of motion states the player could be in
     */
    private enum State {
        IDLE, JUMP_START, JUMP_LOOP, WALKING, FALLING
    }

}