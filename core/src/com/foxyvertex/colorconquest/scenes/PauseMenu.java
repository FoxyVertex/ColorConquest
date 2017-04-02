package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.game.GameManager;
import com.foxyvertex.colorconquest.managers.Assets;

/**
 * Created by aidan on 11/26/2016.
 * <p>
 * This scene allows the game to have a pause menu.
 */

public class PauseMenu extends Scene {

    Table table;
    //Scene2D widgets
    private TextButton btnResume;
    private TextButton btnQuit;

    /**
     * This sets up the pause menu's stage and lets it be amazing
     *
     * @param screen the screen is used to be passed to super
     */
    public PauseMenu(final Screen screen) {
        super(screen);

        btnResume = new TextButton("Resume", Assets.guiSkin);
        btnQuit = new TextButton("Quit", Assets.guiSkin);
        btnResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Globals.gameMan.switchState(GameManager.GameState.RUNNING);
                Assets.playSound(Assets.clickSound);
            }
        });
        btnQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Globals.game.setScreen(Globals.menuScreen);
                Assets.playSound(Assets.clickSound);
                Gdx.input.setInputProcessor(Globals.menuScreen.currentScene.stage);
                Assets.playMusic(Assets.menuMusic);
            }
        });

        table = new Table();
        table.top();
        table.setFillParent(true);
        table.add(btnResume).expandX().padTop(50);
        table.row();
        table.add(btnQuit).expandX().padTop(10);

        Pixmap background = new Pixmap((int) stage.getWidth(), (int) stage.getHeight(), Pixmap.Format.RGBA8888);

        background.setColor(new Color(0f, 0f, 0f, 0.5f));
        background.fill();

        final Texture image = new Texture(background);
        Drawable backgroundD = new Drawable() {

            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                Globals.game.batch.draw(image, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }

            @Override
            public float getLeftWidth() {
                return 0;
            }

            @Override
            public void setLeftWidth(float leftWidth) {

            }

            @Override
            public float getRightWidth() {
                return 0;
            }

            @Override
            public void setRightWidth(float rightWidth) {

            }

            @Override
            public float getTopHeight() {
                return 0;
            }

            @Override
            public void setTopHeight(float topHeight) {

            }

            @Override
            public float getBottomHeight() {
                return 0;
            }

            @Override
            public void setBottomHeight(float bottomHeight) {

            }

            @Override
            public float getMinWidth() {
                return 0;
            }

            @Override
            public void setMinWidth(float minWidth) {

            }

            @Override
            public float getMinHeight() {
                return 0;
            }

            @Override
            public void setMinHeight(float minHeight) {

            }
        };
        table.setBackground(backgroundD);

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
