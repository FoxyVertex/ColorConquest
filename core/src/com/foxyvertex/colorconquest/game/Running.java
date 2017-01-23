package com.foxyvertex.colorconquest.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapProperties;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.entities.Block;
import com.foxyvertex.colorconquest.screens.GameScreen;
import com.foxyvertex.colorconquest.tools.Utilities;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by aidan on 1/23/17.
 */

public class Running extends GameState {

    @Override
    public void update(float delta) {
        if (input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Globals.gameMan.switchState(GameManager.GameState.PAUSED);
            return;
        }

        Globals.gameMan.timeSinceStartLevel += delta;
        Globals.gameMan.world.step(1 / 60f, 6, 2);
        Globals.gameMan.cam.position.x = Globals.gameMan.player.body.getPosition().x;
        Globals.gameMan.cam.position.y = Globals.gameMan.player.body.getPosition().y;
        MapProperties levelProps = Globals.gameMan.tiledMap.getProperties();
        int mapPixelWidth = levelProps.get("width", Integer.class) * levelProps.get("tilewidth", Integer.class);
        int mapPixelHeight = levelProps.get("height", Integer.class) * levelProps.get("tileheight", Integer.class);
        Globals.gameMan.cam.position.x = Utilities.clamp(Globals.gameMan.player.body.getPosition().x, Globals.gameMan.cam.viewportWidth / 2, (mapPixelWidth / Finals.PPM) - (Globals.gameMan.cam.viewportWidth / 2));
        Globals.gameMan.cam.position.y = Utilities.clamp(Globals.gameMan.player.body.getPosition().y, Globals.gameMan.cam.viewportHeight / 2, (mapPixelHeight / Finals.PPM) - (Globals.gameMan.cam.viewportHeight / 2));

        Globals.gameMan.player.tick(delta);
        Globals.gameMan.cam.update();
        Globals.gameMan.mapRenderer.setView(Globals.gameMan.cam);
        Globals.hudScene.stage.act();
        for (Block block : Block.blocks) block.tick(delta);
    }

    @Override
    public void render() {
        Globals.gameMan.mapRenderer.render();
        Globals.gameMan.b2dRenderer.render(Globals.gameMan.world, Globals.gameMan.cam.combined);
        Globals.game.batch.setProjectionMatrix(Globals.gameMan.cam.combined);
        Globals.game.batch.begin();
        for (Block block : Block.blocks) block.draw(Globals.game.batch);
        Globals.gameMan.player.draw(Globals.game.batch);
        Globals.gameMan.player.render(Globals.game.batch);
        Globals.game.batch.end();
        Globals.game.batch.setProjectionMatrix(Globals.hudScene.stage.getCamera().combined);
        Globals.hudScene.stage.draw();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

}
