package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;

import net.dermetfan.gdx.scenes.scene2d.Scene2DUtils;

/**
 * Created by aidan on 11/26/2016.
 * <p>
 * This class represents the heads up display and is currently housing a placeholder for it.
 */

public class Hud extends Scene {

    public  Image  colorIndicator;
    private Pixmap colorIndicatorDrawer;
    private Image  healthBar;
    private Pixmap healthBarDrawer;
    private Table  bottomHud;
    private Table  topHud;
    private Table tables;
    /**
     * The constructor sets up the stage.
     *
     * @param screen the screen is passed to super
     */
    public Hud(final Screen screen) {
        super( screen );


        tables = new Table( Assets.guiSkin );
        tables.setFillParent( true );

        topHud = new Table( Assets.guiSkin );
        tables.addActor( topHud );
        tables.row();
        //Bottom of the HUD that displays color information
        bottomHud = new Table( Assets.guiSkin );

        //Table Positioning stuff doesn't work yet -_-
        bottomHud.row( );
        bottomHud.pad( 10 );
        bottomHud.right( );
        tables.add( bottomHud );
        //add our table to the stage
        stage.addActor( tables );
    }

    @Override
    public void dispose() {
        stage.dispose( );
        colorIndicatorDrawer.dispose( );

    }

    public void show() {

    }

    public void tick(float delta) {
        stage.act( );
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport( ).update( width, height, true );
    }

    public void updateData() {

        healthBarDrawer = new Pixmap( Globals.gameMan.player.maxHealth, 15, Pixmap.Format.RGBA8888 );
        healthBarDrawer.setColor( 0, 0, 0, 0.5f );
        healthBarDrawer.fill();
        healthBarDrawer.setColor( 0.75f, 0, 0, 0.75f );
        healthBarDrawer.fillRectangle( 0, 0, Globals.gameMan.player.health, healthBarDrawer.getHeight() );
        healthBar = new Image( new Texture( new PixmapTextureData( healthBarDrawer, healthBarDrawer.getFormat(), false, false, true ) ));
        tables.right().bottom();
        tables.addActor( healthBar );
        colorIndicatorDrawer = new Pixmap( 85, 255, Pixmap.Format.RGB888 );

        colorIndicatorDrawer.setColor( Color.BLACK );
        colorIndicatorDrawer.drawRectangle( 0, 0, colorIndicatorDrawer.getWidth( ), colorIndicatorDrawer.getHeight( ) );

        colorIndicatorDrawer.setColor( Color.RED );
        colorIndicatorDrawer.fillRectangle( 0, colorIndicatorDrawer.getHeight( ) - Globals.gameMan.player.red, colorIndicatorDrawer.getWidth( ) / 3, Globals.gameMan.player.red );

        colorIndicatorDrawer.setColor( Color.GREEN );
        colorIndicatorDrawer.fillRectangle( colorIndicatorDrawer.getWidth( ) / 3, colorIndicatorDrawer.getHeight( ) - Globals.gameMan.player.green, colorIndicatorDrawer.getWidth( ) / 3, Globals.gameMan.player.green );

        colorIndicatorDrawer.setColor( Color.BLUE );
        colorIndicatorDrawer.fillRectangle( colorIndicatorDrawer.getWidth( ) / 3 * 2, colorIndicatorDrawer.getHeight( ) - Globals.gameMan.player.blue, colorIndicatorDrawer.getWidth( ) / 3, Globals.gameMan.player.blue );

        colorIndicatorDrawer.setColor( Color.WHITE );
        int xLoc = 0;
        switch (Globals.gameMan.player.input.currentColorIndex) {
            case (0):
                colorIndicatorDrawer.drawRectangle( 0, 0, colorIndicatorDrawer.getWidth( ) / 3, colorIndicatorDrawer.getHeight( ) );
                xLoc = 0;
                break;
            case (1):
                colorIndicatorDrawer.drawRectangle( (colorIndicatorDrawer.getWidth( ) / 3), 0, (colorIndicatorDrawer.getWidth( ) / 3), colorIndicatorDrawer.getHeight( ) );
                xLoc = colorIndicatorDrawer.getWidth( ) / 3;
                break;
            case (2):
                colorIndicatorDrawer.drawRectangle( (colorIndicatorDrawer.getWidth( ) / 3) * 2, 0, (colorIndicatorDrawer.getWidth( ) / 3), colorIndicatorDrawer.getHeight( ) );
                xLoc = colorIndicatorDrawer.getWidth( ) / 3 * 2;
                break;
        }

        colorIndicatorDrawer.fillRectangle( xLoc, colorIndicatorDrawer.getHeight( ) - 10, colorIndicatorDrawer.getWidth( ) / 3, 5 );

        colorIndicator = new Image( new Texture( new PixmapTextureData( colorIndicatorDrawer, Pixmap.Format.RGBA8888, false, false, true ) ) );
        colorIndicator.setPosition(-colorIndicatorDrawer.getWidth()+20, 0);
        bottomHud.left();
        bottomHud.bottom();
        bottomHud.addActor( colorIndicator );
    }
}