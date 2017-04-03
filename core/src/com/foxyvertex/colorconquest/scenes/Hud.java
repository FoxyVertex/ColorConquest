package com.foxyvertex.colorconquest.scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;

/**
 * Created by aidan on 11/26/2016.
 * <p>
 * This class represents the heads up display and is currently housing a placeholder for it.
 */

public class Hud extends Scene {

    public Image colorIndicator;
    Pixmap colorIndicatorDrawer;
    Table  bottomHud;

    /**
     * The constructor sets up the stage.
     *
     * @param screen the screen is passed to super
     */
    public Hud(final Screen screen) {
        super(screen);

        //Bottom of the HUD that displays color information
        bottomHud = new Table(Assets.guiSkin);
        bottomHud.setFillParent(true);
        //Table Positioning stuff doesn't work yet -_-
        bottomHud.row();
        bottomHud.pad(10);
        bottomHud.right();

        //add our table to the stage
        stage.addActor(bottomHud);
    }

    @Override
    public void dispose() {
        stage.dispose();
        colorIndicatorDrawer.dispose();

    }

    public void show() {

    }

    public void tick(float delta) {
        stage.act();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void updateData() {

        colorIndicatorDrawer = new Pixmap(85, 255, Pixmap.Format.RGB888);

        colorIndicatorDrawer.setColor(Color.BLACK);
        colorIndicatorDrawer.drawRectangle(0, 0, colorIndicatorDrawer.getWidth(), colorIndicatorDrawer.getHeight());

        colorIndicatorDrawer.setColor(Color.RED);
        colorIndicatorDrawer.fillRectangle(0, colorIndicatorDrawer.getHeight() - Globals.gameMan.player.red, colorIndicatorDrawer.getWidth() / 3, Globals.gameMan.player.red);

        colorIndicatorDrawer.setColor(Color.GREEN);
        colorIndicatorDrawer.fillRectangle(colorIndicatorDrawer.getWidth() / 3, colorIndicatorDrawer.getHeight() - Globals.gameMan.player.green, colorIndicatorDrawer.getWidth() / 3, Globals.gameMan.player.green);

        colorIndicatorDrawer.setColor(Color.BLUE);
        colorIndicatorDrawer.fillRectangle(colorIndicatorDrawer.getWidth() / 3 * 2, colorIndicatorDrawer.getHeight() - Globals.gameMan.player.blue, colorIndicatorDrawer.getWidth() / 3, Globals.gameMan.player.blue);

        colorIndicatorDrawer.setColor(Color.WHITE);
        int xLoc = 0;
        switch (Globals.gameMan.player.input.currentColorIndex) {
            case (0):
                colorIndicatorDrawer.drawRectangle(0, 0, colorIndicatorDrawer.getWidth() / 3, colorIndicatorDrawer.getHeight());
                xLoc = 0;
                break;
            case (1):
                colorIndicatorDrawer.drawRectangle((colorIndicatorDrawer.getWidth() / 3), 0, (colorIndicatorDrawer.getWidth() / 3), colorIndicatorDrawer.getHeight());
                xLoc = colorIndicatorDrawer.getWidth() / 3;
                break;
            case (2):
                colorIndicatorDrawer.drawRectangle((colorIndicatorDrawer.getWidth() / 3) * 2, 0, (colorIndicatorDrawer.getWidth() / 3), colorIndicatorDrawer.getHeight());
                xLoc = colorIndicatorDrawer.getWidth() / 3 * 2;
                break;
        }

        colorIndicatorDrawer.fillRectangle(xLoc, colorIndicatorDrawer.getHeight() - 10, colorIndicatorDrawer.getWidth() / 3, 5);

        colorIndicator = new Image(new Texture(new PixmapTextureData(colorIndicatorDrawer, Pixmap.Format.RGBA8888, false, false, true)));

        bottomHud.addActor(colorIndicator);
        bottomHud.right();
        bottomHud.bottom();
    }
}