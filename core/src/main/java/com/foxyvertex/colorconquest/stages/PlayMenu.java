package com.foxyvertex.colorconquest.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.manager.Assets;
import com.foxyvertex.colorconquest.manager.UserPrefs;

/**
 * Created by aidan on 11/29/2016.
 */

public class PlayMenu extends UIStage {

    Table table;
    TextButton btnContinue;

    public PlayMenu() {
        super();

        table = new Table();
        table.center();
        table.setFillParent(true);

        btnContinue = new TextButton("Continue Game", Assets.guiSkin);
        btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                loadLevel();
            }
        });
        table.add(btnContinue).expandX();
        table.row();
        // TODO: 2/18/2017  If user data has been stored, the continue button is visible.

        TextButton btnLoad = new TextButton("Load Save", Assets.guiSkin);
        table.add(btnLoad).expandX();
        table.row();

        TextButton btnNew = new TextButton("New Game", Assets.guiSkin);
        //Call the newSave() method when the new button is pushed.
        btnNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                newSave();
            }
        });
        table.add(btnNew).expandX();

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void tick(float delta) {
        stage.act();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * Called to create a new save and prompt if override is desirable
     */
    private void newSave() {
        UserPrefs.setLevel(++Globals.currentGameSave, 0);
        Globals.game.setScreen(Globals.gameScreen);
    }

    /**
     * Set the current screen to the play screen
     */
    private void loadLevel() {
        Globals.game.setScreen(Globals.gameScreen);
        Gdx.app.log(Finals.ANSI_CYAN + "Title Screen" + Finals.ANSI_RESET, "Starting game...");
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
