package com.foxyvertex.colorconquest.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.foxyvertex.colorconquest.Globals;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by aidan on 1/23/17.
 */

public class Paused extends GameState {

    @Override
    public void update(float delta) {
        if (input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Globals.gameMan.switchState(GameManager.GameState.RUNNING);
            if (Globals.isMobileApp) {
                Gdx.input.setInputProcessor(Globals.gameMan.player.input.mobileController.stage);
            } else {
                Gdx.input.setInputProcessor(Globals.gameMan.player.input.desktopController);
            }
        }
    }

    @Override
    public void render() {
        Globals.gameMan.mapRenderer.render();
        Globals.game.batch.setProjectionMatrix(Globals.gameMan.cam.combined);
        Globals.game.batch.begin();
        Globals.gameMan.player.draw(Globals.game.batch);
        Globals.game.batch.end();
        Globals.game.batch.setProjectionMatrix(Globals.pauseMenuScene.stage.getCamera().combined);
        Globals.pauseMenuScene.stage.act();
        Globals.hudScene.stage.draw();
        Globals.pauseMenuScene.stage.draw();
    }

    @Override
    public void start() {
        Gdx.input.setInputProcessor(Globals.pauseMenuScene.stage);
    }

    @Override
    public void stop() {

    }

    @Override
    public void dispose() {

    }

}
