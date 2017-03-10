package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.ColorConquest;
import com.foxyvertex.colorconquest.Globals;
import com.foxyvertex.colorconquest.component.Light;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Point;
import com.kotcrab.vis.runtime.component.Transform;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.system.CameraManager;
import com.kotcrab.vis.runtime.system.VisIDManager;
import com.kotcrab.vis.runtime.system.physics.PhysicsSystem;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.PositionalLight;
import box2dLight.RayHandler;

/**
 * Created by aidan on 3/8/17.
 */

public class B2DLightsSetupSystem extends EntitySystem {
    public boolean isStarted = false;

    public Array<Entity> toAdd = new Array<>();

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public B2DLightsSetupSystem() {
        super(Aspect.all(Variables.class));
    }

    @Override
    protected void processSystem() {
        if (!isStarted) {
            Globals.gameScreen.b2dlHandler = new RayHandler(getWorld().getSystem(PhysicsSystem.class).getPhysicsWorld());
            JSONParser parser = new JSONParser();
            String json = Globals.gameScreen.scene.getSceneVariables().get("ambientLight");
            try {
                JSONObject obj = (JSONObject)parser.parse(json);
                JSONObject colorObj = (JSONObject) obj.get("color");
                float colorR = (float)((double) colorObj.get("r"));
                float colorG = (float)((double) colorObj.get("g"));
                float colorB = (float)((double) colorObj.get("b"));
                float colorA = (float)((double) colorObj.get("a"));
                Globals.gameScreen.b2dlHandler.setAmbientLight(new Color(colorR, colorG, colorB, colorA));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
            isStarted = true;
        }

        for (Entity e : toAdd) {
            inserted(e);
            toAdd.removeValue(e, true);
        }
    }

    @Override
    public void inserted(Entity e) {
        if (!isStarted) {
            toAdd.add(e);
            return;
        }
        if (e.getComponent(Variables.class) == null) return;
        if (e.getComponent(Variables.class).get("light") == null) return;
        JSONParser parser = new JSONParser();
        String json = e.getComponent(Variables.class).get("light");
        Transform transform = e.getComponent(Transform.class);

        try {
            JSONObject obj = (JSONObject)parser.parse(json);
            String type = (String) obj.get("type");
            JSONObject colorObj = (JSONObject) obj.get("color");
            float colorR = (float)((double) colorObj.get("r"));
            float colorG = (float)((double) colorObj.get("g"));
            float colorB = (float)((double) colorObj.get("b"));
            float colorA = (float)((double) colorObj.get("a"));
            int rays = (int) Math.floor((double) obj.get("rays"));
            float distance = (float)(double) obj.get("distance");
            float directionDegree;
            float coneDegree;

            PositionalLight light = null;

            if (type.equals("point")) {
                light = new PointLight(Globals.gameScreen.b2dlHandler, rays, new Color(colorR, colorG, colorB, colorA), distance, transform.getX(), transform.getY());
                e.edit().add(new Light(light));
            } else if (type.equals("cone")) {
                directionDegree = (float)(double) obj.get("directionDegree");
                coneDegree = (float)(double) obj.get("coneDegree");
                light = new ConeLight(Globals.gameScreen.b2dlHandler, rays, new Color(colorR, colorG, colorB, colorA), distance, transform.getX(), transform.getY(), directionDegree, coneDegree);
                e.edit().add(new Light(light));
            }

            if (e.getComponent(PhysicsBody.class) != null) light.attachToBody(e.getComponent(PhysicsBody.class).body);

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
    }

}