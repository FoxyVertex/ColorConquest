package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.foxyvertex.colorconquest.ColorConquest;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.managers.Levels;
import com.foxyvertex.colorconquest.managers.UserPrefs;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

/**
 * Created by aidan on 4/2/17.
 */

public class LevelSelect extends Scene {
    public LevelSelect(Screen screen) {
        super(screen);
        VisTable table = new VisTable();
        table.setFillParent(true);
        table.center();
        for (int i = 0; i < Levels.levels.size(); i++) {
            Levels.Level lvl = Levels.levels.get(i);
            table.row();
            table.add(new LevelSelectWidget(lvl.name, UserPrefs.getLevel(Globals.currentGameSave) < i, i)).height(100).width(300).expandX();
            table.row();
            table.add().height(10);
        }
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void tick(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
    private class LevelSelectWidget extends VisWindow {

        public LevelSelectWidget(String name, boolean locked, final int index) {
            super(name);
            setHeight(400);
            setWidth(500);
            VisTable table = new VisTable();
            table.center();

            VisTextButton playBtn = new VisTextButton("Play");
            playBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent i, float x, float y) {
                    Assets.playSound(Assets.clickSound);
                    Levels.Level lvl = new Levels.Level();
                    lvl.index = Levels.levels.get(index).index;
                    lvl.name = Levels.levels.get(index).name;
                    lvl.path = Levels.levels.get(index).path;
                    lvl.hasCutscene = Levels.levels.get(index).hasCutscene;
                    lvl.nextLevel = null;

                    Globals.gameMan.playLevel(lvl);
                    Globals.game.setScreen(Globals.gameScreen);
                }
            });
            if (locked) {
                table.add(new VisLabel("This Level is Locked!"));
            } else {
                table.add(playBtn);
            }
            add(table);
        }
    }
}
