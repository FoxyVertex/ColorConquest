package com.thefoxarmy.rainbowwarrior.scenes;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thefoxarmy.rainbowwarrior.Assets;
import com.thefoxarmy.rainbowwarrior.Globals;

/**
 * Created by aidan on 11/26/2016.
 *
 * This class represents the heads up display and is currently housing a placeholder for it.
 */

public class Hud implements Disposable {
    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    //Mario score/time Tracking Variables
    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    //Scene2D widgets
    private Label countdownLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Label levelLabel;
    private Label worldLabel;
    private Label marioLabel;

    /**
     * The constructor sets up the stage.
     * @param sb the sprite batch is used to set up the stage.
     */
    public Hud(SpriteBatch sb){
        //define our tracking variables
        worldTimer = 300;
        timeCount = 0;
        score = 0;


        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Globals.V_WIDTH, Globals.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        //Top-Align table
        table.top();
        //make the table fill the entire stage
        table.setFillParent(true);

        //define our labels using the String, and a Label style consisting of a font and color
        countdownLabel = new Label(String.format("%03d", worldTimer), Assets.guiSkin);
        scoreLabel =new Label(String.format("%06d", score), Assets.guiSkin);
        timeLabel = new Label("TIME", Assets.guiSkin);
        levelLabel = new Label("1-1", Assets.guiSkin);
        worldLabel = new Label("WORLD", Assets.guiSkin);
        marioLabel = new Label("MARIO", Assets.guiSkin);

        countdownLabel.setFontScale(0.5f);
        scoreLabel.setFontScale(0.5f);
        timeLabel.setFontScale(0.5f);
        levelLabel.setFontScale(0.5f);
        worldLabel.setFontScale(0.5f);
        marioLabel.setFontScale(0.5f);

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        //add a second row to our table
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdownLabel).expandX();

        //add our table to the stage
        stage.addActor(table);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
