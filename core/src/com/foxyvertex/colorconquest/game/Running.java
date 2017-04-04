package com.foxyvertex.colorconquest.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.entities.EntityController;
import com.foxyvertex.colorconquest.tools.Drawable;
import com.foxyvertex.colorconquest.tools.Utilities;
import com.foxyvertex.colorconquest.tools.WorldPhysicsContactListener;

import java.lang.reflect.Method;

import static com.badlogic.gdx.Gdx.input;

/**
 * Created by aidan on 1/23/17.
 */

public class Running extends GameState {

    public Array<Drawable> drawables        = new Array<Drawable>( );
    public Array<Method>   anonymousMethods = new Array<Method>( );
    public boolean         hasWorldStepped  = false;

    @Override
    public void update(float delta) {
        if (input.isKeyJustPressed( Input.Keys.ESCAPE )) {
            Globals.gameMan.switchState( GameManager.GameState.PAUSED );
            return;
        }

        Globals.gameMan.timeSinceStartLevel += delta;
        hasWorldStepped = false;
        Globals.gameMan.world.step( 1 / 60f, 8, 3 );
        hasWorldStepped = true;
        //Destroy every body waiting around to be destroyed

        for (Body body : WorldPhysicsContactListener.deadBodies) {
            Globals.gameMan.world.destroyBody( body );
            WorldPhysicsContactListener.deadBodies.removeValue( body, false );
        }

        Globals.gameMan.cam.position.x = Globals.gameMan.player.body.getPosition( ).x;
        Globals.gameMan.cam.position.y = Globals.gameMan.player.body.getPosition( ).y;
        MapProperties levelProps     = Globals.gameMan.tiledMap.getProperties( );
        int           mapPixelWidth  = levelProps.get( "width", Integer.class ) * levelProps.get( "tilewidth", Integer.class );
        int           mapPixelHeight = levelProps.get( "height", Integer.class ) * levelProps.get( "tileheight", Integer.class );
        Globals.gameMan.cam.position.x = Utilities.clamp( Globals.gameMan.player.body.getPosition( ).x, Globals.gameMan.cam.viewportWidth / 2, (mapPixelWidth / Finals.PPM) - (Globals.gameMan.cam.viewportWidth / 2) );
        Globals.gameMan.cam.position.y = Utilities.clamp( Globals.gameMan.player.body.getPosition( ).y, Globals.gameMan.cam.viewportHeight / 2, (mapPixelHeight / Finals.PPM) - (Globals.gameMan.cam.viewportHeight / 2) );
        //Render all the entities
        EntityController.tick( delta );

        Globals.gameMan.cam.update( );
        Globals.gameMan.mapRenderer.setView( Globals.gameMan.cam );
        Globals.hudScene.stage.act( );
    }

    @Override
    public void render() {
        Globals.gameMan.mapRenderer.render( );
        Globals.gameMan.b2dRenderer.render( Globals.gameMan.world, Globals.gameMan.cam.combined );
        Globals.game.batch.setProjectionMatrix( Globals.gameMan.cam.combined );
        Globals.game.batch.begin( );
        //Render all the entities
        EntityController.render( Globals.game.batch );

        Globals.game.batch.end( );
        Globals.game.batch.setProjectionMatrix( Globals.hudScene.stage.getCamera( ).combined );
        Globals.hudScene.stage.draw( );
        for (Drawable drawable : drawables) {
            drawable.draw( );
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void dispose() {

    }
}
