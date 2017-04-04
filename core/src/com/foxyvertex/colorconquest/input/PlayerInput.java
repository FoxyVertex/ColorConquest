package com.foxyvertex.colorconquest.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.managers.Levels;
import com.foxyvertex.colorconquest.tools.Drawable;
import com.foxyvertex.colorconquest.tools.Utilities;

/**
 * Created by aidan on 1/23/17.
 */

public class PlayerInput extends InputMultiplexer {

    public DesktopController desktopController;
    public MobileController  mobileController;
    public int   currentColorIndex = 0;
    public float speedMultiplier   = 1f;
    boolean jumpPressed, forwardPressed, backwardPressed, downPressed, debugSuperAbilityPressed, debugSpawnpointPressed, debugZoomInPressed, debugZoomOutPressed, debugNextLevelPressed, firingModePressed;
    boolean jumpPressedPrev, forwardPressedPrev, backwardPressedPrev, downPressedPrev, debugSuperAbilityPressedPrev, debugSpawnpointPressedPrev, debugZoomInPressedPrev, debugZoomOutPressedPrev, debugNextLevelPressedPrev, firingModePressedPrev;
    private float   currentJumpLength    = 0;
    private boolean canJump              = true;
    private int     iForRainbowEasterEgg = 0;
    private Vector3 rainBowCurrentColor  = new Vector3();

    private float fireTimer = 0;

    public PlayerInput() {
        super();
        Drawable firingModeDrawable;
        Gdx.input.setInputProcessor(this);
        if (Globals.isMobileApp) {
            mobileController = new MobileController(this);
            addProcessor(mobileController.stage);
            addProcessor(mobileController);
            Globals.gameMan.running.drawables.add(mobileController);
        } else {
            desktopController = new DesktopController(this);
            addProcessor(desktopController);
        }
        firingModeDrawable = new Drawable() {
            @Override
            public void draw() {
                if (Globals.gameMan.player.isFiring) {
                    float bulletStartXValue;

                    if (!Globals.gameMan.player.isRunningRight()) {
                        bulletStartXValue = -1 / Finals.PPM;
                    } else {
                        bulletStartXValue = 2 / Finals.PPM;
                    }

                    Vector2 firingModeStartPos = new Vector2(Globals.gameMan.player.body.getPosition()).add(bulletStartXValue, 3 / Finals.PPM);

                    float   m  = 0.1f * (float) Math.sqrt(20); // Direct velocity
                    Vector2 cp = new Vector2();
                    cp.x = Globals.gameMan.cam.position.x + Utilities.map(Gdx.input.getX(), 0, Gdx.graphics.getWidth(), 0, Globals.gameMan.viewport.getWorldWidth()) - (Globals.gameMan.viewport.getWorldWidth() / 2);
                    cp.y = Globals.gameMan.cam.position.y + Utilities.map(Gdx.input.getY(), 0, Gdx.graphics.getHeight(), Globals.gameMan.viewport.getWorldHeight(), 0) - (Globals.gameMan.viewport.getWorldHeight() / 2);
                    double theta = Math.atan((cp.y - firingModeStartPos.y) / (cp.x - firingModeStartPos.x));
                    double alpha = m * Math.sin(theta);
                    double beta  = m * Math.cos(theta);
                    if ((cp.x - firingModeStartPos.x) < 0 && !(beta < 0)) {
                        beta *= -1;
                        alpha *= -1;
                    }

                    Vector2 clickPointBasedImpulse = new Vector2((float) beta, (float) alpha);
                    clickPointBasedImpulse.add(firingModeStartPos);
                    if (clickPointBasedImpulse.dst(firingModeStartPos) < cp.dst(firingModeStartPos)) {
                        Utilities.DrawDebugLine(firingModeStartPos, cp, 2, new Color(Utilities.map(rainBowCurrentColor.x, 0, 255, 0, 1), Utilities.map(rainBowCurrentColor.y, 0, 255, 0, 1), Utilities.map(rainBowCurrentColor.z, 0, 255, 0, 1), 1), Globals.gameMan.cam.combined);
                        Utilities.DrawDebugLine(firingModeStartPos, clickPointBasedImpulse, 4, Globals.gameMan.player.selectedColor, Globals.gameMan.cam.combined);
                    } else {
                        Utilities.DrawDebugLine(firingModeStartPos, cp, 3, Globals.gameMan.player.selectedColor, Globals.gameMan.cam.combined);
                    }
                }
            }
        };
        Globals.gameMan.running.drawables.add(firingModeDrawable);
    }

    public void handleInput(float delta) {
        Gdx.input.setInputProcessor(this);
        if (Globals.isMobileApp && Gdx.input.getInputProcessor() != mobileController) {
            Gdx.input.setInputProcessor(mobileController.stage);
        } else if (Gdx.input.getInputProcessor() != desktopController) {
            Gdx.input.setInputProcessor(desktopController);
        }

        if (!Globals.isMobileApp) {
            desktopController.handleInput(delta);
        } else {
            mobileController.handleInput();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F11) && Finals.debugMode == Finals.DebugMode.PRODUCTION) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setWindowedMode(1920, 1080);
            } else {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            }
        }

        float maxJumpForceLength = 0.2f;

        //DEBUG JUNK
        if (Gdx.input.isKeyPressed(Input.Keys.B) && Finals.debugMode != Finals.DebugMode.PRODUCTION) {
            Globals.gameMan.player.blue += 1;
            Globals.hudScene.updateData();
        }
        if (debugZoomInPressed && Finals.debugMode != Finals.DebugMode.PRODUCTION)
            Globals.gameMan.cam.zoom += 3; /// Finals.PPM;
        if (debugZoomOutPressed && Finals.debugMode != Finals.DebugMode.PRODUCTION)
            Globals.gameMan.cam.zoom -= 3; /// Finals.PPM;
        if (debugSpawnpointPressed && Finals.debugMode != Finals.DebugMode.PRODUCTION) {
            Globals.gameMan.player.body.setLinearVelocity(new Vector2(0, 0));
            Globals.gameMan.player.body.setTransform(Globals.gameMan.player.spawnPoint, Globals.gameMan.player.body.getAngle());
        }
        if (debugNextLevelPressed && Finals.debugMode != Finals.DebugMode.PRODUCTION) {
            Levels.Level nextLevel = Globals.gameMan.currentLevel.nextLevel;
            if (nextLevel != null) Globals.gameMan.nextLevel(nextLevel);
            else {
                Assets.playMusic(Assets.menuMusic);
                Globals.game.setScreen(Globals.menuScreen);
            }
        }

        Globals.gameMan.player.isFiring = firingModePressed;
        if (Globals.gameMan.player.isFiring) {
            Globals.gameMan.player.minRunSpeed = 0.1f;
            Globals.gameMan.player.maxRunSpeed = 0.3f;
        } else {
            Globals.gameMan.player.minRunSpeed = 0.2f;
            Globals.gameMan.player.maxRunSpeed = 0.4f;
        }

        if (jumpPressed) {
            currentJumpLength += delta;

            if (currentJumpLength >= maxJumpForceLength) canJump = false;

            jumpPressedPrev = true;
        } else {
            currentJumpLength = 0f;
            canJump = Globals.gameMan.player.body.getLinearVelocity().y == 0 && !jumpPressedPrev;
            jumpPressedPrev = false;
        }

        if (debugSuperAbilityPressed && Finals.debugMode != Finals.DebugMode.PRODUCTION) {
            Globals.gameMan.player.runSpeed = Globals.gameMan.player.maxRunSpeed;
            Globals.gameMan.player.jumpForce = Globals.gameMan.player.maxJumpForce;
        }


        if (downPressed)
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(0, -10f), Globals.gameMan.player.body.getWorldCenter(), true);

        if (forwardPressed) { // && Globals.gameMan.player.body.getLinearVelocity().x <= 2 * speedMultiplier) {
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(Globals.gameMan.player.runSpeed * speedMultiplier, 0), Globals.gameMan.player.body.getWorldCenter(), true);
            forwardPressedPrev = true;
        } else if (!backwardPressed && forwardPressedPrev) {
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(-Globals.gameMan.player.runSpeed * speedMultiplier, 0), Globals.gameMan.player.body.getWorldCenter(), true);
            forwardPressedPrev = false;
        }

        if (backwardPressed) { // && Globals.gameMan.player.body.getLinearVelocity().x >= -2 * speedMultiplier) {
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(-Globals.gameMan.player.runSpeed * speedMultiplier, 0), Globals.gameMan.player.body.getWorldCenter(), true);
            backwardPressedPrev = true;
        } else if (!Gdx.input.isKeyPressed(Input.Keys.A) && backwardPressedPrev) {
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(-Globals.gameMan.player.runSpeed * speedMultiplier, 0), Globals.gameMan.player.body.getWorldCenter(), true);
            backwardPressedPrev = false;
        }


        if (!(currentJumpLength >= maxJumpForceLength) && currentJumpLength > 0 && canJump)
            Globals.gameMan.player.body.applyLinearImpulse(new Vector2(0, Globals.gameMan.player.jumpForce * delta), Globals.gameMan.player.body.getWorldCenter(), true);

        float frequency = 0.3f;

        rainBowCurrentColor.x = (float) Math.sin(frequency * iForRainbowEasterEgg + 0) * 127 + 128;
        rainBowCurrentColor.y = (float) Math.sin(frequency * iForRainbowEasterEgg + 2) * 127 + 128;
        rainBowCurrentColor.z = (float) Math.sin(frequency * iForRainbowEasterEgg + 4) * 127 + 128;
        ++iForRainbowEasterEgg;

        Globals.gameMan.player.setColor(new Color(Utilities.map(rainBowCurrentColor.x, 0, 255, 0, 1), Utilities.map(rainBowCurrentColor.y, 0, 255, 0, 1), Utilities.map(rainBowCurrentColor.z, 0, 255, 0, 1), 1));
        Globals.gameMan.player.setSelectedColor(Globals.gameMan.player.colors.get(currentColorIndex));

        fireTimer += delta;
        if (Globals.gameMan.player.canShoot)
        if (Gdx.input.isTouched() && fireTimer >= Globals.gameMan.player.fireSpeed) {
            Globals.gameMan.player.shoot(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            fireTimer = 0;
        }

    }

}
