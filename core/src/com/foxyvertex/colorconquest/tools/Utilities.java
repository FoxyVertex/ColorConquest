package com.foxyvertex.colorconquest.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

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

    private static ShapeRenderer debugRenderer = new ShapeRenderer();

    public static int findBiggestIndex(float nums[]) {
        int   val     = 0;
        float largest = 0;
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

    public static Color complement(Color initial) {
        initial.r = 1f - initial.r / 1f;
        initial.g = 1f - initial.g / 1f;
        initial.b = 1f - initial.b / 1f;
        return initial;
    }
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
}
