package com.thefoxarmy.rainbowwarrior.screens;

import com.badlogic.gdx.Gdx;
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

public class MenuScreen implements Screen {


    private RainbowWarrior game;
    private Stage mainMenu;
    private Skin skin;
    private Table table;
    private TextButton btnContinue;
    //private TextButton btnBattle;
    //private TextButton btnQuit;

    public MenuScreen(RainbowWarrior game) {
        this.game = game;

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        mainMenu = new Stage(new ScreenViewport());


        TextButton btnPlay = new TextButton("Play", skin, "default");
        btnPlay.setSize(200, 50);
        btnPlay.setPosition(Gdx.graphics.getWidth() / 2 - btnPlay.getWidth() / 2, Gdx.graphics.getHeight() - btnPlay.getHeight() - 40);


        table = new Table();
        table.setWidth(mainMenu.getWidth());
        table.setPosition(Gdx.graphics.getWidth() / 2, btnPlay.getY() - 50, Align.center | Align.top);

        TextButton btnNew = new TextButton("New Game", skin);
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
        if (Gdx.files.local("save.json").exists())
            btnContinue.setVisible(true);
        table.add(btnContinue).pad(10);

        TextButton btnLoad = new TextButton("Load Save", skin);
        table.padTop(30);
        table.add(btnLoad).pad(10);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                mainMenu.addActor(table);
            }
        });

        btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                loadLevel(new JsonReader().parse(Gdx.files.local("save.json")).getString("level"));
            }
        });

        mainMenu.addActor(btnPlay);
        Gdx.input.setInputProcessor(mainMenu);
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
        final FileHandle newSave = Gdx.files.local("save.json");
        final String lvl = "\"level\": \"levels/test.tmx\"";
        if (!newSave.exists()) {
            newSave.writeString("{\n" + lvl + "\n}", false);
        } else {
            final Dialog dl = new Dialog("Overwrite Existing save?", skin);

            Table btnTable = new Table();

            TextButton btnYes = new TextButton("Ja", skin);
            btnYes.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent i, float x, float y) {
                    newSave.writeString("{\n" + lvl + "\n}", false);
                    dl.setVisible(false);
                }
            });

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

    private void loadLevel(String level) {
        game.setScreen(new PlayScreen(game, level));
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
