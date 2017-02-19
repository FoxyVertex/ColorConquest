package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

/**
 * Created by aidan on 2/13/2017.
 */

public class Player extends Component {
    public float maxJumpForce = 300;
    public float minJumpForce = 55;
    public float jumpForce = minJumpForce;
    public float maxRunSpeed = 300f;
    public float minRunSpeed = 55f;
    public float runSpeed = minRunSpeed;
    public boolean isFiring = false;

    public Array<Color> colors;
    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public int score = 0;
}
