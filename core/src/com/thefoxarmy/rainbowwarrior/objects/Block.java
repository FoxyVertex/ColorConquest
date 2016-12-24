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
import com.thefoxarmy.rainbowwarrior.FinalGlobals;

/**
 * Created by seth on 12/22/2016.
 */

public class Block {

    public Block(Boolean editable, Body physicalBody, MapObject object, TiledMap map) {


        TiledMapTileLayer.Cell coveredCell = getCell(physicalBody, map);
        MapProperties objectProps = object.getProperties();
        Color tileColor = Color.BLACK;

        if (objectProps.get("red", Float.class) != null) {
            tileColor = new Color(
                    objectProps.get("red", Float.class),
                    objectProps.get("green", Float.class),
                    objectProps.get("blue", Float.class),
                    objectProps.get("alpha", Float.class)
            );
        }
        if (coveredCell == null) Gdx.app.log("aaa", "" + objectProps.get("x", int.class));
        TiledMapTile currentTile = coveredCell.getTile();
        currentTile.setTextureRegion(tintTexture(currentTile.getTextureRegion(), tileColor));
        coveredCell.setTile(currentTile);
        for (Fixture fixture : physicalBody.getFixtureList()) {
            fixture.setUserData(tileColor);
        }

    }

    private TiledMapTileLayer.Cell getCell(Body body, TiledMap map) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * FinalGlobals.PPM / 16), (int) (body.getPosition().y * FinalGlobals.PPM / 16));
    }

    private TextureRegion tintTexture(TextureRegion tex, Color color) {

        Pixmap colorFill = new Pixmap(tex.getRegionWidth(), tex.getRegionHeight(), Pixmap.Format.RGB888);
        colorFill.setColor(color);
        colorFill.fill();

        return new TextureRegion(new Texture(colorFill));
    }

}

