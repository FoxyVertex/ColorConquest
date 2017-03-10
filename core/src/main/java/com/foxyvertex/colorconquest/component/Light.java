package com.foxyvertex.colorconquest.component;

import com.artemis.Component;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;

/**
 * Created by aidan on 3/9/17.
 */

public class Light extends Component {
    public String type;
    public PositionalLight light;

    public Light(PositionalLight light) {
        this.light = light;
        if (light instanceof PointLight) {
            type = "point";
        } else if (light instanceof ConeLight) {
            type = "cone";
        }
    }
}
