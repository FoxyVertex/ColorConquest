package com.thefoxarmy.rainbowwarrior.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.thefoxarmy.rainbowwarrior.Finals;
import com.thefoxarmy.rainbowwarrior.managers.Levels;

import static com.thefoxarmy.rainbowwarrior.tools.Utilities.map;

/**
 * Created by seth on 12/22/2016.
 */

public class Block {

    public Block(Boolean editable, Body physicalBody, MapObject object, TiledMap map) {


        TiledMapTileLayer.Cell coveredCell = getCell(physicalBody, map);
        MapProperties objectProps = object.getProperties();
        Color tileColor = null;

        if (objectProps.get("red", Float.class) != null) {
            if (objectProps.get("alpha", Float.class) == null) {
                Gdx.app.log("Error from Block class", "Something is wrong with the tile in level with index " + Levels.currentLevel + " at coords " + objectProps.get("x", float.class) + ", " + objectProps.get("y", float.class) + " and with id " + objectProps.get("ID", int.class) + ".");
            }
            tileColor = new Color(
                    map(objectProps.get("red", Float.class), 0, 1, 0, 1),
                    map(objectProps.get("green", Float.class), 0, 1, 0, 1),
                    map(objectProps.get("blue", Float.class), 0, 1, 0, 1),
                    objectProps.get("alpha", Float.class)
            );
        }
        if (coveredCell == null)
            Gdx.app.log("Error from Block class", "Something is wrong with the tile in level with index " + Levels.currentLevel + " at coords " + objectProps.get("x", float.class)/16 + ", " + objectProps.get("y", float.class)/16 + " and with id " + objectProps.get("ID", int.class) + ".");
        assert coveredCell != null;
        TiledMapTile currentTile = coveredCell.getTile();
        if (currentTile == null)
            Gdx.app.log("Error from Block class", "Something is wrong with the tile in level with index " + Levels.currentLevel + " at coords " + objectProps.get("x", float.class)/16 + ", " + objectProps.get("y", float.class)/16 + " and with id " + objectProps.get("ID", int.class) + ".");
        assert currentTile != null;
        if (tileColor != null)
            currentTile.setTextureRegion(tintTexture(currentTile.getTextureRegion(), tileColor));
        else {
            tileColor = new Color(0.0f, 0.0f, 0.0f, 0.0f);
        }


        coveredCell.setTile(currentTile);
        for (Fixture fixture : physicalBody.getFixtureList()) {
            fixture.setUserData(tileColor);
        }

    }

    private TiledMapTileLayer.Cell getCell(Body body, TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * Finals.PPM / 16), (int) (body.getPosition().y * Finals.PPM / 16));
    }

    private TextureRegion tintTexture(TextureRegion tex, Color color) {

        Pixmap colorFill = new Pixmap(tex.getRegionWidth(), tex.getRegionHeight(), Pixmap.Format.RGB888);
        colorFill.setColor(color);
        colorFill.fill();

        return new TextureRegion(new Texture(colorFill));
    }

}

