package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.screens.Screen;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

/**
 * Created by aidan on 11/27/2016.
 */

public class TitleScreen extends Scene {

    //Scene2D widgets
    private VisTable table;

    /**
     * This sets up the pause menu's stage and lets it be amazing
     */
    public TitleScreen(final Screen screen) {
        super(screen);

        table = new VisTable();
        table.center();
        table.setFillParent(true);

        //When the play button is clicked, load the play options options table.
        VisTextButton btnPlay = new VisTextButton("Play");
        btnPlay.setSize(200, 50);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                if (Globals.playMenuScene != null)
                    ((Screen) Globals.game.getScreen()).switchScene(Globals.playMenuScene);
                else {
                    Globals.playMenuScene = new PlayMenu((Screen) Globals.game.getScreen());
                    ((Screen) Globals.game.getScreen()).switchScene(Globals.playMenuScene);
                }
            }
        });
        table.add(btnPlay);
        table.row();
        table.add().height(5);
        table.row();
        VisTextButton btnOptions = new VisTextButton("Options");
        btnOptions.setSize(200, 50);
        btnOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                if (Globals.optionsMenuScreen != null)
                    ((Screen) Globals.game.getScreen()).switchScene(Globals.optionsMenuScreen);
                else {
                    Globals.optionsMenuScreen = new OptionsMenu((Screen) Globals.game.getScreen());
                    ((Screen) Globals.game.getScreen()).switchScene(Globals.optionsMenuScreen);
                }
            }
        });
        table.add(btnOptions);
        table.row();
        table.add().height(5);
        table.row();
        VisTextButton btnQuit = new VisTextButton("Quit");
        btnQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                Gdx.app.exit();
            }
        });
        table.add(btnQuit);

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
        stage.act();
        if (!Assets.menuMusic.isPlaying()) Assets.playMusic(Assets.menuMusic);
    }

    public void tick(float delta) {
        stage.act();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

}
