package com.foxyvertex.colorconquest.manager;

import com.artemis.ComponentMapper;
import com.foxyvertex.colorconquest.component.Bounds;

/**
 * Created by aidan on 2/12/2017.
 */

public class GameManager extends BaseSceneManager {
    private ComponentMapper<Bounds> boundsCm;

    public enum GameState {READY, RUNNING, PAUSED, LEVEL_END, GAME_END}

    GameState state = GameState.READY;



    @Override
    public void afterSceneInit () {
        super.afterSceneInit();

    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        unprojectVec.set(screenX, screenY, 0);
        cameraManager.getUiCamera().unproject(unprojectVec);
//
//        float x = unprojectVec.x;
//        float y = unprojectVec.y;
//
//        if (state == GameState.RUNNING && pauseBounds.contains(x, y)) {
//            state = GameState.PAUSED;
//            resumeEntity.edit().remove(new Invisible());
//        }
//
//        if (state == GameState.PAUSED && resumeBounds.contains(x, y)) {
//            state = GameState.RUNNING;
//            resumeEntity.edit().add(new Invisible());
//        }
//
//        if (state == GameState.READY) {
//            state = GameState.RUNNING;
//            readyEntity.edit().add(new Invisible());
//        }

        return false;
    }

    public GameState getState () {
        return state;
    }
}
