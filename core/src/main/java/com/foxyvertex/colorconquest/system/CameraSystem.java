package com.foxyvertex.colorconquest.system;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.foxyvertex.colorconquest.tools.Utilities;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

/**
 * Created by aidan on 2/13/2017.
 */

public class CameraSystem extends BaseSystem implements AfterSceneInit {
    CameraManager cameraManager;
    VisIDManager idManager;
    ComponentMapper<Transform> transformCm;

    Entity player;
    Transform playerTrans;
    float minX;
    float maxX;
    float minY;
    float maxY;

    Vector2 tmpVect;

    @Override
    protected void processSystem() {
        tmpVect.x = Utilities.clamp(playerTrans.getX(), minX, maxX);
        tmpVect.y = Utilities.clamp(playerTrans.getY(), minY, maxY);
        cameraManager.getCamera().position.set(new Vector3(tmpVect.x, tmpVect.y, cameraManager.getCamera().position.z));
    }

    @Override
    public void afterSceneInit() {
        player = idManager.get("player");

        playerTrans = transformCm.get(player);

        minX = transformCm.get(idManager.get("minimumX")).getX()/100;
        minX += cameraManager.getCamera().viewportWidth / 2;
        maxX = transformCm.get(idManager.get("maximumX")).getX()/100;
        maxX -= cameraManager.getCamera().viewportWidth / 2;
        minY = transformCm.get(idManager.get("minimumY")).getY()/100;
        minY += cameraManager.getCamera().viewportHeight / 2;
        maxY = transformCm.get(idManager.get("maximumY")).getY()/100;
        maxY -= cameraManager.getCamera().viewportHeight / 2;

        tmpVect = new Vector2(cameraManager.getCamera().position.x, cameraManager.getCamera().position.y);
    }
}
