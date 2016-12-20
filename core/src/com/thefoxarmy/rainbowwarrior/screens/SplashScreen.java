package com.thefoxarmy.rainbowwarrior.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;
import com.thefoxarmy.rainbowwarrior.DynamicGlobals;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by aidan on 12/20/2016.
 */

public class SplashScreen extends Screen {
    private Texture texture;
    private Stage stage;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
        stage = new Stage();
        texture = new Texture(Gdx.files.internal("FoxyVertex.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion textureRegion = new TextureRegion(texture);
        Image actorImage = new Image(textureRegion);
        actorImage.getColor().a = 0;
        actorImage.setPosition(Gdx.graphics.getWidth()/2 - (actorImage.getWidth() * 0.8f)/2, Gdx.graphics.getHeight()/2 - (actorImage.getHeight() * 0.8f)/2);
        actorImage.setScale(0.8f);

        SequenceAction actions = new SequenceAction(sequence(fadeIn(1f), delay(3.5f), fadeOut(1.5f), run(new Runnable() {
            @Override
            public void run() {
                DynamicGlobals.game.setScreen(DynamicGlobals.menuScreen);
            }
        })));
        actorImage.addAction(actions);
        stage.addActor(actorImage);
    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
