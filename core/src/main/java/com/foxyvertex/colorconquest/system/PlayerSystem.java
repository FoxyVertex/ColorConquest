package com.foxyvertex.colorconquest.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.component.Animation;
import com.foxyvertex.colorconquest.component.Player;
import com.foxyvertex.colorconquest.input.DesktopController;
import com.foxyvertex.colorconquest.input.MobileController;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/12/2017.
 */

public class PlayerSystem extends BaseSystem implements AfterSceneInit {
    VisIDManager idManager;
    ComponentMapper<VisSprite> spriteCm;
    ComponentMapper<Transform> transformCm;
    ComponentMapper<PhysicsBody> bodyCm;

    //GameManager gameManager;
    CameraManager cameraManager;

    public int currentColorIndex = 0;
    public float speedMultiplier = 1f;
    public boolean jumpPressed, forwardPressed, backwardPressed, downPressed, debugSuperAbilityPressed, debugSpawnpointPressed, debugZoomInPressed, debugZoomOutPressed, debugNextLevelPressed;
    public boolean jumpPressedPrev, forwardPressedPrev, backwardPressedPrev, downPressedPrev, debugSuperAbilityPressedPrev, debugSpawnpointPressedPrev, debugZoomInPressedPrev, debugZoomOutPressedPrev, debugNextLevelPressedPrev;
    private float currentJumpLength = 0;
    private boolean canJump = true;
    boolean inAir = false;
    boolean jumpReleased = false;

    boolean animationStarted = false;

    float forceScale = 0.001f;

    Entity player;
    public Player playerComp;
    VisSprite sprite;
    Transform transform;
    Body body;

    enum FacingDIRECTION {UP, DOWN, LEFT, RIGHT}
    FacingDIRECTION facingDIRECTION = FacingDIRECTION.RIGHT;

    boolean shouldFlipX = false;
    boolean shouldFlipY = false;

    MobileController mobileController;
    DesktopController desktopController;


    @Override
    protected void processSystem() {
        if (Globals.isMobile) {
            mobileController.handleInput();
        } else {
            desktopController.handleInput(getWorld().getDelta());
        }

        if (!animationStarted) {
            world.getSystem(AnimationSystem.class).addEntity(player);
            animationStarted = true;
        }
        
        float maxJumpForceLength = 0.2f;
        
        //DEBUG JUNK
        if (Gdx.input.isKeyPressed(Input.Keys.B)) {
            player.getComponent(Player.class).blue += 1;
        }
        if (debugZoomInPressed)
            cameraManager.getCamera().zoom += 1 * getWorld().getDelta();
        if (debugZoomOutPressed)
            cameraManager.getCamera().zoom -= 1 * getWorld().getDelta();

            player.getComponent(Player.class).isFiring = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);


        inAir = !(body.getLinearVelocity().y <= 0.0001 || body.getLinearVelocity().y >= -0.0001);
        if (jumpPressed && (!jumpReleased || !inAir)) {
            currentJumpLength += world.getDelta();

            if (currentJumpLength >= maxJumpForceLength) canJump = false;
            if (!inAir && currentJumpLength < maxJumpForceLength) canJump = true;

            jumpPressedPrev = true;
        } else {
            if (jumpPressedPrev) jumpReleased = true;

            currentJumpLength = 0f;
            canJump = (body.getLinearVelocity().y <= 0.0001 || body.getLinearVelocity().y >= -0.0001) && !jumpPressedPrev;
            jumpPressedPrev = false;
        }

        if (downPressed)
            body.applyLinearImpulse(new Vector2(0, -10f), body.getWorldCenter(), true);

        if (forwardPressed && body.getLinearVelocity().x <= 2 * speedMultiplier) {
            body.applyLinearImpulse(new Vector2(player.getComponent(Player.class).runSpeed * speedMultiplier * forceScale, 0), body.getWorldCenter(), true);
            forwardPressedPrev = true;
            world.getSystem(AnimationSystem.class).changeAnimState(player, "walk", false, false, true);
            facingDIRECTION = FacingDIRECTION.RIGHT;
        } else if (!backwardPressed && forwardPressedPrev) {
            body.applyLinearImpulse(new Vector2(-player.getComponent(Player.class).runSpeed * speedMultiplier * forceScale, 0), body.getWorldCenter(), true);
            forwardPressedPrev = false;
        }

        if (backwardPressed && body.getLinearVelocity().x >= -2 * speedMultiplier) {
            body.applyLinearImpulse(new Vector2(-player.getComponent(Player.class).runSpeed * speedMultiplier * forceScale, 0), body.getWorldCenter(), true);
            backwardPressedPrev = true;
            world.getSystem(AnimationSystem.class).changeAnimState(player, "walk", true, false, true);
            facingDIRECTION = FacingDIRECTION.LEFT;
        } else if (!backwardPressed && backwardPressedPrev) {
            body.applyLinearImpulse(new Vector2(-player.getComponent(Player.class).runSpeed * speedMultiplier * forceScale, 0), body.getWorldCenter(), true);
            backwardPressedPrev = false;
        }

        if (!(backwardPressed || forwardPressed || downPressed || jumpPressed)) {

            switch (facingDIRECTION) {
                case RIGHT:
                    shouldFlipX = false;
                    break;
                case LEFT:
                    shouldFlipX = true;
                    break;
                case UP:
                    shouldFlipY = true;
                    break;
                case DOWN:
                    shouldFlipY = false;
                    break;
            }
            world.getSystem(AnimationSystem.class).changeAnimState(player, "idle", shouldFlipX, shouldFlipY, true);
            shouldFlipX = false;
            shouldFlipY = false;
        }

        if (currentJumpLength > 0 && canJump) {
            body.applyLinearImpulse(new Vector2(0, player.getComponent(Player.class).jumpForce * world.getDelta()),  body.getWorldCenter(), true);
            inAir = true;
            world.getSystem(AnimationSystem.class).changeAnimState(player, "jumploop", true, false, false);
        }
    }

    @Override
    public void afterSceneInit() {
        player = idManager.get("player");
        player.edit().add(new Player());
        playerComp = player.getComponent(Player.class);

        Animation animComp = new Animation();
        animComp.path = "gfx/GreyGuy.pack";
        animComp.currentAnimation = "idle";
        animComp.animationType = Animation.AnimType.ATLAS;
        animComp.loop = true;
        animComp.animationNames.add("idle");
        animComp.animationFrameCounts.put("idle", 1f / 12f);
        animComp.animationNames.add("walk");
        animComp.animationFrameCounts.put("walk", 1f / 15f);
        animComp.animationNames.add("fall");
        animComp.animationFrameCounts.put("fall", 1f / 4f);
        animComp.animationNames.add("jumpstart");
        animComp.animationFrameCounts.put("jumpstart", 1f / 4f);
        animComp.animationNames.add("jumploop");
        animComp.animationFrameCounts.put("jumploop", 1f / 8f);
        player.edit().add(animComp);

        sprite = spriteCm.get(player);
        transform = transformCm.get(player);
        body = bodyCm.get(player).body;
        Filter filter = new Filter();
        filter.categoryBits = Finals.PLAYER_BIT;
        body.getFixtureList().get(0).setFilterData(filter);
        body.setLinearDamping(5f);

        if (Globals.isMobile) {
            mobileController = new MobileController(this);
            Globals.game.drawables.add(mobileController);
            Gdx.input.setInputProcessor(mobileController);
        } else {
            desktopController = new DesktopController(this);
            Gdx.input.setInputProcessor(desktopController);
        }
    }

    public void shoot(Vector2 vector2) {
        // TODO: 2/18/2017 implement shooting
    }
}
