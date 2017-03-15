package com.foxyvertex.colorconquest;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.foxyvertex.colorconquest.component.Player;
import com.kotcrab.vis.runtime.component.PhysicsBody;

/**
 * Created by aidan on 3/9/17.
 */

public class ColorEffects {
    public static ColorEffect RED;
    public static ColorEffect GREEN;
    public static ColorEffect BLUE;
    public static ColorEffect WHITE;

    public static ColorEffect NONCOLOR_FIRING_MODE_SLOWING;

    public interface ColorEffect {
        void go(Entity player, Entity environmentEntity);
        void og(Entity player, Entity environmentEntity);
    }

    public static void load() {
        NONCOLOR_FIRING_MODE_SLOWING = new ColorEffect() {
            boolean activated = false;
            public float value = 0.5f;
            @Override
            public void go(Entity player, Entity environmentEntity) {
                if (!activated) {
                    Player playerComponent = player.getComponent(Player.class);
                    playerComponent.runSpeed *= value;
                    activated = true;
                }
            }

            @Override
            public void og(Entity player, Entity environmentEntity) {
                if (activated){
                    Player playerComponent = player.getComponent(Player.class);
                    playerComponent.runSpeed /= value;
                    activated = false;
                }
            }
        };

        RED = new ColorEffect() {
            boolean activated = false;
            public float value = 1.7f;
            @Override
            public void go(Entity player, Entity environmentEntity) {
                Player playerComponent = player.getComponent(Player.class);
                playerComponent.runSpeed *= value;
            }

            @Override
            public void og(Entity player, Entity environmentEntity) {
                Player playerComponent = player.getComponent(Player.class);
                playerComponent.runSpeed /= value;
            }
        };

        GREEN = new ColorEffect() {
            boolean activated = false;
            public float value = 4.2f;
            @Override
            public void go(Entity player, Entity environmentEntity) {
                Player playerComponent = player.getComponent(Player.class);
                playerComponent.jumpForce *= value;
            }

            @Override
            public void og(Entity player, Entity environmentEntity) {
                Player playerComponent = player.getComponent(Player.class);
                playerComponent.jumpForce /= value;
            }
        };

        BLUE = new ColorEffect() {
            boolean activated = false;
            public float value = 0.5f;
            @Override
            public void go(Entity player, Entity environmentEntity) {
                Body envEBody = environmentEntity.getComponent(PhysicsBody.class).body;
                envEBody.getFixtureList().get(0).setRestitution(value);
            }

            @Override
            public void og(Entity player, Entity environmentEntity) {

            }
        };

        WHITE = new ColorEffect() {
            boolean activated = false;
            @Override
            public void go(Entity player, Entity environmentEntity) {
                Player playerComponent = player.getComponent(Player.class);
                playerComponent.runSpeed = playerComponent.normalRunSpeed;
                playerComponent.jumpForce = playerComponent.normalJumpForce;
            }

            @Override
            public void og(Entity player, Entity environmentEntity) {

            }
        };
    }
}
