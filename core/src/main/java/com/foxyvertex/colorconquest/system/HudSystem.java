package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.foxyvertex.colorconquest.ColorConquest;
import com.foxyvertex.colorconquest.component.Health;
import com.foxyvertex.colorconquest.component.Hud;
import com.foxyvertex.colorconquest.component.Player;
import com.foxyvertex.colorconquest.tools.Utilities;
import com.kotcrab.vis.runtime.component.Origin;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.render.RenderBatchingSystem;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/19/2017.
 */

/**
 * The HudSystem manages the HUD
 */
public class HudSystem extends EntitySystem implements AfterSceneInit {

    CameraManager cameraManager;

    VisIDManager idManager;

    Pixmap colorIndicatorDrawer;
    Pixmap healthIndicatorDrawer;

    Entity player;
    Entity colorMeter;
    Entity healthBar;

    boolean notStarted = true;
    Batch batch;

    private ComponentMapper<VisSprite> spriteCm;
    private com.artemis.ComponentMapper<Transform> transformCm;
    private ComponentMapper<Origin> originCm;

    public HudSystem() {
        super(Aspect.all(Hud.class));
    }

    public boolean renderFiringModeLine = false;
    public Vector2 firingModeClickPoint;
    public Vector2 firingModeStartPos;

    @Override
    protected void processSystem() {
        if (notStarted) {
            updateColorMeter();
            updateHealthBar();
            notStarted = false;
        }
        batch = getWorld().getSystem(RenderBatchingSystem.class).getBatch();
        renderFiringMode();
        renderSpriteEntity(colorMeter);
        renderSpriteEntity(healthBar);
    }

    public void renderFiringMode() {
        if (renderFiringModeLine) {
            if (firingModeClickPoint != null && firingModeStartPos != null) {
                float m = 0.5f * (float) Math.sqrt(20); // Direct velocity
                Vector2 cp = new Vector2();
                cp.x = Utilities.map(firingModeClickPoint.x, 0, Gdx.graphics.getWidth(), 0, cameraManager.getViewport().getWorldWidth());
                cp.y = Utilities.map(firingModeClickPoint.y, 0, Gdx.graphics.getHeight(), cameraManager.getViewport().getWorldHeight(), 0);
                double theta = Math.atan((cp.y-firingModeStartPos.y)/(cp.x-firingModeStartPos.x));
                double alpha = m * Math.sin(theta);
                double beta  = m * Math.cos(theta);
                if ((cp.x-firingModeStartPos.x) < 0 && !(beta < 0)) {
                    beta *= -1;
                    alpha *= -1;
                }

                Vector2 clickPointBasedImpulse = new Vector2((float) beta, (float) alpha);
                clickPointBasedImpulse.add(firingModeStartPos);
                Utilities.DrawDebugLine(firingModeStartPos, cp, 2, Color.RED, cameraManager.getCombined());
                if (clickPointBasedImpulse.x <= cp.x && clickPointBasedImpulse.y <= cp.y) {
                    Utilities.DrawDebugLine(firingModeStartPos, clickPointBasedImpulse, 2, Color.GREEN, cameraManager.getCombined());
                } else {
                    Utilities.DrawDebugLine(firingModeStartPos, cp, 2, Color.GREEN, cameraManager.getCombined());
                }

            }
        }
    }

    private void renderSpriteEntity (int entityId) {
        VisSprite sprite = spriteCm.get(entityId);
        Transform transform = transformCm.get(entityId);
        Origin origin = originCm.get(entityId);

        batch.begin();
        batch.draw(sprite.getRegion(), transform.getX(), transform.getY(), origin.getOriginX(), origin.getOriginY(),
                sprite.getWidth(), sprite.getHeight(), transform.getScaleX(), transform.getScaleY(), transform.getRotation());
        batch.end();
    }

    private void renderSpriteEntity (Entity e) {
        VisSprite sprite = spriteCm.get(e);
        Transform transform = transformCm.get(e);
        Origin origin = originCm.get(e);

        batch.begin();
        batch.draw(sprite.getRegion(), transform.getX(), transform.getY(), origin.getOriginX(), origin.getOriginY(),
                sprite.getWidth(), sprite.getHeight(), transform.getScaleX(), transform.getScaleY(), transform.getRotation());
        batch.end();
    }

    /**
     * afterSceneInit is called before the processSystem methods of all of the systems to allow for initiation of the system.
     */
    @Override
    public void afterSceneInit() {
        player = idManager.get("player");
        colorMeter = idManager.get("colorMeter");
        healthBar = idManager.get("healthBar");
    }

    /**
     * updateHealthBar is called when the health of the player is changed
     */
    public void updateHealthBar() {
        if (player.getComponent(Health.class) != null) {
            Gdx.app.log("", "");
            healthIndicatorDrawer = new Pixmap(255, 30, Pixmap.Format.RGB888);
            healthIndicatorDrawer.setColor(Color.RED);
            healthIndicatorDrawer.fillRectangle(0, 0, (int)Utilities.map(player.getComponent(Health.class).getCurrentHealth(), 0, player.getComponent(Health.class).getMaxHealth(), 0, healthIndicatorDrawer.getWidth()), healthIndicatorDrawer.getHeight());

            healthBar.getComponent(VisSprite.class).setRegion(new TextureRegion(new Texture(healthIndicatorDrawer)));
        }
    }

    /**
     * updateColorMeter gets called when the color meter's data get changed
     */
    public void updateColorMeter() {
        if (player.getComponent(Player.class) != null) {
            colorIndicatorDrawer = new Pixmap(85, 255, Pixmap.Format.RGB888);

            colorIndicatorDrawer.setColor(Color.BLACK);
            colorIndicatorDrawer.drawRectangle(0, 0, colorIndicatorDrawer.getWidth(), colorIndicatorDrawer.getHeight());

            colorIndicatorDrawer.setColor(Color.RED);
            colorIndicatorDrawer.fillRectangle(0, colorIndicatorDrawer.getHeight() - player.getComponent(Player.class).red, colorIndicatorDrawer.getWidth() / 3, player.getComponent(Player.class).red);

            colorIndicatorDrawer.setColor(Color.GREEN);
            colorIndicatorDrawer.fillRectangle(colorIndicatorDrawer.getWidth() / 3, colorIndicatorDrawer.getHeight() - player.getComponent(Player.class).green, colorIndicatorDrawer.getWidth() / 3, player.getComponent(Player.class).green);

            colorIndicatorDrawer.setColor(Color.BLUE);
            colorIndicatorDrawer.fillRectangle(colorIndicatorDrawer.getWidth() / 3 * 2, colorIndicatorDrawer.getHeight() - player.getComponent(Player.class).blue, colorIndicatorDrawer.getWidth() / 3, player.getComponent(Player.class).blue);

            colorIndicatorDrawer.setColor(Color.WHITE);
            int xLoc = 0;
            switch (getWorld().getSystem(PlayerSystem.class).currentColorIndex) {
                case (0):
                    colorIndicatorDrawer.drawRectangle(0, 0, colorIndicatorDrawer.getWidth() / 3, colorIndicatorDrawer.getHeight());
                    xLoc = 0;
                    break;
                case (1):
                    colorIndicatorDrawer.drawRectangle((colorIndicatorDrawer.getWidth() / 3), 0, (colorIndicatorDrawer.getWidth() / 3), colorIndicatorDrawer.getHeight());
                    xLoc = colorIndicatorDrawer.getWidth() / 3;
                    break;
                case (2):
                    colorIndicatorDrawer.drawRectangle((colorIndicatorDrawer.getWidth() / 3) * 2, 0, (colorIndicatorDrawer.getWidth() / 3), colorIndicatorDrawer.getHeight());
                    xLoc = colorIndicatorDrawer.getWidth() / 3 * 2;
                    break;
            }

            colorIndicatorDrawer.fillRectangle(xLoc, colorIndicatorDrawer.getHeight() - 10, colorIndicatorDrawer.getWidth() / 3, 5);
            colorMeter.getComponent(VisSprite.class).setRegion(new TextureRegion(new Texture(colorIndicatorDrawer)));
        } else {
            notStarted = true;
        }
    }
}
