package com.thefoxarmy.rainbowwarrior.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.thefoxarmy.rainbowwarrior.Globals;
import com.thefoxarmy.rainbowwarrior.screens.PlayScreen;

/**
 * Sets up all of the physics for the world
 */
public class WorldPhysicsCreator {
    /**
     * Sets up all of the physics for the world
     * @param screen the play screen is used for doing things that this class needs tp do
     */
    public WorldPhysicsCreator(PlayScreen screen) {
        PolygonShape polygon = new PolygonShape();
        for (MapObject object : screen.level.getLayers().get("blocks").getObjects()) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / Globals.PPM, (rect.getY() + rect.getHeight() / 2) / Globals.PPM);
            Body body = screen.getWorld().createBody(bdef);
            FixtureDef fdef = new FixtureDef();
            polygon.setAsBox((rect.getWidth() / 2) / Globals.PPM, (rect.getHeight() / 2) / Globals.PPM);
            fdef.shape = polygon;
            body.createFixture(fdef);
        }
    }

}
