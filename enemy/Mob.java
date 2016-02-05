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
        super(t, health, x, y, bounty);
    }

    public Texture getTexture() {
        return this.texture;
    }
}
