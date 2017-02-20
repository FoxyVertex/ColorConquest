package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.util.autotable.ATProperty;

/**
 * Created by aidan on 2/13/2017.
 */

public class Player extends Component {
    @ATProperty(fieldName="Max Jump Force")
    public float maxJumpForce = 300;
    @ATProperty(fieldName="Min Jump Force")
    public float minJumpForce = 55;
    public float jumpForce = minJumpForce;
    @ATProperty(fieldName="Max Run Speed")
    public float maxRunSpeed = 300f;
    @ATProperty(fieldName="Min Run Speed")
    public float minRunSpeed = 55f;
    public float runSpeed = minRunSpeed;
    public boolean isFiring = false;

    public Color selectedColor = Color.BLACK;
    public Array<Color> colors;

    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public int score = 0;

    public Player(float maxJumpForce, float minJumpForce, float maxRunSpeed, float minRunSpeed) {
        this.maxJumpForce = maxJumpForce;
        this.minJumpForce = minJumpForce;
        this.maxRunSpeed = maxRunSpeed;
        this.minRunSpeed = minRunSpeed;
    }

    public Player(){}
}
