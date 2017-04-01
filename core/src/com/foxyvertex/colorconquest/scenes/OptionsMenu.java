package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.managers.UserPrefs;
import com.foxyvertex.colorconquest.screens.Screen;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by aidan on 11/28/2016.
 */

public class OptionsMenu extends Scene {

    private CheckBox   soundEnabledCheckBox;
    private CheckBox   musicEnabledCheckBox;
    private TextButton backButton;

    private Screen screen;

    public OptionsMenu(final Screen screen) {
        super(screen);
        this.screen = screen;
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        musicEnabledCheckBox = new CheckBox("Music Enabled", Assets.guiSkin);
        musicEnabledCheckBox.setChecked(UserPrefs.isSoundEnabled());
        musicEnabledCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                UserPrefs.setSoundEnabled(musicEnabledCheckBox.isChecked());
            }
        });
        table.add(musicEnabledCheckBox).expandX();
        table.row();
        soundEnabledCheckBox = new CheckBox("Sound Enabled", Assets.guiSkin);
        soundEnabledCheckBox.setChecked(UserPrefs.isSoundEnabled());
        soundEnabledCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                Assets.playSound(Assets.clickSound);
                UserPrefs.setSoundEnabled(soundEnabledCheckBox.isChecked());
            }
        });
        table.add(soundEnabledCheckBox).expandX();
        table.row();
        backButton = new TextButton("Back", Assets.guiSkin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                ((Screen) Globals.game.getScreen()).switchScene(Globals.titleScreenScene);
                Globals.titleScreenScene.show();
            }
        });
        table.add(backButton).expandX();
        stage.addActor(table);
    }

    @Override
    public void dispose() {

    }

    public void show() {
        input.setInputProcessor(stage);
    }

    public void tick(float delta) {
        stage.act();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
