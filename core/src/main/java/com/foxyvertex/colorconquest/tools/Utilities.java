package com.foxyvertex.colorconquest.tools;

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
}
