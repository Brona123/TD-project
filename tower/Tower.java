package fi.joutsijoki.tower;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.Constant;
import fi.joutsijoki.enemy.Enemy;
import fi.joutsijoki.projectile.Projectile;

/**
 * Created by Sami on 19.1.2016.
 */
public abstract class Tower extends Sprite {
    private int radius;
    private boolean lockedAtTarget;
    private Array<fi.joutsijoki.projectile.Projectile> projectilesToRemove;

    protected long timeAtLastShot;
    protected Vector2 pos;
    protected int damage;
    protected int shotCooldown;
    protected Enemy target;
    protected Array<fi.joutsijoki.projectile.Projectile> projectileList = new Array<fi.joutsijoki.projectile.Projectile>();
    protected Animation attackAnimation;
    protected float stateTime;
    protected boolean humanoidShooting;

    public Tower(int damage, int radius, int cooldown, int x, int y) {
        this.damage = damage;
        this.radius = radius;
        this.shotCooldown = cooldown;
        this.pos = new Vector2(x, y);
    }

    public static Tower createTower(int damage, int radius, int cooldown, int x, int y, AssetLoader.TOWER_TEXTURE towerTexture) {
        switch (towerTexture) {
            case SHADOW:
                return new ShadowTower(damage, radius, cooldown, x, y);
            case LIGHTNING:
                return new LightningTower(damage, radius, cooldown, x, y);
            case ICE:
                return new IceTower(damage, radius, cooldown, x, y);
            case POISON:
                return new PoisonTower(damage, radius, cooldown, x, y);
            case SHADOW_MAGE_IDLE:
                return new ShadowMageTower(damage, radius, cooldown, x, y);
            case DWARF_IDLE:
                return new DwarfTower(damage, radius, cooldown, x, y);
            default:
                return null;
        }
    }

    public void updateProjectiles() {
        projectilesToRemove = new Array<fi.joutsijoki.projectile.Projectile>();

        for (fi.joutsijoki.projectile.Projectile p : this.projectileList) {

            // Handle projectile update/draw, when mob dies inside range
            if (p.getTarget().isAlive()) {
                p.update();
            } else {
                if (p.getClass() == fi.joutsijoki.projectile.LightningProjectile.class) {
                    lightningException(projectilesToRemove, p);
                } else {
                    projectilesToRemove.add(p);
                }

                setLockedAtTarget(false);
                humanoidShooting = false;
            }

            // Handle projectile update/draw, when mob escapes range
            if (targetEscaped()) {
                if (p.getClass() == fi.joutsijoki.projectile.LightningProjectile.class) {
                    lightningException(projectilesToRemove, p);
                } else {
                    projectilesToRemove.add(p);
                }

                setLockedAtTarget(false);
                humanoidShooting = false;
            }

            // Handle projectile removal, when projectile hits mob (different for lightning)
            if (p.getClass() == fi.joutsijoki.projectile.LightningProjectile.class) {
                fi.joutsijoki.projectile.LightningProjectile lp = (fi.joutsijoki.projectile.LightningProjectile)p;

                if (lp.isComplete()) {
                    projectilesToRemove.add(p);
                }
            } else {
                if (p.hitTarget() && p.getClass() != fi.joutsijoki.projectile.LightningProjectile.class) {
                    projectilesToRemove.add(p);
                }
            }
        }
    }

    private void lightningException(Array<fi.joutsijoki.projectile.Projectile> projectilesToRemove, fi.joutsijoki.projectile.Projectile p) {
        fi.joutsijoki.projectile.LightningProjectile lp = (fi.joutsijoki.projectile.LightningProjectile)p;

        if (lp.isComplete()) {
            projectilesToRemove.add(p);
        } else {
            p.update();
        }
    }

    public void drawProjectiles(SpriteBatch batch) {
        if (this.projectileList == null) {
            return;
        }

        for (Projectile p : this.projectileList) {
            p.draw(batch);
        }

        this.projectileList.removeAll(projectilesToRemove, true);
    }

    public void clearProjectiles() {
        this.projectileList.clear();
    }

    public abstract void shoot();

    public int getRadius() {
        return radius;
    }

    public Vector2 getPos() {
        return this.pos;
    }

    public boolean isLockedAtTarget() {
        return this.lockedAtTarget;
    }

    public void setLockedAtTarget(boolean flag) {
        this.lockedAtTarget = flag;

        if (!flag) {
            this.humanoidShooting = false;
            this.stateTime = 0f;
        }
    }

    public boolean isTargetDead() {
        return !this.target.isAlive();
    }

    public boolean targetEscaped() {
        if (this.target == null) {
            return true;
        }
        float distance = this.pos.dst(target.getPos());

        if (distance > this.radius * Constant.CELL_WIDTH || this.target.isAtEnd()) {
            setLockedAtTarget(false);
            return true;
        } else {
            return false;
        }
    }

    public void updateTower() {
        if (targetEscaped()) {
            setLockedAtTarget(false);
        }
    }

    public void setTarget(Enemy e) {
        this.target = e;
        setLockedAtTarget(true);
    }

    public Enemy getTarget() {
        return this.target;
    }

    public abstract void drawTower(SpriteBatch batch);
}
