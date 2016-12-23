package com.thefoxarmy.rainbowwarrior.tools;

/**
 * Created by aidan on 11/26/2016.
 *
 * This houses general utilities for the game.
 */

public class Utilities {
    /**
     * Forces a value between two values
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
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] > largest) {
                largest = nums[i];
                val = i;
            }
        }
        return val;
    }
}
