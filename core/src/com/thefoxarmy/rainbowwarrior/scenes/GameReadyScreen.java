package com.thefoxarmy.rainbowwarrior.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ai.steer.behaviors.Alignment;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import com.thefoxarmy.rainbowwarrior.managers.Assets;
import com.thefoxarmy.rainbowwarrior.screens.GameScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by aidan on 12/20/2016.
 */

public class GameReadyScreen extends Scene implements InputProcessor {

    public GameReadyScreen(GameScreen screen) {
        super(screen);
        Label pressAnyKeyToStart = new Label("Press Any Key to Start the Game", Assets.guiSkin);
        pressAnyKeyToStart.getColor().a = 0;
        pressAnyKeyToStart.setPosition(Gdx.graphics.getWidth()/2 - pressAnyKeyToStart.getWidth()/2, Gdx.graphics.getHeight()/2 - pressAnyKeyToStart.getHeight()/2);
        SequenceAction actions = new SequenceAction(forever(sequence(fadeIn(0.2f), fadeOut(0.2f))));
        pressAnyKeyToStart.setFontScale(1.4f);
        pressAnyKeyToStart.setAlignment(1);
        pressAnyKeyToStart.addAction(actions);
        stage.addActor(pressAnyKeyToStart);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void tick(float delta) {
        stage.act(delta);

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.input.setInputProcessor(DynamicGlobals.gameScreen.player.input);
        GameScreen.gameState = GameScreen.GameState.RUNNING;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Gdx.input.setInputProcessor(DynamicGlobals.gameScreen.player.input);
        GameScreen.gameState = GameScreen.GameState.RUNNING;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
