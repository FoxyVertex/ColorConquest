package com.foxyvertex.colorconquest.system;

import com.artemis.BaseSystem;
import com.foxyvertex.colorconquest.Globals;
import com.kotcrab.vis.runtime.system.CameraManager;

/**
 * Created by aidan on 3/9/17.
 */

public class B2DLightsRenderSystem extends BaseSystem {
    CameraManager cameraManager;
    @Override
    protected void processSystem() {
        if (Globals.gameScreen.b2dlHandler != null) {
            Globals.gameScreen.b2dlHandler.setCombinedMatrix(cameraManager.getCamera());
            Globals.gameScreen.b2dlHandler.updateAndRender();
        }
    }
}
