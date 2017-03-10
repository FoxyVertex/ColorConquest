package com.foxyvertex.colorconquest;

/**
 * A set of predefined constant that might need to be used by any class.
 */
public class Finals {
    public static final float
            V_WIDTH = 400,
            V_HEIGHT = 225,
            PPM = 100;
    //Collision bits made to test object collsions -- MUST BE A POWER OF 2
    public static final short NOTHING_BIT = 0,
            BLOCK_BIT = 1,
            COLOR_BIT = 1,
            PLAYER_BIT = 4,
            END_LEVEL_BIT = 8,
            PLAYER_FEET_BIT = 16,
            BULLET_BIT = 32,
            ZOMBIE_BIT = 64,
            EVERYTHING_BIT = BLOCK_BIT | PLAYER_BIT | END_LEVEL_BIT | PLAYER_FEET_BIT | BULLET_BIT;
    public static String TITLE = "Color Conquest";
    public static int firstLevel = 0;

    //DEBUG FEATURES
    public static final boolean SKIP_TO_GAME = true;
    public static final boolean ENABLE_BOX2D_DEBUG_RENDERER = false;


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
}
