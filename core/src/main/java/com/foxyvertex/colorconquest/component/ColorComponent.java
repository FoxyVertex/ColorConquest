package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * Created by aidan on 2/28/2017.
 */

public class ColorComponent extends Component {
    public float r;
    public float g;
    public float b;

    public ColorComponent(Color c) {
        r = c.r;
        g = c.g;
        b = c.b;
    }
}
