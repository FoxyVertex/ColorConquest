package com.thefoxarmy.rainbowwarrior.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.thefoxarmy.rainbowwarrior.FinalGlobals;
import com.thefoxarmy.rainbowwarrior.objects.Block;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;

/**
 * This class reads object layers from the currently loaded tiled map and creates box2d fixtures for them in order to create collision for the player.
 */
public class WorldPhysicsCreator {
    /**
     * Loops though each layer and creates fixtures that the player can collide with, and sets up TriggerPoints.
     * @param screen this instance is used to get the current tiled map.
     */
    private PolygonShape polygon;

    public WorldPhysicsCreator(World world, TiledMap map) {
        polygon = new PolygonShape();
        //For every rectangular object in the "blocks" object layer of the tile map, initialize a rectangle to create a physical fixture.
        for (MapObject object : map.getLayers().get("blocks").getObjects()) {
            new Block(false, initializeRect(world, FinalGlobals.BLOCK_BIT, object), object, map);
        }
        for (MapObject object : map.getLayers().get("EditableBlocks").getObjects()) {
            new Block(true, initializeRect(world, FinalGlobals.BLOCK_BIT, object), object, map);
        }
        //Generate fixtures for the endpoints in the triggerPoints object layer of the tiled map so that the player can collide with it to go to the next tiledMap.
        initializeRect(world, FinalGlobals.END_LEVEL_BIT, map.getLayers().get("triggerPoints").getObjects().get("EndPoint"));

    }

    /**
     * This method creates a fixture based on a specified tiledMap object. object
     * @param world needed for accessing the current tiledMap loaded and b2dWorld.
     * @param categoryBit The "type" of fixture being created. Used in collsions.
     * @param object The map object that will be used to create the rectangular fixtures.
     */
    private Body initializeRect(World world, short categoryBit, MapObject object) {
        Rectangle rect = ((RectangleMapObject) object).getRectangle();
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((rect.getX() + rect.getWidth() / 2) / FinalGlobals.PPM, (rect.getY() + rect.getHeight() / 2) / FinalGlobals.PPM);
        Body body = world.createBody(bdef);
        FixtureDef fdef = new FixtureDef();
        polygon.setAsBox((rect.getWidth() / 2) / FinalGlobals.PPM, (rect.getHeight() / 2) / FinalGlobals.PPM);
        fdef.shape = polygon;
        fdef.filter.categoryBits = categoryBit;
        body.createFixture(fdef);
        return body;
    }

}
