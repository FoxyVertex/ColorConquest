package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
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

public class Player extends Interactant {

    public int lives = 10;
    public PlayerInput input;
    public Vector2     spawnPoint;
    //Might replace with Vector
    public int     red       = 255;
    public int     green     = 255;
    public int     blue      = 255;
    public int     score     = 0;
    public float   fireSpeed = 0.1f;
    public boolean isFiring  = false;
    public Array<Color> colors;
    //FixtureDef fdef;
    //Properties
    boolean hasGun;
    private float timer;
    private State currentState;
    private State previousState;
    private boolean runningRight  = false;
    public Color   selectedColor = Color.RED;
    public Array<Bullet> bullets = new Array<Bullet>();
    public boolean reduceAmmo = false;
    public float maxAmmoReducedPerSecond = 30;
    public float minammoReducedPerSecond = 10;
    public float ammoReducedPerSecond = minammoReducedPerSecond;
    public boolean canShoot = true;
    public float ammoTimer = 0;
    public int maxHealth = 100;

    public Player(Vector2 spawnPoint) {
        super(spawnPoint.scl(1 / Finals.PPM));
        setRegion(Assets.mainAtlas.findRegion("idle"));


        this.spawnPoint = spawnPoint;
        def();

        setBounds(0, 0, getRegionWidth() / 8.5f / Finals.PPM, getRegionHeight() / 8.5f / Finals.PPM);

        currentState = State.IDLE;

        colors = new Array<Color>();
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        input = new PlayerInput();
        bullets = new Array<Bullet>();
    }

    @Override
    public void reInitVars() {

    }

    @Override
    protected short CATIGORY_BIT() {
        return Finals.PLAYER_BIT;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    /**
     * Defines all fixtures to create the body of the player.
     */
    private void def() {

//        PolygonShape shape = new PolygonShape();
//        Vector2[] verts = {
//                new Vector2(0, 0).scl(1 / Finals.PPM),
//                new Vector2(20, 0).scl(1 / Finals.PPM),
//                new Vector2(0, 30).scl(1 / Finals.PPM),
//                new Vector2(20, 30).scl(1 / Finals.PPM)
//        };
        CircleShape shape = new CircleShape();
        shape.setRadius(13.4f / Finals.PPM);
        //shape.set(verts);

        super.def(shape);
        primaryFixture.setDensity(1f);
        primaryFixture.setFriction(1f);
        body.setLinearDamping(5f);
    }

    @Override
    public void tick(float delta) {
        super.tick(delta);
        //Gdx.input.setInputProcessor(input);
        if (isFiring) {
            //slow the player down
            //runSpeed *= 0.75;
        }
        if(reduceAmmo) {
            ammoTimer += delta;
            if(ammoTimer >= 1) {
                red -= ammoReducedPerSecond;
                green -= ammoReducedPerSecond;
                blue -= ammoReducedPerSecond;
                Globals.hudScene.updateData();
                ammoTimer = 0;
            }
        }

        input.handleInput(delta);
        timer += delta;
        setRegion(getFrame(delta));
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


        float bulletStartXValue = (!runningRight) ? -1/Finals.PPM : 2/Finals.PPM;

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
                bullets.add(new Bullet(body.getPosition().add(new Vector2(bulletStartXValue, 3/Finals.PPM)), selectedColor, new Vector2(4,2)));
            else
                bullets.add(new Bullet(body.getPosition().add(new Vector2(bulletStartXValue, 3/Finals.PPM)), selectedColor, new Vector2(4,2)));
        }
    }

    public boolean isRunningRight() {
        return runningRight;
    }

    public void dispose() {
        super.dispose();
        getTexture().dispose();
    }

    @Override
    public void attacked(SpriteBody attacker) {
        health -= ((Interactant) attacker).damangeDealtOnContact;
    }

    @Override
    public void die() {
        Globals.gameMan.playLevel( Globals.gameMan.currentLevel );
    }

    /**
     * A bunch of motion states the player could be in
     */
    private enum State {
        IDLE, JUMP_START, JUMP_LOOP, WALKING, FALLING
    }

}