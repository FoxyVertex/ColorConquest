package com.foxyvertex.colorconquest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.foxyvertex.colorconquest.ColorConquest;
import com.foxyvertex.colorconquest.Finals;

/**
 * The desktop launcher class
 */
public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new ColorConquest(), config);
        config.title = Finals.TITLE;

    }
}
