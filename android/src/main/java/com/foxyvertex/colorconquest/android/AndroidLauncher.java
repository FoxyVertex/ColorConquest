package com.foxyvertex.colorconquest.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.foxyvertex.colorconquest.ColorConquest;
import com.foxyvertex.colorconquest.Globals;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Globals.isMobile = true;
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        initialize(new ColorConquest(), configuration);
    }
}