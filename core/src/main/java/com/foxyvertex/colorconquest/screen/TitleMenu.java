package com.foxyvertex.colorconquest.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.stages.OptionsMenu;
import com.foxyvertex.colorconquest.stages.PlayMenu;
import com.foxyvertex.colorconquest.stages.TitleScreen;
import com.foxyvertex.colorconquest.stages.UIStage;

/**
 * Created by aidan on 2/19/2017.
 */

public class TitleMenu implements Screen {
    public UIStage currentScene;

    public TitleMenu() {
        currentScene = new TitleScreen( );
        Globals.titleScreenStage = currentScene;
        Gdx.input.setInputProcessor(currentScene.stage);
        currentScene.show();
        Globals.titleMenu = this;

        Globals.playMenuStage = new PlayMenu();
        Globals.optionsMenuStage = new OptionsMenu();
    }

    @Override
    public void show() {
        Gdx.app.log(Finals.ANSI_CYAN + "Title Screen" + Finals.ANSI_RESET, "Started.");
    }

    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(currentScene.stage);
        currentScene.tick(delta);
        Globals.game.batch.setProjectionMatrix(currentScene.stage.getCamera().combined);
        currentScene.stage.act();
        currentScene.stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        currentScene.dispose();
    }

    public void switchScene(UIStage newScene) {
        currentScene = newScene;
        currentScene.show();
    }
}
