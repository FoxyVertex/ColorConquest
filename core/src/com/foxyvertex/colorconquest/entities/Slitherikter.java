package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.Finals;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.managers.Assets;
import com.foxyvertex.colorconquest.tools.Utilities;

/**
 * Created by seth on 3/12/2017.
 */

public class Slitherikter extends Interactant {

    public boolean shouldFlip = false;
    public  boolean isColidingWithSlime;
    private int     prevColor;
    private boolean hasBeenShotByColor = false;
    private Color initialColor;
    public Array<Slime> slimes = new Array<Slime>();

    /**
     * Slitherikter is snail like enemy that leaves a trail of color based on the specefied eye color and is defeated by shooting a complementary color
     *
     * @param spawnPoint A place to spawn the slitherikter
     * @param color      The color eye the slitherikter will have
     */
    public Slitherikter(Vector2 spawnPoint, Color color) {
        super(spawnPoint);
        initialColor = new Color(color);
        setRegion(Assets.slitherikter);
        this.spawnPoint = spawnPoint;
        super.color = color;
        def();
        setBounds(0, 0, getRegionWidth() / 4.5f / Finals.PPM, getRegionHeight() / 4.5f / Finals.PPM);
        //Prepare the Texture for pixmap creation
        if (!getTexture().getTextureData().isPrepared()) getTexture().getTextureData().prepare();

        fillColor();
    }

    protected short CATIGORY_BIT() {
        return Finals.SLITHERIKTER_BIT;
    }

    /**
     * Fill the Slitherikter's eye with the current color in the class.
     */
    private void fillColor() {
        long   start          = System.nanoTime();
        Pixmap eyeColorPixmap = getTexture().getTextureData().consumePixmap();
        //Loop though every x and y value of the pixmap
        for (int x = 400; x < eyeColorPixmap.getWidth(); x++) {
            for (int y = 400; y < eyeColorPixmap.getHeight(); y++) {
                //if the current pixel's color is equal to the specified color of the Slitherikter's eye. Really only happens when setting the color from the tiled map,
                if (eyeColorPixmap.getPixel(x, y) == Finals.Slitherikter_INITIAL_EYE_COLOR) {
                    eyeColorPixmap.drawPixel(x, y, Color.rgba8888(color)); //Replace the pixel with one from the tiled map.
                    prevColor = Color.rgba8888(color); //set a variable that can make sure the last applied color can be referenced later.
                    // if the current pixel's color is equal to the previous color the eye was set to

                } else if (eyeColorPixmap.getPixel(x, y) == prevColor) {
                    eyeColorPixmap.drawPixel(x, y, Color.rgba8888(color)); //apply the class's eye color to the current pixel
                    hasBeenShotByColor = true; //in order to prevent the application of color to just the first possible pixel, this flag is set to update the color after it's been applied.
                }
            }
        }
        //if the color was previously applied+
        if (hasBeenShotByColor)
            prevColor = Color.rgba8888(color); // Update the previous color to the one that was applied.
        setRegion(new Texture(eyeColorPixmap));
        Gdx.app.log("", "" + (System.nanoTime() - start) / 1000000);
    }

    private void def() {
        CircleShape shape = new CircleShape();
        shape.setRadius(8f / Finals.PPM);
        super.def(shape);
        FixtureDef bufferFdef = new FixtureDef();
        EdgeShape  buffer     = new EdgeShape();
        buffer.set(new Vector2(shape.getRadius(), shape.getRadius()), new Vector2(shape.getRadius(), 0));
        bufferFdef.shape = buffer;
        bufferFdef.filter.categoryBits = Finals.ENEMY_BUFFER_BIT;
        body.createFixture(bufferFdef).setUserData(this);

        buffer.set(new Vector2(-shape.getRadius(), shape.getRadius()), new Vector2(-shape.getRadius(), 0));
        bufferFdef.shape = buffer;
        body.createFixture(bufferFdef).setUserData(this);
        body.setLinearDamping(7f);
    }

    public void tick(float delta) {
        super.tick(delta);
        Gdx.app.log("", "" + runSpeed);
        if (!shouldFlip) {
            TextureRegion t = new TextureRegion(Assets.slitherikter);
            t.flip(true, false);
            setRegion(t);
        } else {
            TextureRegion t = new TextureRegion(Assets.slitherikter);
            t.flip(false, false);
            setRegion(t);
        }
        body.applyLinearImpulse(new Vector2((!shouldFlip) ? runSpeed : -runSpeed, 0), body.getWorldCenter(), true);

        if (!isColidingWithSlime && body.getLinearVelocity().y == 0)
            EntityController.entities.add(new Slime(body.getPosition().add(0, -primaryFixture.getShape().getRadius()), color));

    }

    @Override
    public void reInitVars() {
        minJumpForce = 0.5f;
        maxJumpForce = 1f;
    }

    /**
     * When a bullet collides with the slitherikter, the color is added with the bullet color.
     *
     * @param attacker the specific bullet that hit the slitherikter to get its color.
     */
    @Override
    public void attacked(SpriteBody attacker) {
        color.r += Utilities.clamp(attacker.color.r, 0, 1);
        color.g += Utilities.clamp(attacker.color.g, 0, 1);
        color.b += Utilities.clamp(attacker.color.b, 0, 1);
        fillColor();
        //Gdx.app.log(color + "", "" + Utilities.complement(initialColor));
        if (color.r == Utilities.complement(initialColor).r && color.g == Utilities.complement(initialColor).g && color.b == Utilities.complement(initialColor).b)
            die();
    }

    @Override
    public void die() {
        super.die();
    }
}