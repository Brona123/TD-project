package fi.joutsijoki.tower;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.enemy.Enemy;

/**
 * Created by Sami on 19.1.2016.
 */
public abstract class Projectile {
    protected Vector2 pos;
    protected Vector2 fromVec;
    protected Vector2 toVec;
    protected boolean hitTarget;
    protected Enemy target;
    protected int damage;

    public Projectile() {}

    public Projectile(Vector2 fromVec, Vector2 toVec, Enemy target, int damage) {
        this.pos = new Vector2(fromVec);
        this.fromVec = fromVec;
        this.toVec = toVec;
        this.target = target;
        this.damage = damage;
    }

    abstract public void update();
    abstract public void draw(SpriteBatch batch);

    public boolean hitTarget() {
        return this.hitTarget;
    }
}
