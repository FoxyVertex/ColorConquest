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

//    Doesn't work for some reason
//    public static Color getComplimentaryColor(Color originalColor) {
//        HSLColor hslColor = rgbColorToHSL(originalColor);
//        float h2 = hslColor.h + 0.5f;
//        if (h2 > 1) {
//            h2 -= 1;
//        }
//        hslColor.h = h2;
//        return hslColorToRGB(hslColor);
//    }
//
//    public static Color hslColorToRGB(HSLColor hslColor) {
//        float h = hslColor.h;
//        float s = hslColor.s;
//        float l = hslColor.l;
//        float r = 0f;
//        float g = 0f;
//        float b = 0f;
//        float var_2 = 0f;
//        float var_1 = 0f;
//        float h2 = 0f;
//
//        if (s == 0)
//        {
//            r = l * 255;
//            g = l * 255;
//            b = l * 255;
//        }
//        else
//        {
//            if (l < 0.5f)
//            {
//                var_2 = l * (1 + s);
//            }
//            else
//            {
//                var_2 = (l + s) - (s * l);
//            }
//
//            var_1 = 2 * l - var_2;
//            r = 255 * hue_2_rgb(var_1,var_2,h2 + (1 / 3));
//            g = 255 * hue_2_rgb(var_1,var_2,h2);
//            b = 255 * hue_2_rgb(var_1,var_2,h2 - (1 / 3));
//        }
//
//        r = map(r, 0, 255, 0, 1);
//        g = map(g, 0, 255, 0, 1);
//        b = map(b, 0, 255, 0, 1);
//
//        return new Color(r, g, b, 1f);
//    }
//    private static float hue_2_rgb(float v1, float v2, float vh) {
//        if (vh < 0)
//        {
//            vh += 1;
//        }
//
//        if (vh > 1)
//        {
//            vh -= 1;
//        }
//
//        if ((6 * vh) < 1)
//        {
//            return (v1 + (v2 - v1) * 6 * vh);
//        }
//
//        if ((2 * vh) < 1)
//        {
//            return (v2);
//        }
//
//        if ((3 * vh) < 2)
//        {
//            return (v1 + (v2 - v1) * ((2 / 3 - vh) * 6));
//        }
//        return v1;
//    }
//
//    public static HSLColor rgbColorToHSL(Color color) {
//        HSLColor hsl = new HSLColor();
//        float r = color.r;
//        float g = color.g;
//        float b = color.b;
//
//        float min = Math.min(r, Math.min(g, b));
//        float max = Math.max(r, Math.max(g, b));
//        float del_max = max-min;
//
//        float l = (max+min)/2;
//
//        if (del_max == 0) {
//            hsl.h = 0f;
//            hsl.s = 0f;
//        } else {
//            if (l < 0.5f) {
//                hsl.s = del_max / (max+min);
//            } else {
//                hsl.s = del_max / (2 - max - min);
//            }
//
//            float del_r = (((max - r)/6)+(del_max/2))/del_max;
//            float del_g = (((max - g)/6)+(del_max/2))/del_max;
//            float del_b = (((max - b)/6)+(del_max/2))/del_max;
//
//            if (r == max) {
//                hsl.h = del_b-del_g;
//            } else if (g == max) {
//                hsl.h = (1/3) + del_r - del_b;
//            } else if (b == max) {
//                hsl.h = (2/3) + del_g - del_r;
//            }
//
//            if (hsl.h < 0) {
//                hsl.h += 1;
//            }
//            if (hsl.h > 1) {
//                hsl.h -= 1;
//            }
//        }
//
//        return hsl;
//    }

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
