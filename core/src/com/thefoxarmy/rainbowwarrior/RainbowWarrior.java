package com.thefoxarmy.rainbowwarrior;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RainbowWarrior extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private int x = 0, y = 0, x2, y2;

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
    }

	@Override
	public void render () {
        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //I Tried... Not verry hard though.....
        batch.draw(img, x++, y++, x++, y++);
        batch.draw(img, x2-- + Gdx.graphics.getWidth() - img.getWidth(), y2++, x2-- + Gdx.graphics.getWidth() - img.getWidth(), y++);//, x2++, y2++);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
		img.dispose();
	}
}
