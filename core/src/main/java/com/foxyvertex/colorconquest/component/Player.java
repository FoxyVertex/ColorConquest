package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.runtime.util.autotable.ATProperty;

/**
 * Created by aidan on 2/13/2017.
 */

public class Player extends Component {
    public float normalJumpForce = 46f;
    public float jumpForce = normalJumpForce;
    public float normalRunSpeed = 8f;
    public float runSpeed = normalRunSpeed;
    public boolean isFiring = false;

    public Color selectedColor = Color.RED;
    public Array<Color> colors;

    public int red = 255;
    public int green = 255;
    public int blue = 255;
    public int score = 0;
}
