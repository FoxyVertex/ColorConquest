package com.foxyvertex.colorconquest;

/**
 * A set of predefined constant that might need to be used by any class.
 */
public class Finals {
    public static final float
            V_WIDTH  = 400,
            V_HEIGHT = 225,
            PPM      = 100;
    //Collision bits made to test object collsions MUST BE A POWER OF 2
    public static final short NOTHING_BIT = 0,
            BLOCK_BIT                     = 1,
            PLAYER_BIT                    = 2,
            END_LEVEL_BIT                 = 4,
            BULLET_BIT                    = 8,
            SLITHERIKTER_BIT              = 16,
            ENEMY_BUFFER_BIT              = 32,
            SLIME_BIT                     = 64,
            BARRIER_BIT                   = 128,
            EVERYTHING_BIT                = BLOCK_BIT | PLAYER_BIT | END_LEVEL_BIT | BULLET_BIT | SLITHERIKTER_BIT | SLIME_BIT;

    public static String TITLE                          = "Color Conquest";
    public static int    firstLevel                     = 0;
    public static int    Slitherikter_INITIAL_EYE_COLOR = 2080413439;

    public static DebugMode debugMode = DebugMode.SKIP_TO_GAME;

    public enum DebugMode {
        SKIP_SPLASH,
        SKIP_TO_GAME,
        NORMAL
    }
}
