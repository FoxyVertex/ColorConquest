package com.thefoxarmy.rainbowwarrior.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.thefoxarmy.rainbowwarrior.RainbowWarrior;

/**
 * Handles the Main menu for the game, as well as saving and loading player data.
 */
public class MenuScreen implements Screen {


    private RainbowWarrior game;
    private Stage mainMenu;
    private Skin skin;
    private Table table;
    private TextButton btnContinue;
    private Preferences prefs;
    //private TextButton btnBattle;
    //private TextButton btnQuit;

    /**
     * Sets up the title screen
     * @param game allows this screen to make calls to the player class for things like screen management.
     */
    public MenuScreen(RainbowWarrior game) {
        this.game = game;
        prefs = Gdx.app.getPreferences("User Data"); //Initialize the user preferences.

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        mainMenu = new Stage(new ScreenViewport());


        TextButton btnPlay = new TextButton("Play", skin, "default");
        btnPlay.setSize(200, 50);
        btnPlay.setPosition(Gdx.graphics.getWidth() / 2 - btnPlay.getWidth() / 2, Gdx.graphics.getHeight() - btnPlay.getHeight() - 40);


        table = new Table();
        table.setWidth(mainMenu.getWidth());
        table.setPosition(Gdx.graphics.getWidth() / 2, btnPlay.getY() - 50, Align.center | Align.top);

        TextButton btnNew = new TextButton("New Game", skin);
        //Call the newSave() method when the new button is pushed.
        btnNew.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                newSave();
            }
        });
        table.padTop(15);
        table.add(btnNew).pad(10);

        btnContinue = new TextButton("Continue Game", skin);
        btnContinue.setVisible(false);
        table.padTop(30);
        //If user data has been stored, the contuse button is viable.
        if (prefs.contains("Level")) {
            btnContinue.setVisible(true);
            prefs.flush();
        }
        table.add(btnContinue).pad(10);

        TextButton btnLoad = new TextButton("Load Save", skin);
        table.padTop(30);
        table.add(btnLoad).pad(10);
        //When the play button is clicked, load the play options options table.
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                mainMenu.addActor(table);
            }
        });
        //When the contiue button is clicked, start a new instance of the play screen and load the level.
        btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                loadLevel(prefs.getString("Level"));
            }
        });

        mainMenu.addActor(btnPlay);
        Gdx.input.setInputProcessor(mainMenu); //Set the stage as libGDX's input adapter.
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        mainMenu.act();
        mainMenu.draw();

    }

    private void newSave() {
        //If there's no User data, create it, and make the continue button viable.
        if (!prefs.contains("Level")) {

            prefs.putString("Level", "levels/test.tmx");
            prefs.flush();
            //IF there is User Data, however, create a dialog prompt asking the user if it's okay to overwrite their existing User data.
        } else {
            final Dialog dl = new Dialog("Overwrite Existing save?", skin);

            Table btnTable = new Table();

            TextButton btnYes = new TextButton("Ja", skin);
            //If the user agrees, their userdata will be overwritten, and the dialog will disappear
            btnYes.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent i, float x, float y) {
                    prefs.putString("Level", "test.tmx").flush();
                    dl.setVisible(false);
                }
            });
            //If the user refuses, the dialog will dissapear
            TextButton btnNo = new TextButton("NEIN", skin);
            btnNo.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent i, float x, float y) {
                    dl.setVisible(false);
                }
            });
            btnTable.add(btnYes);
            btnTable.add(btnNo);
            dl.add(btnTable);
            dl.show(mainMenu);
            mainMenu.addActor(dl);
        }
        btnContinue.setVisible(true);

    }

    /**
     * Set the current screen to a new instance of a play screen
     * @param level string to load the TMX.
     */
    private void loadLevel(String level) {
        game.setScreen(new PlayScreen(game, prefs.getString("Level")));
    }

    @Override
    public void resize(int width, int height) {
        mainMenu.getViewport().update(width, height, true);
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
        mainMenu.dispose();
    }
}
