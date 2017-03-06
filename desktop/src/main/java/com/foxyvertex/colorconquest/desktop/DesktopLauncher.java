package com.foxyvertex.colorconquest.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.foxyvertex.colorconquest.ColorConquest;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }

    private static LwjglApplication createApplication() {
        return new LwjglApplication(new ColorConquest(), getDefaultConfiguration());
    }

    private static LwjglApplicationConfiguration getDefaultConfiguration() {
        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
        configuration.title = "Color Conquest";
//        configuration.width = 1920;
//        configuration.height = 1080;
        configuration.addIcon("gfx/splash/thefoxarmy.jpg", Files.FileType.Internal);
        return configuration;
    }
}