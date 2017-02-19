package com.foxyvertex.colorconquest.stages;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.foxyvertex.colorconquest.manager.Assets;
import com.foxyvertex.colorconquest.manager.UserPrefs;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by aidan on 11/28/2016.
 */

public class OptionsMenu extends UIStage {

    private CheckBox soundEnabledCheckBox;
    private CheckBox musicEnabledCheckBox;
    private TextButton backButton;

    public OptionsMenu() {
        super();
        Table table = new Table();
        table.center();
        table.setFillParent(true);

        musicEnabledCheckBox = new CheckBox("Music Enabled", Assets.guiSkin);
        musicEnabledCheckBox.setChecked(UserPrefs.isSoundEnabled());
        musicEnabledCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                // TODO: 2/18/2017 set the music enabled user prefs
            }
        });
        table.add(musicEnabledCheckBox).expandX();
        table.row();
        soundEnabledCheckBox = new CheckBox("Sound Enabled", Assets.guiSkin);
        soundEnabledCheckBox.setChecked(UserPrefs.isSoundEnabled());
        soundEnabledCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                UserPrefs.setSoundEnabled(soundEnabledCheckBox.isChecked());
            }
        });
        table.add(soundEnabledCheckBox).expandX();
        table.row();
        backButton = new TextButton("Back", Assets.guiSkin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent i, float x, float y) {
                // TODO: 2/18/2017 go back to previous menu
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
