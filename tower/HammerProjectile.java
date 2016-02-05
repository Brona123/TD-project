package fi.joutsijoki.tower;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.GameField;
import fi.joutsijoki.Utils;
import fi.joutsijoki.enemy.Enemy;

/**
 * Created by Sami on 28.1.2016.
 */
public class HammerProjectile extends Projectile {
    private float angle = 0f;
    private TextureRegion texture;
    private int textureWidth = 16;
    private int textureHeight = 16;
    private float speed = 0.05f;

    public HammerProjectile(Vector2 fromVec, Enemy target, int damage) {
        this.pos = fromVec;
        this.fromVec = fromVec;
        Vector2 targetPos = Utils.centerPos(target.getPos());
        this.toVec = new Vector2(targetPos.x - textureWidth / 2, targetPos.y + textureHeight / 2);
        this.target = target;
        this.damage = damage;
        this.texture = new TextureRegion(GameField.assetLoader.getProjectileTexture(AssetLoader.PROJECTILE_TEXTURE.DWARF_HAMMER));
    }

    @Override
    public void update() {
        this.angle += 20f;

        if (isAtTarget(toVec)) {
            this.hitTarget = true;
            this.target.damage(this.damage);
        } else {
            this.delta += this.speed;
            this.fromVec.lerp(this.toVec, this.delta);
        }
    }

    // TODO Mobiili port

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.texture
                , this.fromVec.x
                , this.fromVec.y
                , 8
                , 8
                , this.textureWidth
                , this.textureHeight
                , 1
                , 1
                , this.angle);
    }

    public boolean isAtTarget(Vector2 v) {
        if (this.delta >= 1f) {
            return true;
        } else {
            return false;
        }
    }
}
