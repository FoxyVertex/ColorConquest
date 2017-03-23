package com.foxyvertex.colorconquest.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.foxyvertex.colorconquest.ColorConquest;
import com.foxyvertex.colorconquest.component.SimpleAiMovement;
import com.kotcrab.vis.runtime.component.PhysicsBody;
import com.kotcrab.vis.runtime.component.Variables;
import com.kotcrab.vis.runtime.component.VisSprite;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by aidan on 3/16/17.
 */

public class simpleAiMovementSystem extends EntitySystem {
    ComponentMapper<Variables> variablesCm;
    private boolean isStarted = false;

    private Array<Entity> toAdd = new Array<>();

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     */
    public simpleAiMovementSystem() {
        super(Aspect.all(Variables.class));
    }

    @Override
    protected void processSystem() {
        if (!isStarted) {
            isStarted = true;
        }
        for (Entity e : toAdd) {
            inserted(e);
            toAdd.removeValue(e, true);
        }

        for (Entity e : getEntities()) {
            if (e.getComponent(SimpleAiMovement.class) != null) {
                SimpleAiMovement movComp = e.getComponent(SimpleAiMovement.class);

                if (movComp.type.equals("waypoints")) {
                    Body body = e.getComponent(PhysicsBody.class).body;

                    float xVel = body.getLinearVelocity().x;
                    float yVel = body.getLinearVelocity().y;
                    float desiredXVel = 0;

                    Vector2 position = body.getPosition();

                    if (movComp.currentGoal == 1) {
                        if (movComp.waypoint1.x > position.x) {
                            e.getComponent(VisSprite.class).setFlip(true, false);
                            desiredXVel = movComp.speed;
                        } else if (movComp.waypoint1.x < position.x) {
                            e.getComponent(VisSprite.class).setFlip(false, false);
                            desiredXVel = -movComp.speed;
                        }
                        if (movComp.waypoint2.x > movComp.waypoint1.x && movComp.waypoint1.x > position.x) {
                            movComp.currentGoal = 2;
                        } else if (movComp.waypoint2.x < movComp.waypoint1.x && movComp.waypoint1.x < position.x) {
                            movComp.currentGoal = 2;
                        }
                    } else {
                        if (movComp.waypoint2.x > position.x) {
                            e.getComponent(VisSprite.class).setFlip(true, false);
                            desiredXVel = movComp.speed;
                        } else if (movComp.waypoint2.x < position.x) {
                            e.getComponent(VisSprite.class).setFlip(false, false);
                            desiredXVel = -movComp.speed;
                        }
                        if (movComp.waypoint1.x > movComp.waypoint2.x && movComp.waypoint2.x > position.x) {
                            movComp.currentGoal = 1;
                        } else if (movComp.waypoint1.x < movComp.waypoint2.x && movComp.waypoint2.x < position.x) {
                            movComp.currentGoal = 1;
                        }
                    }

                    body.setLinearVelocity(desiredXVel, yVel);
                } else if (movComp.type.equals("direction")) {
                    Body body = e.getComponent(PhysicsBody.class).body;
                    float desiredXVel = 0;

                    if (movComp.direction.equals("left")) {
                        desiredXVel = -movComp.speed;
                    } else {
                        desiredXVel = movComp.speed;
                    }

                    body.setLinearVelocity(desiredXVel, body.getLinearVelocity().y);
                }
            }
        }
    }

    @Override
    public void inserted(Entity e) {
        if (!isStarted) {
            toAdd.add(e);
            return;
        }
        if (variablesCm.get(e).get("simpleAiMovement") != null) {
            JSONParser parser = new JSONParser();
            String json = variablesCm.get(e).get("simpleAiMovement");

            try {
                JSONObject obj = (JSONObject)parser.parse(json);
                String type = (String) obj.get("type");

                if (type.equals("direction")) {
                    String direction = (String) obj.get("direction");
                    SimpleAiMovement simpleAiMovementComp = new SimpleAiMovement();
                    simpleAiMovementComp.type = type;
                    simpleAiMovementComp.direction = direction;
                    simpleAiMovementComp.speed = (float)(double)obj.get("speed");
                    e.edit().add(simpleAiMovementComp);
                } else if (type.equals("waypoints")) {
                    JSONArray waypoints = (JSONArray) obj.get("waypoints");
                    SimpleAiMovement simpleAiMovementComp = new SimpleAiMovement();
                    simpleAiMovementComp.type = type;
                    simpleAiMovementComp.waypoint1 = new Vector2((float)(double)((JSONObject)waypoints.get(0)).get("x"), (float)(double)((JSONObject)waypoints.get(0)).get("y"));
                    simpleAiMovementComp.waypoint2 = new Vector2((float)(double)((JSONObject)waypoints.get(1)).get("x"), (float)(double)((JSONObject)waypoints.get(1)).get("y"));
                    simpleAiMovementComp.speed = (float)(double)obj.get("speed");
                    e.edit().add(simpleAiMovementComp);
                }

            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
    }
}
