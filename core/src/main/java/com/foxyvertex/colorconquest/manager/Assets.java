package com.foxyvertex.colorconquest.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by aidan on 2/18/2017.
 */

public class Assets {

    public static Skin guiSkin;

    public static void load() {
        guiSkin = new Skin(Gdx.files.internal("skin/clean-crispy-ui.json"));
    }

}
