package fi.joutsijoki.tower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fi.joutsijoki.AnimationBuilder;
import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.GameField;
import fi.joutsijoki.Utils;

/**
 * Created by Sami on 21.2.2016.
 */
public class ShadowMageTower extends HumanoidTower {

    public ShadowMageTower(int damage, int radius, int cooldown, int x, int y) {
        super(damage, radius, cooldown, x, y);

        this.attackAnimation = AnimationBuilder.buildAnimation(GameField.assetLoader.getTowerTexture(AssetLoader.TOWER_TEXTURE.SHADOW_MAGE_ATTACK)
                , 3
                , 1
                , (float) this.shotCooldown / 1000.0f);
        this.attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    @Override
    public void shoot() {
        long shotTime = System.currentTimeMillis();
        long timeSinceLastShot = shotTime - timeAtLastShot;

        if (timeSinceLastShot > shotCooldown) {
            humanoidShooting = true;
            timeAtLastShot = System.currentTimeMillis();

            if (this.attackAnimation.isAnimationFinished(this.stateTime)) {
                this.projectileList.add(new fi.joutsijoki.projectile.HammerProjectile(Utils.centerPos(this.pos), this.target, this.damage));
                humanoidShooting = false;
                this.stateTime = 0f;
            }

            if (!target.isAlive()) {
                humanoidShooting = false;
            }
        }
    }

    public TextureRegion getHumanoidTexture() {
        TextureRegion currentTexture = this.attackAnimation.getKeyFrame(this.stateTime, true);

        return currentTexture;
    }
}
