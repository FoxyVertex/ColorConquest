package com.foxyvertex.colorconquest;

import com.artemis.Entity;
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

    public interface ColorEffect {
        void go(Entity player, Entity environmentEntity);
        void og(Entity player, Entity environmentEntity);
    }

    public static void load() {
        RED = new ColorEffect() {
            public float value = 4.5f;
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
