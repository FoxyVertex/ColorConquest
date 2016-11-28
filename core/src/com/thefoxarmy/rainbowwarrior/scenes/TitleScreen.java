package com.thefoxarmy.rainbowwarrior.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.managers.Assets;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;
import com.thefoxarmy.rainbowwarrior.screens.MenuScreen;

/**
 * Created by aidan on 11/27/2016.
 */

public class TitleScreen extends Scene {

    //Scene2D widgets
    private Table table;
    private TextButton btnResume;
    private TextButton btnQuit;
    private TextButton btnContinue;

    /**
     * This sets up the pause menu's stage and lets it be amazing
     */
    public TitleScreen(final MenuScreen screen){
        super(screen);

        TextButton btnPlay = new TextButton("Play", Assets.guiSkin, "default");
        btnPlay.setSize(200, 50);
        btnPlay.setPosition(Gdx.graphics.getWidth() / 2 - btnPlay.getWidth() / 2, Gdx.graphics.getHeight() - btnPlay.getHeight() - 40);

        table = new Table();
        table.setWidth(stage.getWidth());
        table.setPosition(Gdx.graphics.getWidth() / 2, btnPlay.getY() - 50, Align.center | Align.top);

        TextButton btnNew = new TextButton("New Game", Assets.guiSkin);
        //Call the newSave() method when the new button is pushed.
        btnNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                newSave();
            }
        });
        table.padTop(15);
        table.add(btnNew).pad(10);

        btnContinue = new TextButton("Continue Game", Assets.guiSkin);
        btnContinue.setVisible(false);
        table.padTop(30);
        //If user data has been stored, the contuse button is viable.
        if (DynamicGlobals.prefs.contains("Level")) {
            btnContinue.setVisible(true);
            DynamicGlobals.prefs.flush();
        }
        table.add(btnContinue).pad(10);

        TextButton btnLoad = new TextButton("Load Save", Assets.guiSkin);
        table.padTop(30);
        table.add(btnLoad).pad(10);
        //When the play button is clicked, load the play options options table.
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                stage.addActor(table);
            }
        });
        //When the contiue button is clicked, start a new instance of the play screen and load the level.
        btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                loadLevel(DynamicGlobals.prefs.getString("Level"));
            }
        });

        stage.addActor(btnPlay);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called to create a new save and prompt if override is desirable
     */
    private void newSave() {
        //If there's no User data, create it, and make the continue button viable.
        if (!DynamicGlobals.prefs.contains("Level")) {

            DynamicGlobals.prefs.putString("Level", "levels/test.tmx");
            DynamicGlobals.prefs.flush();
            //IF there is User Data, however, create a dialog prompt asking the user if it's okay to overwrite their existing User data.
        } else {
            final Dialog dl = new Dialog("Overwrite Existing save?", Assets.guiSkin);

            Table btnTable = new Table();

            TextButton btnYes = new TextButton("Yes", Assets.guiSkin);
            //If the user agrees, their userdata will be overwritten, and the dialog will disappear
            btnYes.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent i, float x, float y) {
                    Assets.playSound(Assets.clickSound);
                    DynamicGlobals.prefs.putString("Level", "levels/test.tmx").flush();
                    dl.setVisible(false);
                }
            });
            //If the user refuses, the dialog will dissapear
            TextButton btnNo = new TextButton("No", Assets.guiSkin);
            btnNo.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent i, float x, float y) {
                    Assets.playSound(Assets.clickSound);
                    dl.setVisible(false);
                }
            });
            btnTable.top();
            btnTable.add(btnYes);
            btnTable.add(btnNo);
            dl.add(btnTable);
            dl.show(stage);
            stage.addActor(dl);
        }
        btnContinue.setVisible(true);

    }

    /**
     * Set the current screen to a new instance of a play screen
     * @param level string to load the TMX.
     */
    private void loadLevel(String level) {
        DynamicGlobals.game.setScreen(new GameScreen(DynamicGlobals.game, DynamicGlobals.prefs.getString("Level")));
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

}
