package com.foxyvertex.colorconquest.exception;

import com.artemis.Entity;
import com.kotcrab.vis.runtime.component.Variables;

/**
 * Created by aidan on 2/17/2017.
 */

public class NoCategoryBitFoundOnPhysicsEntityException extends Exception {
    public NoCategoryBitFoundOnPhysicsEntityException(Entity entity) {
        super(calculateMessage(entity));
    }

    private static String calculateMessage(Entity entity) {
        String message = "";

        message += "Entity with id: " + entity.getId() + " says: ";

        if (entity.getComponent(Variables.class) == null) {
            message += "There is no Variables component";
        } else if (entity.getComponent(Variables.class).get("collisionCat") == null) {
            message += "There is no collisionCat variable on the Variables component";
        }

        return message;
    }
}
