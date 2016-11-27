package com.thefoxarmy.rainbowwarrior.tools;

/**
 * Created by aidan on 11/26/2016.
 */

public class Utilities {
    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }
}
