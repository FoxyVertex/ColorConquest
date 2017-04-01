package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.input.PlayerInput;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.tools.Utilities;

/**
 * Created by aidan on 12/24/2016.
 */

public class Player extends SpriteBody {

    public int lives = 10;
    public PlayerInput input;
    public InputProcessor inputProcessor;
    public Vector2 spawnPoint;
    //Might replace with Vector
    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public int score = 0;
    public float fireSpeed = 0.1f;
    //Physical Properties
    public float maxJumpForce = 300;
    public float minJumpForce = 55;
    public float jumpForce = minJumpForce;
    public float maxRunSpeed = 0.4f;
    public float minRunSpeed = 0.2f;
    public float runSpeed = minRunSpeed;
    public boolean isFiring = false;
    public Array<Color> colors;
    //FixtureDef fdef;
    //Properties
    boolean hasGun;
    private float timer;
    private State currentState;
    private State previousState;
    public boolean runningRight = false;
    private Array<Bullet> bullets;
    public Color selectedColor = Color.RED;

    public Player(Vector2 spawnPoint) {
        super(spawnPoint.scl(1 / Finals.PPM));
        setRegion(Assets.mainAtlas.findRegion("idle"));


        this.spawnPoint = spawnPoint;
        def();

        setBounds(0, 0, getRegionWidth() / 8.5f / Finals.PPM, getRegionHeight() / 8.5f / Finals.PPM);

        currentState = State.IDLE;

        bullets = new Array<Bullet>();
        colors = new Array<Color>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        input = new PlayerInput();

    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    /**
     * Defines all fixtures to create the body of the player.
     */
    private void def() {
        super.CATIGORY_BIT = Finals.PLAYER_BIT;
        super.bodyType = BodyDef.BodyType.DynamicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(13.4f / Finals.PPM);

        super.def(shape);

        body.setLinearDamping(5f);
        body.setUserData(this);
    }

    @Override
    public void tick(float delta) {
        //Gdx.input.setInputProcessor(input);
        if (isFiring) {
            minRunSpeed = 0.1f;
            maxRunSpeed = 0.3f;
        } else {
            minRunSpeed = 0.2f;
            maxRunSpeed = 0.4f;
        }
        input.handleInput(delta);
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRotation((float) Math.toDegrees(body.getAngle()));
        timer += delta;
        setRegion(getFrame(delta));
        for (Bullet b : bullets) {
            b.tick(delta);
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet b : bullets) {
            b.draw(batch);
        }
    }


    /**
     * Determent the cornet state in which to the player is, based on the player's current state of motion.
     *
     * @return the current motion state of the player.
     */
    private State getMotionAnimationState() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
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
        if (!isFiring)
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

    public void shoot(Vector2 clickPoint) {

        if (selectedColor == Color.RED)
            if (red <= 0)
                return;
            else
                red--;
        else if (selectedColor == Color.GREEN)
            if (green <= 0)
                return;
            else
                green--;
        else if (blue <= 0)
            return;
        else
            blue--;


        Globals.hudScene.updateData();

        float bulletStartXValue;

        if (!runningRight) {
            bulletStartXValue = -1/Finals.PPM;
        } else {
            bulletStartXValue = 2/Finals.PPM;
        }

        float m = 1f * (float) Math.sqrt(20); // Direct velocity
        Vector2 cp = new Vector2();
        Vector2 pp = new Vector2(body.getPosition()).add(bulletStartXValue, 1f);

        cp.x = Globals.gameMan.cam.position.x + Utilities.map(clickPoint.x, 0, Gdx.graphics.getWidth(), 0, Globals.gameMan.viewport.getWorldWidth());
        cp.y = Globals.gameMan.cam.position.y + Utilities.map(clickPoint.y, 0, Gdx.graphics.getHeight(), Globals.gameMan.viewport.getWorldHeight(), 0);

        double theta = Math.atan((cp.y-pp.y)/(cp.x-pp.x));
        double alpha = m * Math.sin(theta);
        double beta  = m * Math.cos(theta);
        if ((cp.x-pp.x) < 0 && !(beta < 0)) {
            beta *= -1;
            alpha *= -1;
        }

        Vector2 clickPointBasedImpulse = new Vector2((float) beta, (float) alpha);
        if (isFiring) {
            if (runningRight)
                bullets.add(new Bullet(body.getPosition().add(new Vector2(bulletStartXValue, 3/Finals.PPM)), selectedColor, clickPointBasedImpulse));
            else
                bullets.add(new Bullet(body.getPosition().add(new Vector2(bulletStartXValue, 3/Finals.PPM)), selectedColor, clickPointBasedImpulse));
        } else {
            if (runningRight)
                bullets.add(new Bullet(body.getPosition().add(new Vector2(bulletStartXValue, 3/Finals.PPM)), selectedColor));
            else
                bullets.add(new Bullet(body.getPosition().add(new Vector2(bulletStartXValue, 3/Finals.PPM)), selectedColor));
        }

    }

    public boolean isRunningRight() {
        return runningRight;
    }

    public void dispose() {
        super.dispose();
        for (Bullet b : bullets)
            b.dispose();
        getTexture().dispose();
    }

    /**
     * A bunch of motion states the player could be in
     */
    private enum State {
        IDLE, JUMP_START, JUMP_LOOP, WALKING, FALLING
    }

}