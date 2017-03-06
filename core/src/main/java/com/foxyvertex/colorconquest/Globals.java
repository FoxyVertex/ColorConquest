package com.foxyvertex.colorconquest;

import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.screen.GameScreen;
import com.foxyvertex.colorconquest.screen.SplashScreen;
import com.foxyvertex.colorconquest.screen.TitleMenu;
import com.foxyvertex.colorconquest.stages.UIStage;

/**
 * Created by aidan on 2/15/2017.
 */

public class Globals {
    public static ColorConquest game;
    public static TitleMenu titleMenu;
    public static GameScreen gameScreen;
    public static SplashScreen splashScreen;

    public static UIStage titleScreenStage;
    public static UIStage pauseMenuStage;
    //public static UIStage hudStage;
    public static UIStage optionsMenuStage;
    public static UIStage playMenuStage;
    public static UIStage gameReadyStage;

    public static int currentGameSave = 0;

    public static boolean isMobile = false;

    public static Array<Class> systemsToDisableOnPause = new Array<>();
}
