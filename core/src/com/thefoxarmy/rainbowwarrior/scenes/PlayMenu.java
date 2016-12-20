package com.thefoxarmy.rainbowwarrior.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.FinalGlobals;
import com.thefoxarmy.rainbowwarrior.managers.Assets;
import com.thefoxarmy.rainbowwarrior.managers.Levels;
import com.thefoxarmy.rainbowwarrior.managers.UserPrefs;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;
import com.thefoxarmy.rainbowwarrior.screens.Screen;

/**
 * Created by aidan on 11/29/2016.
 */

public class PlayMenu extends Scene {

    Table table;
    TextButton btnContinue;

    public PlayMenu(final Screen screen) {
        super(screen);

        table = new Table();
        table.center();
        table.setFillParent(true);

        btnContinue = new TextButton("Continue Game", Assets.guiSkin);
        btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                loadLevel();
            }
        });
        table.add(btnContinue).expandX();
        table.row();
        //If user data has been stored, the contuse button is viable.
        if (!UserPrefs.gdxPrefs.contains("tiledMap" + DynamicGlobals.currentGameSave)) {
            btnContinue.setVisible(false);
        }

        TextButton btnLoad = new TextButton("Load Save", Assets.guiSkin);
        table.add(btnLoad).expandX();
        table.row();

        TextButton btnNew = new TextButton("New Game", Assets.guiSkin);
        //Call the newSave() method when the new button is pushed.
        btnNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
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

    }

    /**
     * Called to create a new save and prompt if override is desirable
     */
    private void newSave() {
        //If there's no User data, create it, and make the continue button viable.
        if (!UserPrefs.gdxPrefs.contains("tiledMap" + DynamicGlobals.currentGameSave)) {

            UserPrefs.setLevel(DynamicGlobals.currentGameSave, FinalGlobals.firstLevel);
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
                    UserPrefs.setLevel(DynamicGlobals.currentGameSave, FinalGlobals.firstLevel);
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
     * Set the current screen to the play screen
     */
    private void loadLevel() {
        DynamicGlobals.game.setScreen(DynamicGlobals.gameScreen);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
