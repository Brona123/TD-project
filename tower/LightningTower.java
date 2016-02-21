package fi.joutsijoki.tower;

import com.badlogic.gdx.graphics.Texture;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.Utils;

/**
 * Created by Sami on 21.2.2016.
 */
public class LightningTower extends StaticTower {

    public LightningTower(int damage, int radius, int cooldown, int x, int y) {
        super(damage, radius, cooldown, x, y);
    }

    @Override
    public void shoot() {
        long shotTime = System.currentTimeMillis();
        long timeSinceLastShot = shotTime - timeAtLastShot;

        if (timeSinceLastShot > shotCooldown) {
            humanoidShooting = true;
            timeAtLastShot = System.currentTimeMillis();

            this.projectileList.add(new fi.joutsijoki.projectile.LightningProjectile(Utils.centerPos(this.pos), this.target, this.damage, this.shotCooldown));
        }
    }

    public Texture getTexture() {
        return AssetLoader.getTowerTexture(AssetLoader.TOWER_TEXTURE.LIGHTNING);
    }
}
