package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.artemis.Entity;
import com.foxyvertex.colorconquest.tools.DamageRunnable;
import com.foxyvertex.colorconquest.tools.DeathRunnable;

/**
 * Created by aidan on 3/6/17.
 */

public class Health extends Component {
    public DeathRunnable deathCallback;
    public DamageRunnable damageCallback;
    private float maxHealth;
    private float currentHealth;

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }

    public Health(DeathRunnable deathCallback, float maxHealth) {
        this.deathCallback = deathCallback;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public Health(DeathRunnable deathCallback, float maxHealth, float currentHealth) {
        this.deathCallback = deathCallback;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
    }

    public Health(DeathRunnable deathCallback, float maxHealth, DamageRunnable damageCallback) {
        this.deathCallback = deathCallback;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.damageCallback = damageCallback;
    }

    public Health(DeathRunnable deathCallback, float maxHealth, float currentHealth, DamageRunnable damageCallback) {
        this.deathCallback = deathCallback;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.damageCallback = damageCallback;
    }

    public float dealDamage(Entity e, float damage) {
        currentHealth -= damage;
        if (damageCallback != null) damageCallback.run(e, damage);
        return currentHealth;
    }
}
