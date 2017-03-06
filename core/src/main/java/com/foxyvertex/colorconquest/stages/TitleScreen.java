package com.foxyvertex.colorconquest.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.manager.Assets;

/**
 * Created by aidan on 11/27/2016.
 */

public class TitleScreen extends UIStage {

    //Scene2D widgets
    private Table table;

    /**
     * This sets up the pause menu's stage and lets it be amazing
     */
    public TitleScreen() {

        table = new Table();
        table.center();
        table.setFillParent(true);
        //When the play button is clicked, load the play options options table.
        TextButton btnPlay = new TextButton("Play", Assets.guiSkin, "default");
        btnPlay.setSize(200, 50);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Globals.titleMenu.switchScene(Globals.playMenuStage);
            }
        });
        table.add(btnPlay);
        table.row();
        TextButton btnOptions = new TextButton("Options", Assets.guiSkin, "default");
        btnOptions.setSize(200, 50);
        btnOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Globals.titleMenu.switchScene(Globals.optionsMenuStage);
            }
        });
        table.add(btnOptions);

        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }


    @Override
    public void dispose() {
        stage.dispose();
    }

    /**
     * This method is called to switch the input adapter to the pausemenu's when shown.
     */
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void tick(float delta) {
        stage.act();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

}
