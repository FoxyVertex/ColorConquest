package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.foxyvertex.colorconquest.tools.WorldPhysicsContactListener;

/**
 * Created by seth on 3/24/2017.
 */

public abstract class Interactant extends SpriteBody {

    public float maxJumpForce = 300;
    public float minJumpForce = 55;
    public float jumpForce    = minJumpForce;
    public float maxRunSpeed  = 0.25f;
    public float minRunSpeed  = 0.2f;
    public float runSpeed     = minRunSpeed;
    public int health = 20;
    public boolean doFallDamage = true;
    public boolean isInvulnerable = false;
    public boolean isBeingDamaged = false;
    public float minDPS = 1f;
    public float DPS = minDPS;
    public float maxDPS = 5f;
    private float DPStimer = 0;

    Interactant(Vector2 spawnPoint) {
        super(spawnPoint);
        reInitVars();
    }

    public abstract void reInitVars();

    public abstract void attacked(SpriteBody attacker);

    public void tick(float delta) {
        super.tick( delta );
        if (isBeingDamaged) {
            DPStimer += delta;
            if (DPStimer >= 1) {
                health -= DPS;
                DPStimer = 0;
            }
        }
        if(health <= 0)
            die();
    }

    public void die() {
        if(!isInvulnerable)
            setToDestroy = true;
    }
}
