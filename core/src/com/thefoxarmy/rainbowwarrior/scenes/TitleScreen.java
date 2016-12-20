package com.thefoxarmy.rainbowwarrior.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.FinalGlobals;
import com.thefoxarmy.rainbowwarrior.managers.Assets;
import com.thefoxarmy.rainbowwarrior.managers.UserPrefs;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;
import com.thefoxarmy.rainbowwarrior.screens.MenuScreen;
import com.thefoxarmy.rainbowwarrior.screens.Screen;

/**
 * Created by aidan on 11/27/2016.
 */

public class TitleScreen extends Scene {

    //Scene2D widgets
    private Table table;

    /**
     * This sets up the pause menu's stage and lets it be amazing
     */
    public TitleScreen(final Screen screen){
        super(screen);

        table = new Table();
        table.center();
        table.setFillParent(true);
        //When the play button is clicked, load the play options options table.
        TextButton btnPlay = new TextButton("Play", Assets.guiSkin, "default");
        btnPlay.setSize(200, 50);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                if (DynamicGlobals.playMenuScene != null)
                    ((Screen)DynamicGlobals.game.getScreen()).switchScene(DynamicGlobals.playMenuScene);
                else {
                    DynamicGlobals.playMenuScene = new PlayMenu((Screen)DynamicGlobals.game.getScreen());
                    ((Screen)DynamicGlobals.game.getScreen()).switchScene(DynamicGlobals.playMenuScene);
                }
            }
        });
        table.add(btnPlay);
        table.row();
        TextButton btnOptions = new TextButton("Options", Assets.guiSkin, "default");
        btnOptions.setSize(200, 50);
        btnOptions.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                if (DynamicGlobals.optionsMenuScreen != null)
                    ((Screen)DynamicGlobals.game.getScreen()).switchScene(DynamicGlobals.optionsMenuScreen);
                else {
                    DynamicGlobals.optionsMenuScreen = new OptionsMenu((Screen)DynamicGlobals.game.getScreen());
                    ((Screen)DynamicGlobals.game.getScreen()).switchScene(DynamicGlobals.optionsMenuScreen);
                }
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
        stage.act();
    }

    public void tick(float delta) {
        stage.act();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

}
