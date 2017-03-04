package com.foxyvertex.colorconquest.tools;

import com.foxyvertex.colorconquest.Globals;
import com.strongjoshua.console.CommandExecutor;

/**
 * Created by aidan on 2/15/2017.
 */

public class ConsoleCommandExecutor extends CommandExecutor {
    public static void resetLevel() {
        Globals.gameScreen.resetLevel();
        Globals.game.console.log("Resetting...");
    }

    public static void nextLevel() {
        Globals.gameScreen.nextLevel();
        Globals.game.console.log("Skipping...");
    }
}
