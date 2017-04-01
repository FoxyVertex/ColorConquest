package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.foxyvertex.colorconquest.screens.Screen;

import java.nio.file.Path;
import java.nio.file.Paths;

//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static java.util.stream.Nodes.collect;

/**
 * Created by seth on 3/31/2017.
 */

class LoadLevel extends Scene {

    Table table;


    public LoadLevel(Screen screen) {
        super(screen);
        table = new Table();
        table.center();
        table.setFillParent(true);

         Path path = Paths.get(Gdx.files.getLocalStoragePath());
//
//        try {
//            List<Path> files = Files.walk(path)
//                    .filter("" final boolean b = !Files.isDirectory("") &&
//                    "".toLowerCase().endsWith(".tmx");->!Files.isDirectory("") &&
//                    "".toLowerCase().endsWith(".tmx"))
//                    .collect(Collectors.toList());
//        }
//        catch (IOException ex) {
//            ex.printStackTrace();
//        }
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
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
