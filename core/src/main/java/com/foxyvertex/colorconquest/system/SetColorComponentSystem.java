package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.component.ColorComponent;
import com.kotcrab.vis.runtime.component.Tint;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.util.AfterSceneInit;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by aidan on 2/28/2017.
 */

/**
 * SetColorComponentSystem sets the ColorComponent of all the entities who need it
 */
public class SetColorComponentSystem extends BaseEntitySystem {
    VisIDManager idManager;

    ComponentMapper<Variables> variablesCm;

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public SetColorComponentSystem() {
        super(Aspect.all(Variables.class).exclude(ColorComponent.class));
    }

    /**
     * processSystem is called every frame at a max of 60 times per second. It manages the movement of the zombies.
     */
    @Override
    protected void processSystem() {

    }

    /**
     * inserted is called every time an entity is added to the scene
     * @param entities
     */
    @Override
    public void inserted(IntBag entities) {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = getWorld().getEntity(entities.get(i));
            if (variablesCm.get(e) != null) {
                if (variablesCm.get(e).get("effect") != null) {
                    JSONParser parser = new JSONParser();
                    String json = variablesCm.get(e).get("effect");
                    try {
                        JSONObject obj = (JSONObject)parser.parse(json);
                        JSONObject colorObj = (JSONObject) obj.get("color");
                        float colorR = (float)((double) colorObj.get("r"));
                        float colorG = (float)((double) colorObj.get("g"));
                        float colorB = (float)((double) colorObj.get("b"));
                        float colorA = (float)((double) colorObj.get("a"));
                        e.edit().add(new ColorComponent(new Color(colorR, colorG, colorB, colorA)));
                    } catch (ParseException pe) {
                        pe.printStackTrace();
                    }
                }
            }
        }

    }
}
