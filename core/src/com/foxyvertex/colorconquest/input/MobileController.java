package com.foxyvertex.colorconquest.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.tools.Drawable;
import com.foxyvertex.colorconquest.tools.Utilities;

/**
 * Created by aidan on 1/23/17.
 */
public class MobileController implements Drawable {
    private PlayerInput inputManager;

    Viewport viewport;
    public Stage stage;

    public MobileController(final PlayerInput inputManager){
        this.inputManager = inputManager;

    }

    public void draw(){
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }

    public void handleInput() {

    }
}