package com.foxyvertex.colorconquest.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.foxyvertex.colorconquest.component.Animation;

/**
 * Created by aidan on 11/26/2016.
 * <p>
 * This houses general utilities for the game.
 */

public class Utilities {
    /**
     * Forces a value between two values
     *
     * @param val the input value
     * @param min the minimum value
     * @param max the maximum value
     * @return the clamped value
     */
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static int findBiggestIndex(float nums[]) {
        int val = 0;
        float largest = 0;
        float totalValue = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > largest) {
                largest = nums[i];
                val = i;
            }
        }
        return val;
    }

    public static float map(float input, float origMin, float origMax, float newMin, float newMax) {
        return (input - origMin) / (origMax - origMin) * (newMax - newMin) + newMin;
    }

    public static Vector2 getTrajectoryPoint(Vector2 startingPos, Vector2 startingVel, float n, World world) {
        //velocity and gravity are given per second but we want time step values here
        float t = 1 / 60.0f; // seconds per time step (at 60fps)
        Vector2 stepVelocity = startingVel.scl(t); // m/s
        Vector2 stepGravity = world.getGravity().scl(t*t); // m/s/s

        stepVelocity.scl(n);
        stepGravity.scl(0.5f * (n*n+n));

        return startingPos.add(stepVelocity.add(stepGravity));
    }

    private static ShapeRenderer debugRenderer = new ShapeRenderer();

    public static void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color, Matrix4 projectionMatrix)
    {
        Gdx.gl.glLineWidth(lineWidth);
        debugRenderer.setProjectionMatrix(projectionMatrix);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(color);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1);
    }

    public static Animation parseAnimationFile(String path) {
        FileHandle fileHandle = Gdx.files.internal(path);
        Json json = new Json();
        json.setElementType(AnimatedObject.class, "animations", AnimationObject.class);
        json.addClassTag("animatedObject", AnimatedObject.class);
        json.addClassTag("animationObject", AnimationObject.class);
        AnimatedObject animatedObject = json.fromJson(AnimatedObject.class, fileHandle.readString());

        Animation animation = new Animation();
        animation.path = animatedObject.path;
        for (int i = 0; i < animatedObject.animations.length; i++) {
            animation.animationNames.add(animatedObject.animations[i].name);
            animation.animationFrameCounts.put(animatedObject.animations[i].name, 1f / animatedObject.animations[i].frameCount);
        }
        animation.currentAnimation = animatedObject.animations[0].name;
        animation.animationType = Animation.AnimType.ATLAS;

        return animation;
    }

    public static class AnimatedObject {
        public String path;
        public AnimationObject[] animations;
    }
    public static class AnimationObject {
        public String name;
        public int frameCount;
    }
}
