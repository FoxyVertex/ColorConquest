package com.foxyvertex.colorconquest.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by aidan on 12/20/2016.
 */

public class SplashScreen extends Screen {
    private Stage stage;
    private int currentImageIndex = 0;

    public SplashScreen() {
        Globals.splashScreen = this;
    }

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

        for (int i = 0; i < Assets.splashScreenLogos.size(); i++) {
            if (i != Assets.splashScreenLogos.size() - 1) {
                SequenceAction actions = new SequenceAction(sequence(fadeIn(1f), delay(3.5f), fadeOut(1.5f), run(new Runnable() {
                    @Override
                    public void run() {
                        Assets.splashScreenLogos.get(currentImageIndex).remove();
                        currentImageIndex++;
                        stage.addActor(Assets.splashScreenLogos.get(currentImageIndex));
                    }
                })));
                Assets.splashScreenLogos.get(i).addAction(actions);
            } else {
                SequenceAction actions = new SequenceAction(sequence(fadeIn(1f), delay(3.5f), fadeOut(1.5f), run(new Runnable() {
                    @Override
                    public void run() {
                        Globals.game.setScreen(Globals.menuScreen);
                    }
                })));
                Assets.splashScreenLogos.get(i).addAction(actions);
            }
        }
        stage.addActor(Assets.splashScreenLogos.get(currentImageIndex));
        if (!Assets.menuMusic.isPlaying()) {
            Assets.playMusic(Assets.menuMusic);
            Assets.menuMusic.setLooping(true);
        }
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
        stage.dispose();
    }

    public static class SplashLogo {
        public Image actorImage;

        public SplashLogo(String file, float scale) {
            Texture texture = new Texture(Gdx.files.internal(file));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            actorImage = new Image(new TextureRegion(texture));
            actorImage.getColor().a = 0;
            actorImage.setPosition(Gdx.graphics.getWidth() / 2 - (actorImage.getWidth() * scale) / 2, Gdx.graphics.getHeight() / 2 - (actorImage.getHeight() * scale) / 2);
            actorImage.setScale(scale);
        }
    }
}
