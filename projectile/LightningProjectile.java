package fi.joutsijoki.projectile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.AudioHandler;
import fi.joutsijoki.Utils;
import fi.joutsijoki.enemy.Enemy;
import fi.joutsijoki.lightning.LightningBolt;

/**
 * Created by Sami on 21.1.2016.
 */
public class LightningProjectile extends Projectile {
    private LightningBolt lightningBolt;
    private int cooldown;
    private long timeAtLastShot;

    public LightningProjectile(Vector2 fromVec, Enemy target, int damage, int shotCooldown) {
        super(fromVec, Utils.centerPos(target.getPos()), target, damage);
        this.lightningBolt = new LightningBolt(fromVec, Utils.centerPos(target.getPos()), Color.PURPLE);
        this.cooldown = shotCooldown;
    }

    @Override
    public void draw(SpriteBatch batch) {
        this.lightningBolt.draw(batch);
    }

    @Override
    public void update() {
        this.lightningBolt.update();

        long shotTime = System.currentTimeMillis();
        long timeSinceLastShot = shotTime - timeAtLastShot;

        if (timeSinceLastShot > cooldown) {

            if (this.target.isAlive()) {
                this.target.damage(this.damage);
                this.lightningBolt = new LightningBolt(this.pos, this.target.getPos());
                AudioHandler.playEffect(AudioHandler.EFFECT.LIGHTNING);
            } else {
                if (this.lightningBolt.isComplete()) {
                    this.hitTarget = true;
                }
            }

            this.timeAtLastShot = System.currentTimeMillis();
        }
    }

    public boolean isComplete() {
        return this.lightningBolt.isComplete();
    }
}
