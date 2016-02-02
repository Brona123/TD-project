package fi.joutsijoki.tower;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.Labyrinth;
import fi.joutsijoki.Utils;
import fi.joutsijoki.enemy.Enemy;

/**
 * Created by Sami on 28.1.2016.
 */
public class HammerProjectile extends Projectile {
    private float angle = 0f;
    private TextureRegion texture;
    private int textureWidth = 6;
    private int textureHeight = 6;

    public HammerProjectile(Vector2 fromVec, Enemy target, int damage) {
        this.pos = fromVec;
        this.fromVec = fromVec;
        Vector2 targetPos = Utils.centerPos(target.getPos());
        this.toVec = new Vector2(targetPos.x - textureWidth / 2, targetPos.y + textureHeight / 2);
        this.target = target;
        this.damage = damage;
        this.texture = new TextureRegion(Labyrinth.assetLoader.getProjectileTexture(AssetLoader.PROJECTILE_TEXTURE.DWARF_HAMMER));
    }

    @Override
    public void update() {
        this.angle += 20f;

        if (isAtTarget(toVec)) {
            this.hitTarget = true;
            this.target.damage(this.damage);
        } else {
            this.fromVec.lerp(this.toVec, 0.1f);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(this.texture
                , this.fromVec.x
                , this.fromVec.y
                , 4
                , 4
                , this.textureWidth
                , this.textureHeight
                , 1
                , 1
                , this.angle);
    }

    public boolean isAtTarget(Vector2 v) {
        /*
        Rectangle r = new Rectangle(this.pos.x, this.pos.y, this.textureWidth, this.textureHeight);

        Vector2 targetPos = this.target.getPos();
        Rectangle r2 = new Rectangle(targetPos.x, targetPos.y, Labyrinth.CELL_WIDTH, Labyrinth.CELL_HEIGHT);

        if (r.overlaps(r2)) {
            return true;
        } else {
            return false;
        }
        */


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
