package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.foxyvertex.colorconquest.component.Health;
import com.foxyvertex.colorconquest.component.Hud;
import com.foxyvertex.colorconquest.component.Player;
import com.foxyvertex.colorconquest.tools.Utilities;
import com.kotcrab.vis.runtime.component.VisSprite;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/19/2017.
 */

/**
 * The HudSystem manages the HUD
 */
public class HudSystem extends EntitySystem implements AfterSceneInit {

    VisIDManager idManager;

    Pixmap colorIndicatorDrawer;
    Pixmap healthIndicatorDrawer;

    Entity player;
    Entity colorMeter;
    Entity healthBar;

    boolean notStarted = true;

    public HudSystem() {
        super(Aspect.all(Hud.class));
    }

    @Override
    protected void processSystem() {
        updateHealthBar();
    }

    /**
     * afterSceneInit is called before the processSystem methods of all of the systems to allow for initiation of the system.
     */
    @Override
    public void afterSceneInit() {
        player = idManager.get("player");
        colorMeter = idManager.get("colorMeter");
        healthBar = idManager.get("healthBar");
        if (notStarted) updateColorMeter();
    }

    /**
     * updateHealthBar is called when the health of the player is changed
     */
    public void updateHealthBar() {
        if (player.getComponent(Health.class) != null) {
            healthIndicatorDrawer = new Pixmap(255, 30, Pixmap.Format.RGB888);
            healthIndicatorDrawer.setColor(Color.RED);
            healthIndicatorDrawer.fillRectangle(0, 0, (int)Utilities.map(player.getComponent(Health.class).currentHealth, 0, player.getComponent(Health.class).maxHealth, 0, healthIndicatorDrawer.getWidth()), healthIndicatorDrawer.getHeight());

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
