package com.foxyvertex.colorconquest.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.foxyvertex.colorconquest.screen.SplashScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aidan on 2/18/2017.
 */

public class Assets {

    public static Skin guiSkin;
    public static List<Image> splashScreenLogos = new ArrayList<Image>();

    public static void load() {
        guiSkin = new Skin(Gdx.files.internal("gfx/UI/skin/clean-crispy-ui.json"));

        splashScreenLogos.add(new SplashScreen.SplashLogo("gfx/splash/FoxyVertex.png", 0.8f).actorImage);
    }

}
