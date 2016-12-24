package com.thefoxarmy.rainbowwarrior.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.thefoxarmy.rainbowwarrior.Finals;
import com.thefoxarmy.rainbowwarrior.Globals;
import com.thefoxarmy.rainbowwarrior.managers.Levels;

import java.util.ArrayList;
import java.util.List;

import static com.thefoxarmy.rainbowwarrior.tools.Utilities.*;

/**
 * Created by aidan on 12/24/2016.
 */

public class Block extends Entity {

    private MapObject mapObject;
    public static List<Block> blocks = new ArrayList<Block>();
    private TextureRegion theFrigginTextureRegion;

    public Block(MapObject object, short categoryBit) {
        super(new Vector2(object.getProperties().get("x", float.class), object.getProperties().get("y", float.class)), -1f);

        this.mapObject = object;

        MapProperties objectProps = mapObject.getProperties();
        Color color = null;

        if (objectProps.get("red", Float.class) != null) {
            if (objectProps.get("alpha", Float.class) == null || objectProps.get("blue", Float.class) == null || objectProps.get("green", Float.class) == null) {
                Gdx.app.log("Error from Block class", "Something is wrong with the tile in level with index " + Levels.currentLevel + " at coords " + objectProps.get("x", float.class) / 16 + ", " + objectProps.get("y", float.class) / 16 + " and with id " + objectProps.get("ID", int.class) + ".");
                throw new AssertionError();
            }
            color = new Color(
                    map(objectProps.get("red", Float.class), 0, 1, 0, 1),
                    map(objectProps.get("green", Float.class), 0, 1, 0, 1),
                    map(objectProps.get("blue", Float.class), 0, 1, 0, 1),
                    objectProps.get("alpha", Float.class)
            );
        }

        if (color != null) {
            tintTexture(color);
            blocks.add(this);
        }


        PolygonShape polygon = new PolygonShape();
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rect.getX() + rect.getWidth() / 2) / Finals.PPM, (rect.getY() + rect.getHeight() / 2) / Finals.PPM);
        Body body = Globals.gameScreen.getWorld().createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        polygon.setAsBox((rect.getWidth() / 2) / Finals.PPM, (rect.getHeight() / 2) / Finals.PPM);
        fdef.shape = polygon;
        fdef.filter.categoryBits = categoryBit;
        body.createFixture(fdef);
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setUserData(color);
        }
    }

    private void tintTexture(Color color) {

        Pixmap colorFill = new Pixmap((int)Math.floor(mapObject.getProperties().get("width", float.class)), (int)Math.floor(mapObject.getProperties().get("height", float.class)), Pixmap.Format.RGB888);
        colorFill.setColor(color);
        colorFill.fill();

        this.setAlpha(color.a);
        this.setRegion(new TextureRegion(new Texture(colorFill)));
        this.setBounds(this.getX()/Finals.PPM, this.getY()/Finals.PPM, colorFill.getWidth()/Finals.PPM, colorFill.getHeight()/Finals.PPM);
    }

    @Override
    public void tick(float delta) {
        //this.setRegion(theFrigginTextureRegion);
    }

    @Override
    public void die(Entity killedBy) {

    }

    @Override
    public void damage(Entity damagedBy, float damageAmount) {

    }

    @Override
    public void dispose() {
        this.getTexture().dispose();
    }
}
