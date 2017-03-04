package com.foxyvertex.colorconquest.component;

import com.artemis.Component;

/**
 * Created by aidan on 3/4/2017.
 */

public class ToDestroy extends Component {
    public float currentTimer;

    public ToDestroy(float initialTime) {
        this.currentTimer = initialTime;
    }
}