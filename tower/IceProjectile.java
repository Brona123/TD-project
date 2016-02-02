package fi.joutsijoki.tower;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.Labyrinth;
import fi.joutsijoki.Utils;
import fi.joutsijoki.enemy.Enemy;

/**
 * Created by Sami on 22.1.2016.
 */
public class IceProjectile extends Projectile {

    public IceProjectile(Vector2 fromVec, Enemy target, int damage) {
        this.pos = new Vector2(fromVec);
        this.fromVec = new Vector2(fromVec);
        this.toVec = Utils.centerPos(target.getPos());
        this.target = target;
        this.damage = damage;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(Labyrinth.assetLoader.getProjectileTexture(AssetLoader.PROJECTILE_TEXTURE.ICE)
                , this.fromVec.x
                , this.fromVec.y
                , 20
                , 20);
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
