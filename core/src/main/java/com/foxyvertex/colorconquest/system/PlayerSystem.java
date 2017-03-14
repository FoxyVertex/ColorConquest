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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.ColorConquest;
import com.foxyvertex.colorconquest.ColorEffects;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.component.Animation;
import com.foxyvertex.colorconquest.component.Bullet;
import com.foxyvertex.colorconquest.component.Health;
import com.foxyvertex.colorconquest.component.Player;
import com.foxyvertex.colorconquest.input.DesktopController;
import com.foxyvertex.colorconquest.input.MobileController;
import com.foxyvertex.colorconquest.tools.DamageRunnable;
import com.foxyvertex.colorconquest.tools.DeathRunnable;
import com.foxyvertex.colorconquest.tools.Utilities;
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
/**
 * The PlayerSystem manages all of the input and movement for the player.
 */
public class PlayerSystem extends BaseSystem implements AfterSceneInit {
    VisIDManager idManager;
    ComponentMapper<VisSprite> spriteCm;
    ComponentMapper<Transform> transformCm;
    ComponentMapper<PhysicsBody> bodyCm;

    CameraManager cameraManager;

    public int currentColorIndex = 0;
    public float speedMultiplier = 1f;
    public boolean jumpPressed, forwardPressed, backwardPressed, downPressed, debugSuperAbilityPressed, debugSpawnpointPressed, debugZoomInPressed, debugZoomOutPressed, debugNextLevelPressed, firingModePressed;
    private boolean jumpPressedPrev, forwardPressedPrev, backwardPressedPrev, downPressedPrev, debugSuperAbilityPressedPrev, debugSpawnpointPressedPrev, debugZoomInPressedPrev, debugZoomOutPressedPrev, debugNextLevelPressedPrev, firingModePressedPrev;
    private float currentJumpLength = 0;
    private boolean canJump = true;
    private boolean jumpReleased = false;

    private float forceScale = 0.01f;
    private Vector2 initialBulletImpulse = new Vector2(4, 2);
    private Vector2 iInitialBulletImpulse = new Vector2(-4, 2);

    public Health healthComp;
    public Entity player;
    public Player playerComp;
    private VisSprite sprite;
    private Transform transform;
    public Body body;

    private enum FacingDIRECTION {LEFT, RIGHT}
    private FacingDIRECTION facingDIRECTION = FacingDIRECTION.RIGHT;

    private boolean shouldFlipX = false;
    private boolean shouldFlipY = false;

    private MobileController mobileController;
    private DesktopController desktopController;

    public boolean isGamePaused = false;

    private TextureRegion bulletTextureRegion;

    /**
     * processSystem is called every frame at a max of 60 times per second. It handles all input and movement logic.
     */
    @Override
    protected void processSystem() {
        if (!isGamePaused) {
            float desiredXVel = 0f;
            float xVel = body.getLinearVelocity().x;
            float yVel = body.getLinearVelocity().y;

            if (Globals.isMobile) {
                mobileController.handleInput();
            } else {
                desktopController.handleInput(getWorld().getDelta());
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

            if (Gdx.input.isKeyPressed(Input.Keys.B)) {
                player.getComponent(Player.class).blue += 1;
            }
            if (debugNextLevelPressed) {
                Globals.gameScreen.nextLevel();
            }
            if (debugSuperAbilityPressed && !debugSuperAbilityPressedPrev) {
                playerComp.runSpeed *= 2;
                playerComp.jumpForce *= 2;
                debugSuperAbilityPressedPrev = true;
            } else if (debugSuperAbilityPressedPrev && !debugSuperAbilityPressed) {
                playerComp.runSpeed /= 2;
                playerComp.jumpForce /= 2;
                debugSuperAbilityPressedPrev = false;
            }

            if (debugZoomInPressed)
                cameraManager.getCamera().zoom += 1.3f * getWorld().getDelta();
            if (debugZoomOutPressed)
                cameraManager.getCamera().zoom -= 1.3f * getWorld().getDelta();

            if (firingModePressed) {

                float bulletStartXValue;
                if (facingDIRECTION == FacingDIRECTION.LEFT) {
                    bulletStartXValue = -0.2f;
                } else {
                    bulletStartXValue = 0.75f;
                }
                getWorld().getSystem(HudSystem.class).renderFiringModeLine = true;
                getWorld().getSystem(HudSystem.class).firingModeClickPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                getWorld().getSystem(HudSystem.class).firingModeStartPos = new Vector2(body.getPosition()).add(bulletStartXValue, 1f);
                ColorEffects.NONCOLOR_FIRING_MODE_SLOWING.go(player, null);
                firingModePressedPrev = true;
            } else if (firingModePressedPrev) {
                getWorld().getSystem(HudSystem.class).renderFiringModeLine = false;
                ColorEffects.NONCOLOR_FIRING_MODE_SLOWING.og(player, null);
                firingModePressedPrev = false;
            }

            player.getComponent(Player.class).isFiring = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT);

            if (jumpPressed && !jumpReleased) {
                currentJumpLength += world.getDelta();

                if (currentJumpLength >= maxJumpForceLength) canJump = false;
                if (currentJumpLength < maxJumpForceLength) canJump = true;

                jumpPressedPrev = true;
            } else if (body.getLinearVelocity().y <= 0.0001 && body.getLinearVelocity().y >= -0.0001) {
                jumpReleased = false;
            } else {
                if (jumpPressedPrev && !jumpPressed) {
                    jumpReleased = true;
                    currentJumpLength = 0f;
                }
                jumpPressedPrev = false;
            }

            if (downPressed)
                body.applyLinearImpulse(new Vector2(0, -10f), body.getWorldCenter(), true);

            if (forwardPressed) {
                desiredXVel = playerComp.runSpeed;
                forwardPressedPrev = true;
                if (player.getComponent(Animation.class) != null) world.getSystem(AnimationSystem.class).changeAnimState(player, "walk", false, false, true);
                facingDIRECTION = FacingDIRECTION.RIGHT;
            } else if (forwardPressedPrev) {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                forwardPressedPrev = false;
            }

            if (backwardPressed) {
                desiredXVel = -playerComp.runSpeed;
                if (player.getComponent(Animation.class) != null) world.getSystem(AnimationSystem.class).changeAnimState(player, "walk", true, false, true);
                facingDIRECTION = FacingDIRECTION.LEFT;
                backwardPressedPrev = true;
            } else if (backwardPressedPrev) {
                body.setLinearVelocity(0, body.getLinearVelocity().y);
                backwardPressedPrev = false;
            }
            if (body.getLinearVelocity().y < -0.01) {
                if (player.getComponent(Animation.class) != null) world.getSystem(AnimationSystem.class).changeAnimState(player, "fall", shouldFlipX, false, true);
            }

            switch (facingDIRECTION) {
                case RIGHT:
                    shouldFlipX = false;
                    break;
                case LEFT:
                    shouldFlipX = true;
                    break;
            }

            if (!backwardPressed && !forwardPressed && !downPressed && !jumpPressed && (body.getLinearVelocity().y >= -0.001) && (body.getLinearVelocity().y <= 0.001f)) {
                if (player.getComponent(Animation.class) != null) world.getSystem(AnimationSystem.class).changeAnimState(player, "idle", shouldFlipX, shouldFlipY, true);
            }



            if (currentJumpLength > 0 && canJump && !jumpReleased) {
                body.applyLinearImpulse(new Vector2(0, player.getComponent(Player.class).jumpForce * world.getDelta()), body.getWorldCenter(), true);
                if (player.getComponent(Animation.class) != null) world.getSystem(AnimationSystem.class).changeAnimState(player, "jumpstart", shouldFlipX, false, false);
            }

            float velChange = desiredXVel - xVel;
            float impulse = body.getMass() * velChange;
            body.applyForce(impulse, 0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        } else {
            Gdx.input.setInputProcessor(Globals.pauseMenuStage.stage);
            Globals.pauseMenuStage.stage.act();
        }
    }

    /**
     * afterSceneInit is called before the processSystem methods of all of the systems to allow for initiation of the system.
     */
    @Override
    public void afterSceneInit() {
        // Get the player entity from the scene
        player = idManager.get("player");

        // Add the Player component to the player entity
        player.edit().add(new Player());
        playerComp = player.getComponent(Player.class);

        // Add health component
        player.edit().add(new Health(new DeathRunnable() {
            @Override
            public void run(Entity e) {
                e.getWorld().getSystem(PlayerSystem.class).playerDeath();
                getWorld().getSystem(HudSystem.class).updateHealthBar();
            }
        }, 20f, new DamageRunnable() {
            @Override
            public void run(Entity e, float damage) {
                e.getWorld().getSystem(HudSystem.class).updateHealthBar();
            }
        }));
        healthComp = player.getComponent(Health.class);

        // Configure the Player component
        playerComp.colors = new Array<>();
        playerComp.colors.add(Color.RED);
        playerComp.colors.add(Color.GREEN);
        playerComp.colors.add(Color.BLUE);
        player.getComponent(Variables.class).put("collisionCat", "player");

        // Get the sprite, transform, and body components of the player and put them in class variables
        sprite = spriteCm.get(player);
        transform = transformCm.get(player);
        body = bodyCm.get(player).body;

        // Set the player's category bit (might be redundant, not sure atm)
        Filter filter = new Filter();
        filter.categoryBits = Finals.PLAYER_BIT;
        body.getFixtureList().get(0).setFilterData(filter);

        // Setup the input delegates
        if (Globals.isMobile) {
            mobileController = new MobileController(this);
            Globals.gameScreen.drawables.add(mobileController);
            Gdx.input.setInputProcessor(mobileController);
        } else {
            desktopController = new DesktopController(this);
            Gdx.input.setInputProcessor(desktopController);
        }

        // Create the bullets' texture
        Pixmap pixmap = new Pixmap(20, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(pixmap.getWidth() / 2, pixmap.getHeight() / 2, 20 / 2);
        bulletTextureRegion = new TextureRegion(new Texture(pixmap));
    }

    /**
     * Fire a color bullet when the input delegate tells me to.
     * @param clickPoint used in firing mode for more accurate firing
     */
    public void shoot(Vector2 clickPoint) {
        // Subtract the correct amount of color from the player's stash
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

        // Update the color meter
        updateHud();

        float bulletStartXValue;
        if (facingDIRECTION == FacingDIRECTION.LEFT) {
            bulletStartXValue = -0.2f;
        } else {
            bulletStartXValue = 0.75f;
        }

        float m = 2f * (float) Math.sqrt(20); // Direct velocity
        Vector2 cp = new Vector2();
        Vector2 pp = new Vector2(body.getPosition()).add(bulletStartXValue, 1f);
        cp.x = Utilities.map(clickPoint.x, 0, Gdx.graphics.getWidth(), 0, cameraManager.getViewport().getWorldWidth());
        cp.y = Utilities.map(clickPoint.y, 0, Gdx.graphics.getHeight(), cameraManager.getViewport().getWorldHeight(), 0);
        double theta = Math.atan((cp.y-pp.y)/(cp.x-pp.x));
        double alpha = m * Math.sin(theta);
        double beta  = m * Math.cos(theta);
        if ((cp.x-pp.x) < 0 && !(beta < 0)) {
            beta *= -1;
            alpha *= -1;
        }

        Vector2 clickPointBasedImpulse = new Vector2((float) beta, (float) alpha);

        // Create the bullet component
        Bullet bulletComp = new Bullet();
        bulletComp.color = playerComp.selectedColor;


        Vector2 impulse;

        if (firingModePressed) {
            impulse = clickPointBasedImpulse;
        } else {
            if (facingDIRECTION == FacingDIRECTION.LEFT) {
                impulse = iInitialBulletImpulse;
            } else {
                impulse = initialBulletImpulse;
            }
        }


        // Create the transform component
        Transform transformComp = new Transform(body.getPosition().x+bulletStartXValue, body.getPosition().y+1f);

        // Set the collisionCat variable for the SetupEntityComponentsSystem to set the category bit
        Variables variables = new Variables();
        variables.put("collisionCat", "bullet");

        // Create the bullet
        Entity thisBullet = world.createEntity().edit()
                .add(new Renderable(0))
                .add(new Layer(Globals.gameScreen.scene.getLayerDataByName("Foreground").id))
                .add(transformComp)
                .add(new Tint(playerComp.selectedColor))
                .add(bulletComp)
                .add(variables)
                .getEntity();

        // Create a vector from the transform component
        Vector2 worldPos = new Vector2(transformComp.getX(), transformComp.getY());

        // Create a body definition and set it's position
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(worldPos);

        // Create the body from the body definition
        Body body = getWorld().getSystem(PhysicsSystem.class).getPhysicsWorld().createBody(bodyDef);
        body.setType(BodyDef.BodyType.DynamicBody);
        body.setUserData(thisBullet);

        // Configure the body's physics properties
        body.setGravityScale(1f);
        body.setBullet(true);
        body.setFixedRotation(true);
        body.setSleepingAllowed(false);
        CircleShape shape = new CircleShape();
        shape.setRadius(0.1f);
        shape.setPosition(new Vector2(0.1f, 0.1f));

        // Apply the initial velocity of the bullet
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);

        // Create the body's fixture and hitbox
        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        body.createFixture(fd);
        shape.dispose();

        // Create the sprite component
        VisSprite visSpriteComp = new VisSprite(bulletTextureRegion);
        visSpriteComp.setSize(0.2f, 0.2f);

        // Create the origin component
        Origin origin = new Origin();
        origin.setOrigin(0, 0);
        origin.setDirty(true);

        // Add the sprite, body, and fixed rotation to the bullet
        thisBullet.edit()
                .add(new PhysicsBody(body))
                .add(new OriginalRotation(transform.getRotation()))
                .add(visSpriteComp)
                .add(origin);
    }

    /**
     * Called when the player dies
     */
    public void playerDeath() {
        Globals.gameScreen.resetLevel();
    }

    /**
     * Tells the HudSystem to update the hud
     */
    public void updateHud() {
        getWorld().getSystem(HudSystem.class).updateColorMeter();
    }
}
