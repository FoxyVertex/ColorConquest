package com.foxyvertex.colorconquest.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by aidan on 12/24/2016.
 */

public abstract class Entity extends Sprite implements Disposable {

    protected float health;
    protected float maxHealth;
    protected String name;

    public Entity(Vector2 startPos, String name, float maxHealth) {
        this.setPosition(startPos.x, startPos.y);
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public Entity(String name, float maxHealth) {
        this.setPosition(0f, 0f);
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public Entity(Vector2 startPos, float maxHealth) {
        this.setPosition(startPos.x, startPos.y);
        this.name = "";
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public Entity(float maxHealth) {
        this.setPosition(0f, 0f);
        this.name = "";
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public Entity(Vector2 startPos, String name, float maxHealth, Object mySupersConstruct) {
        super((TextureRegion) mySupersConstruct);
        this.setPosition(startPos.x, startPos.y);
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public Entity(String name, float maxHealth, Object mySupersConstruct) {
        super((TextureRegion) mySupersConstruct);
        this.setPosition(0f, 0f);
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public Entity(Vector2 startPos, float maxHealth, Object mySupersConstruct) {
        super((TextureRegion) mySupersConstruct);
        this.setPosition(startPos.x, startPos.y);
        this.name = "";
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public Entity(float maxHealth, Object mySupersConstruct) {
        super((TextureRegion) mySupersConstruct);
        this.setPosition(0f, 0f);
        this.name = "";
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public Entity() {}

    public abstract void tick(float delta);

    public abstract void die(Entity killedBy);

    public abstract void damage(Entity damagedBy, float damageAmount);
}
