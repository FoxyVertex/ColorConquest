package com.foxyvertex.colorconquest.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.component.Animation;
import com.foxyvertex.colorconquest.component.Bullet;
import com.foxyvertex.colorconquest.component.Player;
import com.foxyvertex.colorconquest.input.DesktopController;
import com.foxyvertex.colorconquest.input.MobileController;
import com.kotcrab.vis.runtime.component.Layer;
import com.kotcrab.vis.runtime.component.Origin;
import com.kotcrab.vis.runtime.component.OriginalRotation;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Renderable;
import com.kotcrab.vis.runtime.component.Tint;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/12/2017.
 */

public class PlayerSystem extends BaseSystem implements AfterSceneInit {
    VisIDManager idManager;
    ComponentMapper<VisSprite> spriteCm;
    ComponentMapper<Transform> transformCm;
    ComponentMapper<PhysicsBody> bodyCm;

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

    float forceScale = 0.01f;
    private Vector2 initialBulletImpulse = new Vector2(4, 2);

    public Entity player;
    public Player playerComp;
    VisSprite sprite;
    Transform transform;
    Body body;

    enum FacingDIRECTION {LEFT, RIGHT}
    FacingDIRECTION facingDIRECTION = FacingDIRECTION.RIGHT;

    boolean shouldFlipX = false;
    boolean shouldFlipY = false;

    MobileController mobileController;
    DesktopController desktopController;

    public boolean isGamePaused = false;

    @Override
    protected void processSystem() {
        if (!isGamePaused) {
            if (Globals.isMobile) {
                mobileController.handleInput();
            } else {
                desktopController.handleInput(getWorld().getDelta());
            }

            if (!animationStarted) {
                world.getSystem(AnimationSystem.class).addEntity(player);
                animationStarted = true;
            }

            switch (currentColorIndex) {
                case 0:
                    playerComp.selectedColor = Color.RED;
                    break;
                case 1:
                    playerComp.selectedColor = Color.GREEN;
                    break;
                case 2:
                    playerComp.selectedColor = Color.BLUE;
                    break;
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

//            Gdx.app.log("r", "" + playerComp.runSpeed);
//            Gdx.app.log("j", "" + playerComp.jumpForce);

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
                sprite.setFlip(false, false);
                body.applyLinearImpulse(new Vector2(playerComp.runSpeed * speedMultiplier * forceScale, 0), body.getWorldCenter(), true);
                forwardPressedPrev = true;
                world.getSystem(AnimationSystem.class).changeAnimState(player, "walk", false, false, true);
                facingDIRECTION = FacingDIRECTION.RIGHT;
            } else if (!backwardPressed && forwardPressedPrev) {
                //body.applyLinearImpulse(new Vector2(-playerComp.runSpeed * speedMultiplier * forceScale, 0), body.getWorldCenter(), true);
                forwardPressedPrev = false;
            }

            if (backwardPressed && body.getLinearVelocity().x >= -2 * speedMultiplier) {
                sprite.setFlip(true, false);
                body.applyLinearImpulse(new Vector2(-playerComp.runSpeed * speedMultiplier * forceScale, 0), body.getWorldCenter(), true);
                world.getSystem(AnimationSystem.class).changeAnimState(player, "walk", true, false, true);
                facingDIRECTION = FacingDIRECTION.LEFT;
                backwardPressedPrev = true;

            } else if (!backwardPressed && backwardPressedPrev) {
                //body.applyLinearImpulse(new Vector2(playerComp.runSpeed * speedMultiplier * forceScale, 0), body.getWorldCenter(), true);
                backwardPressedPrev = false;
            }

            if (body.getLinearVelocity().y < -0.0001) {
                world.getSystem(AnimationSystem.class).changeAnimState(player, "fall", shouldFlipX, false, true);
            }

            if (!backwardPressed && !forwardPressed && !downPressed && !jumpPressed && (body.getLinearVelocity().y >= -0.001) && (body.getLinearVelocity().y <= 0.001f)) {

                switch (facingDIRECTION) {
                    case RIGHT:
                        shouldFlipX = false;
                        break;
                    case LEFT:
                        shouldFlipX = true;
                        break;
                }
                world.getSystem(AnimationSystem.class).changeAnimState(player, "idle", shouldFlipX, shouldFlipY, true);
            }



            if (currentJumpLength > 0 && canJump) {
                body.applyLinearImpulse(new Vector2(0, player.getComponent(Player.class).jumpForce * world.getDelta()), body.getWorldCenter(), true);
                world.getSystem(AnimationSystem.class).changeAnimState(player, "jumpstart", shouldFlipX, false, false);
                inAir = true;
            }
        } else {
            Gdx.input.setInputProcessor(Globals.pauseMenuStage.stage);
            Globals.pauseMenuStage.stage.act();
        }
    }

    @Override
    public void afterSceneInit() {
        player = idManager.get("player");
        player.edit().add(new Player());
        playerComp = player.getComponent(Player.class);
        playerComp.colors = new Array<>();
        playerComp.colors.add(Color.RED);
        playerComp.colors.add(Color.GREEN);
        playerComp.colors.add(Color.BLUE);
        player.getComponent(Variables.class).put("collisionCat", "player");

        Animation animComp = new Animation();
        animComp.path = "gfx/GreyGuy.atlas";
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
        //body.setLinearDamping(5f);

        if (Globals.isMobile) {
            mobileController = new MobileController(this);
            Globals.gameScreen.drawables.add(mobileController);
            Gdx.input.setInputProcessor(mobileController);
        } else {
            desktopController = new DesktopController(this);
            Gdx.input.setInputProcessor(desktopController);
        }
    }

    public void shoot(Vector2 clickPoint) {
        if (playerComp.selectedColor == Color.RED)
            if (playerComp.red <= 0)
                return;
            else
                playerComp.red--;
        else if (playerComp.selectedColor == Color.GREEN)
            if (playerComp.green <= 0)
                return;
            else
                playerComp.green--;
        else if (playerComp.selectedColor == Color.BLUE)
            if (playerComp.blue <= 0)
                return;
            else
                playerComp.blue--;
        else
            return;
        updateHud();

        Bullet bulletComp = new Bullet();
        bulletComp.color = playerComp.selectedColor;
        Transform transformComp = new Transform(body.getPosition().x+0.8f, body.getPosition().y+1f);
        Variables variables = new Variables();
        variables.put("collisionCat", "bullet");

        Entity thisBullet = world.createEntity().edit()
                .add(new Renderable(0))
                .add(new Layer(Globals.gameScreen.scene.getLayerDataByName("Foreground").id))
                .add(transformComp)
                .add(new Tint())
                .add(bulletComp)
                .add(variables)
                .getEntity();

        Vector2 worldPos = new Vector2(transformComp.getX(), transformComp.getY());

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(worldPos);

        Body body = getWorld().getSystem(PhysicsSystem.class).getPhysicsWorld().createBody(bodyDef);
        body.setType(BodyDef.BodyType.DynamicBody);
        body.setUserData(thisBullet);

        body.setGravityScale(1f);
        body.setBullet(true);
        body.setFixedRotation(true);
        body.setSleepingAllowed(false);
        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f);
        shape.setPosition(new Vector2(0.1f, 0.1f));

        body.applyLinearImpulse(initialBulletImpulse.scl((facingDIRECTION == FacingDIRECTION.LEFT ? -1f : 1f), 1f), body.getWorldCenter(), true);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        body.createFixture(fd);
        shape.dispose();

        Vector2 vect = new Vector2(0, 0);
        for (Fixture f : body.getFixtureList()) {
            vect = new Vector2(shape.getRadius() * 2, shape.getRadius() * 2);
        }

        Pixmap pixmap = new Pixmap((int) (vect.x * 100), (int) (vect.y * 100), Pixmap.Format.RGBA8888);
        pixmap.setColor((playerComp.selectedColor == Color.BLACK || playerComp.selectedColor == null) ? Color.RED : playerComp.selectedColor);
        pixmap.fillCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, (int) (vect.x * 100) / 2);
        TextureRegion textureRegion = new TextureRegion(new Texture(pixmap));
        VisSprite visSpriteComp = new VisSprite(textureRegion);
        visSpriteComp.setSize(0.2f, 0.2f);

        Origin origin = new Origin();
        origin.setOrigin(0, 0);
        origin.setDirty(true);

        thisBullet.edit()
                .add(new PhysicsBody(body))
                .add(new OriginalRotation(transform.getRotation()))
                .add(visSpriteComp)
                .add(origin);
    }

    public void updateHud() {
        getWorld().getSystem(HudSystem.class).updateColorMeter();
    }
}
