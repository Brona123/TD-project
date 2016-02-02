package fi.joutsijoki.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.AnimationBuilder;
import fi.joutsijoki.pathfinding.GraphPathImpl;

/**
 * Created by Sami on 12.1.2016.
 */
public class Mob extends Enemy {

    public Mob(Texture t, int health, float x, float y, int bounty) {
        this.texture = t;
        this.walkAnimation = AnimationBuilder.buildAnimation(t, 3, 1, 0.2f);
        this.walkAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        this.maxHealth = health;
        this.currentHealth = maxHealth;
        this.position = new Vector2(x, y);
        this.bounty = bounty;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setPath(GraphPathImpl path) {
        this.path = path;
        this.currentNode = path.get(0);
        this.nextNode = path.get(1);
        this.nextNodeIndex = 1;
    }
}
