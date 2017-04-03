package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.game.GameManager;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.managers.Levels;
import com.foxyvertex.colorconquest.managers.UserPrefs;
import com.foxyvertex.colorconquest.screens.Screen;
import com.kotcrab.vis.ui.util.dialog.ConfirmDialogListener;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by aidan on 11/29/2016.
 */

public class PlayMenu extends Scene {

    VisTable table;
    VisTextButton btnContinue;
    private VisTextButton backButton;
    FileChooser fileChooser;

    public PlayMenu(final Screen screen) {
        super(screen);

        fileChooser = new FileChooser(FileChooser.Mode.OPEN);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().contains(".tmx");
            }
        });
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected (Array<FileHandle> file) {
                Levels.Level lvl = new Levels.Level();
                lvl.path = file.first().path();
                lvl.index = -1;
                lvl.hasCutscene = false;
                lvl.name = "boopaloop";
                Globals.gameMan.playLevel(lvl);
                Globals.game.setScreen(Globals.gameScreen);
            }
        });

        table = new VisTable();
        table.center();
        table.setFillParent(true);

        btnContinue = new VisTextButton("Continue Game");
        btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                loadLevel();
            }
        });
        table.add(btnContinue).expandX();
        table.row();
        table.add().height(5);
        table.row();
        //If user data has been stored, the contuse button is viable.
        if (!UserPrefs.gdxPrefs.contains("tiledMap" + Globals.currentGameSave)) {
            btnContinue.setVisible(false);
        }

        VisTextButton btnLoad = new VisTextButton("Select Level");
        btnLoad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);

                if (Globals.levelSelectScene != null)
                    ((Screen) Globals.game.getScreen()).switchScene(Globals.levelSelectScene);
                else {
                    Globals.levelSelectScene = new LevelSelect((Screen) Globals.game.getScreen());
                    ((Screen) Globals.game.getScreen()).switchScene(Globals.levelSelectScene);
                }

            }
        });
        table.add(btnLoad).expandX();
        table.row();
        table.add().height(5);
        table.row();

        VisTextButton btnNew = new VisTextButton("New Game");
        //Call the newSave() method when the new button is pushed.
        btnNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                newSave();
            }
        });
        table.add(btnNew).expandX();
        table.row();
        table.add().height(5);
        table.row();

        VisTextButton btnLoadLevel = new VisTextButton("Load Custom Level");
        btnLoadLevel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                stage.addActor(fileChooser.fadeIn());
            }
        });
        table.add(btnLoadLevel).expandX();
        table.row();
        table.add().height(10);
        table.row();

        backButton = new VisTextButton("Back");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                ((Screen) Globals.game.getScreen()).switchScene(Globals.titleScreenScene);
                Globals.titleScreenScene.show();
            }
        });
        table.add(backButton).expandX();

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
        //If there's no User data, create it, and make the continue button viable.
        if (!UserPrefs.gdxPrefs.contains("tiledMap" + Globals.currentGameSave)) {

            UserPrefs.setLevel(Globals.currentGameSave, Finals.firstLevel);
            //IF there is User Data, however, create a dialog prompt asking the user if it's okay to overwrite their existing User data.
        } else {
            Dialogs.showConfirmDialog(stage, "Overwrite existing save?", null, new String[]{"Yes", "No"}, new String[]{"y", "n"}, new ConfirmDialogListener<String>(){
                @Override
                public void result(String result) {
                    if (result.equals("y")) {
                        UserPrefs.setLevel(Globals.currentGameSave, Finals.firstLevel);
                    }
                }
            });
        }
        btnContinue.setVisible(true);

    }

    /**
     * Set the current screen to the play screen
     */
    private void loadLevel() {
        Globals.game.setScreen(Globals.gameScreen);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
