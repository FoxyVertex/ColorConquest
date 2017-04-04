package com.foxyvertex.colorconquest.desktop;

import com.badlogic.gdx.Files;
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
        config.title = Finals.TITLE;
        config.addIcon("icon/32.png", Files.FileType.Internal);
        config.addIcon("icon/128.png", Files.FileType.Internal);
        //config.width = 1920;
        //config.height = 1080;
        new LwjglApplication(new ColorConquest(), config);
    }
}
