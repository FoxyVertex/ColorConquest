package com.foxyvertex.colorconquest.components;

import com.foxyvertex.colorconquest.ECS.Component;
import com.foxyvertex.colorconquest.ECS.ComponentTypes;

/**
 * Created by aidan on 12/30/2016.
 */

public class PlayerComponent extends Component {
    public PlayerComponent() {
        super.setTypeID(ComponentTypes.PLAYER_COMPONENT);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void create() {

    }

    @Override
    public void enable() {

    }

    @Override
    public void disable() {

    }

    @Override
    public void obliterate() {

    }
}
