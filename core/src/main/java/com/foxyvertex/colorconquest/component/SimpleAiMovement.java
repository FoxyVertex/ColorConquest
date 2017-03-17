package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aidan on 3/16/17.
 */

public class SimpleAiMovement extends Component {
    public String type;
    public Vector2 waypoint1;
    public Vector2 waypoint2;
    public int currentGoal = 1;
    public String direction;

    public float speed;
}
