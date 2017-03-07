package com.foxyvertex.colorconquest.component;

import com.artemis.Component;
import com.foxyvertex.colorconquest.tools.DeathRunnable;

/**
 * Created by aidan on 3/6/17.
 */

public class Health extends Component {
    public DeathRunnable deathCallback;
    public float maxHealth;
    public float currentHealth;

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
}
