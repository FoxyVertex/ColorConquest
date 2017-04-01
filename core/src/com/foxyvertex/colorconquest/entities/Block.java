package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.managers.Levels;
import com.foxyvertex.colorconquest.tools.Utilities;

import java.util.ArrayList;
import java.util.List;

import static com.foxyvertex.colorconquest.tools.Utilities.map;

/**
 * Created by aidan on 12/24/2016.
 */

public class Block extends SpriteBody {

    public        boolean     rainbow = false;
    Vector2 spawnPoint;
    private MapObject mapObject;
    private int iForRainbow = 0;
    private Color rainbowColor;

    public Block(MapObject object) {
        super(new Vector2(object.getProperties().get("x", float.class), object.getProperties().get("y", float.class)));
        this.mapObject = object;

        def();

        MapProperties objectProps = mapObject.getProperties();
        Color         color       = new Color(0, 0, 0, 0);

        if (objectProps.get("red", Float.class) != null) {
            if (objectProps.get("alpha", Float.class) == null || objectProps.get("blue", Float.class) == null || objectProps.get("green", Float.class) == null) {
                Gdx.app.log("Error from Block class", "Something is wrong with the tile in level with index " + Levels.currentLevel + " at coords " + objectProps.get("x", float.class) / 16 + ", " + objectProps.get("y", float.class) / 16 + " and with id " + objectProps.get("ID", int.class) + ".");
                throw new AssertionError();
            }
            color = new Color(
                    map(objectProps.get("red", Float.class), 0, 1, 0, 255),
                    map(objectProps.get("green", Float.class), 0, 1, 0, 255),
                    map(objectProps.get("blue", Float.class), 0, 1, 0, 255),
                    objectProps.get("alpha", Float.class)
            );

        }
        this.color = color;
        tintTexture();
    }

    protected BodyDef.BodyType bodyType() {
        return BodyDef.BodyType.StaticBody;
    }

    protected short CATIGORY_BIT() {
        return Finals.BLOCK_BIT;
    }

    protected short MASKED_BIT() {
        return Finals.EVERYTHING_BIT;
    }

    public void def() {
        PolygonShape polygon = new PolygonShape();
        Rectangle    rect    = ((RectangleMapObject) mapObject).getRectangle();
        polygon.setAsBox((rect.getWidth() / 2) / Finals.PPM, (rect.getHeight() / 2) / Finals.PPM);

        super.def(polygon, new Vector2((rect.getX() + rect.getWidth() / 2) / Finals.PPM, (rect.getY() + rect.getHeight() / 2) / Finals.PPM));
        setPosition((rect.getX()) / Finals.PPM, (rect.getY()) / Finals.PPM);
    }

    @Override
    public void tick(float delta) {
        if (rainbow) {
            if (rainbowColor == null) rainbowColor = new Color();
            float frequency = 0.3f;
            rainbowColor.r = Utilities.map((float) Math.sin(frequency * iForRainbow + 0) * 127 + 128, 0, 255, 0, 1);
            rainbowColor.g = Utilities.map((float) Math.sin(frequency * iForRainbow + 2) * 127 + 128, 0, 255, 0, 1);
            rainbowColor.b = Utilities.map((float) Math.sin(frequency * iForRainbow + 4) * 127 + 128, 0, 255, 0, 1);
            rainbowColor.a = 1.0f;
            iForRainbow++;

            setColor(rainbowColor);
        }
    }

    public void dispose() {
        this.getTexture().dispose();

    }

    public void tintTexture() {
        Pixmap colorFill = new Pixmap((int) Math.floor(mapObject.getProperties().get("width", float.class)), (int) Math.floor(mapObject.getProperties().get("height", float.class)), Pixmap.Format.RGBA8888);
        colorFill.setColor(color);
        colorFill.fill();

        this.setRegion(new TextureRegion(new Texture(colorFill)));
        this.setBounds(this.getX(), this.getY(), colorFill.getWidth() / Finals.PPM, colorFill.getHeight() / Finals.PPM);
    }

}