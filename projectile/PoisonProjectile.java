package fi.joutsijoki.projectile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.GameField;
import fi.joutsijoki.Utils;
import fi.joutsijoki.enemy.Enemy;

/**
 * Created by Sami on 23.1.2016.
 */
public class PoisonProjectile extends Projectile {

    public PoisonProjectile(Vector2 fromVec, Enemy target, int damage) {
        this.pos = new Vector2(fromVec);
        this.fromVec = new Vector2(fromVec);
        this.toVec = Utils.centerPos(target.getPos());
        this.target = target;
        this.damage = damage;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(GameField.assetLoader.getProjectileTexture(AssetLoader.PROJECTILE_TEXTURE.POISON)
                , this.fromVec.x
                , this.fromVec.y
                , 12
                , 12);
    }

    @Override
    public void update() {
        if (isAtTarget(toVec)) {
            this.hitTarget = true;
            this.target.damage(this.damage);
        } else {
            this.pos = new Vector2(this.fromVec.lerp(this.toVec, 0.2f));
        }
    }

    public boolean isAtTarget(Vector2 v) {
        if (v.x + 0.5f >= pos.x
                && v.x - 0.5f <= pos.x
                && v.y + 0.5f >= pos.y
                && v.y - 0.5f <= pos.y) {
            return true;
        } else {
            return false;
        }
    }
}
