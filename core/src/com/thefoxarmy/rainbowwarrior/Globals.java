package com.thefoxarmy.rainbowwarrior;

/**
 * Houses final variables used by multiple other classes
 */
public class Globals {
    public static final float
            V_WIDTH = 400,
            V_HEIGHT = 225,
            PPM = 100;
    public static String TITLE = "Rainbow Warrior";

    //Collision bits made to test object collsions MUST BE A POWER OF 2
    public static final short NOTHING_BIT = 0,
            BLOCK_BIT = 1,
            PLAYER_BIT = 2,
            END_LEVEL_BIT = 4;
}
