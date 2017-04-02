package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.managers.UserPrefs;
import com.foxyvertex.colorconquest.screens.Screen;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by aidan on 11/28/2016.
 */

public class OptionsMenu extends Scene {

    private CheckBox   soundEnabledCheckBox;
    private CheckBox   musicEnabledCheckBox;

    private VisSlider masterVolumeSlider;
    private VisSlider musicVolumeSlider;
    private VisSlider soundVolumeSlider;
    private TextButton backButton;


    public OptionsMenu(final Screen screen) {
        super(screen);
        VisTable table = new VisTable();
        table.center();
        table.setFillParent(true);

        VisLabel masterLabel = new VisLabel("Master Volume");
        masterVolumeSlider = new VisSlider(0, 100, 1, false);
        masterVolumeSlider.setValue(UserPrefs.getMasterVolume());
        masterVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UserPrefs.setMasterVolume(masterVolumeSlider.getValue());
                Assets.playSound(Assets.clickSound);
            }
        });
        table.add(masterLabel);
        table.add(masterVolumeSlider).expandX();
        table.row();

        VisLabel musicLabel = new VisLabel("Music Volume");
        musicVolumeSlider = new VisSlider(0, 100, 1, false);
        musicVolumeSlider.setValue(UserPrefs.getMasterVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UserPrefs.setMusicVolume(musicVolumeSlider.getValue());
                Assets.playSound(Assets.clickSound);
            }
        });
        table.add(musicLabel);
        table.add(musicVolumeSlider).expandX();
        table.row();

        VisLabel soundLabel = new VisLabel("Sound Volume");
        soundVolumeSlider = new VisSlider(0, 100, 1, false);
        soundVolumeSlider.setValue(UserPrefs.getMasterVolume());
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                UserPrefs.setSoundVolume(soundVolumeSlider.getValue());
                Assets.playSound(Assets.clickSound);
            }
        });
        table.add(soundLabel);
        table.add(soundVolumeSlider).expandX();
        table.row();
        backButton = new TextButton("Back", Assets.guiSkin);
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
