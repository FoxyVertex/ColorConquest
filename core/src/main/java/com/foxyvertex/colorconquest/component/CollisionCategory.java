package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.kotcrab.vis.runtime.util.autotable.ATProperty;

public class CollisionCategory
extends Component {
    @ATProperty(fieldName="Collision Category")
    String collisionCat;

    public CollisionCategory(String collisionCat) {
        this.collisionCat = collisionCat;
    }
}



