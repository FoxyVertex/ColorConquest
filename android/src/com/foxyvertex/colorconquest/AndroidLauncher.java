package com.foxyvertex.colorconquest;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Globals.isMobileApp = true;

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGyroscope = true;
        initialize(new ColorConquest(), config);
    }
}
