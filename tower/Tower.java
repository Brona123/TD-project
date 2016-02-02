package fi.joutsijoki.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import fi.joutsijoki.AnimationBuilder;
import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.AudioHandler;
import fi.joutsijoki.Effect;
import fi.joutsijoki.Labyrinth;
import fi.joutsijoki.Utils;
import fi.joutsijoki.enemy.Enemy;

/**
 * Created by Sami on 19.1.2016.
 */
public class Tower extends Sprite {
    private int radius;
    private Vector2 pos;
    private int damage;
    private TOWER_TYPE towerType;
    private int shotCooldown;
    private long timeAtLastShot;
    private boolean lockedAtTarget;
    private Enemy target;
    private Array<Projectile> projectileList = new Array<Projectile>();
    private Array<Projectile> projectilesToRemove;
    private Animation attackAnimation;
    private float stateTime;
    private boolean humanoidTower;
    private boolean humanoidShooting;

    public enum TOWER_TYPE {
        SHADOW,
        LIGHTNING,
        ICE,
        POISON,
        SHADOW_MAGE,
        DWARF
    }

    public Tower(int damage, int radius, int cooldown, int x, int y, AssetLoader.TOWER_TEXTURE texture) {
        this.damage = damage;
        this.radius = radius;
        this.shotCooldown = cooldown;
        this.pos = new Vector2(x, y);

        switch (texture) {
            case SHADOW:
                this.towerType = TOWER_TYPE.SHADOW;
                break;
            case LIGHTNING:
                this.towerType = TOWER_TYPE.LIGHTNING;
                break;
            case ICE:
                this.towerType = TOWER_TYPE.ICE;
                break;
            case POISON:
                this.towerType = TOWER_TYPE.POISON;
                break;
            case SHADOW_MAGE_IDLE:
                this.humanoidTower = true;
                this.towerType = TOWER_TYPE.SHADOW_MAGE;
                this.attackAnimation = AnimationBuilder.buildAnimation(Labyrinth.assetLoader.getTowerTexture(AssetLoader.TOWER_TEXTURE.SHADOW_MAGE_ATTACK)
                        , 3
                        , 1
                        , (float)this.shotCooldown / 1000.0f);
                this.attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);
                break;
            case DWARF_IDLE:
                this.humanoidTower = true;
                this.towerType = TOWER_TYPE.DWARF;
                this.attackAnimation = AnimationBuilder.buildAnimation(Labyrinth.assetLoader.getTowerTexture(AssetLoader.TOWER_TEXTURE.DWARF_ATTACK)
                        , 3
                        , 1
                        , (float)this.shotCooldown / 1000.0f);
        }
    }

    public void updateProjectiles() {
        projectilesToRemove = new Array<Projectile>();

        for (Projectile p : this.projectileList) {

            // Handle projectile update/draw, when mob dies inside range
            if (p.target.isAlive()) {
                p.update();
            } else {
                if (p.getClass() == LightningProjectile.class) {
                    lightningException(projectilesToRemove, p);
                } else {
                    projectilesToRemove.add(p);
                }

                setLockedAtTarget(false);
                humanoidShooting = false;
            }

            // Handle projectile update/draw, when mob escapes range
            if (targetEscaped()) {
                if (p.getClass() == LightningProjectile.class) {
                    lightningException(projectilesToRemove, p);
                } else {
                    projectilesToRemove.add(p);
                }

                setLockedAtTarget(false);
                humanoidShooting = false;
            }

            // Handle projectile removal, when projectile hits mob (different for lightning)
            if (p.getClass() == LightningProjectile.class) {
                LightningProjectile lp = (LightningProjectile)p;

                if (lp.isComplete()) {
                    projectilesToRemove.add(p);
                }
            } else {
                if (p.hitTarget() && p.getClass() != LightningProjectile.class) {
                    projectilesToRemove.add(p);
                }
            }
        }
    }

    private void lightningException(Array<Projectile> projectilesToRemove, Projectile p) {
        LightningProjectile lp = (LightningProjectile)p;

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

    public void shoot() {
        long shotTime = System.currentTimeMillis();
        long timeSinceLastShot = shotTime - timeAtLastShot;

        if (timeSinceLastShot > shotCooldown) {
            humanoidShooting = true;
            timeAtLastShot = System.currentTimeMillis();

            switch(towerType) {
                case SHADOW:
                    this.projectileList.add(new ShadowProjectile(Utils.centerPos(this.pos), this.target, this.damage));
                    break;
                case LIGHTNING:
                    this.projectileList.add(new LightningProjectile(Utils.centerPos(this.pos), this.target, this.damage, this.shotCooldown));
                    break;
                case ICE:
                    target.setEffect(Effect.TYPE.SLOW);
                    this.projectileList.add(new IceProjectile(Utils.centerPos(this.pos), this.target, this.damage));
                    break;
                case POISON:
                    target.setEffect(Effect.TYPE.DOT);
                    this.projectileList.add(new PoisonProjectile(Utils.centerPos(this.pos), this.target, this.damage));
                    AudioHandler.playEffect(AudioHandler.EFFECT.POISON);
                    break;
                case SHADOW_MAGE:
                    if (this.attackAnimation.isAnimationFinished(this.stateTime)) {
                        this.projectileList.add(new ShadowProjectile(Utils.centerPos(this.pos), this.target, this.damage));
                        humanoidShooting = false;
                        this.stateTime = 0f;
                    }
                    break;
                case DWARF:
                    if (this.attackAnimation.isAnimationFinished(this.stateTime)) {
                        this.projectileList.add(new HammerProjectile(Utils.centerPos(this.pos), this.target, this.damage));
                        humanoidShooting = false;
                        this.stateTime = 0f;
                    }
                    break;
            }

            if (!target.isAlive()) {
                humanoidShooting = false;
            }
        }
    }

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

        if (distance > this.radius * Labyrinth.CELL_WIDTH || this.target.isAtEnd()) {
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

    public Texture getTexture() {
        switch (towerType) {
            case SHADOW:
                return Labyrinth.assetLoader.getTowerTexture(AssetLoader.TOWER_TEXTURE.SHADOW);
            case LIGHTNING:
                return Labyrinth.assetLoader.getTowerTexture(AssetLoader.TOWER_TEXTURE.LIGHTNING);
            case ICE:
                return Labyrinth.assetLoader.getTowerTexture(AssetLoader.TOWER_TEXTURE.ICE);
            case POISON:
                return Labyrinth.assetLoader.getTowerTexture(AssetLoader.TOWER_TEXTURE.POISON);
        }

        return null;
    }

    public void drawTower(SpriteBatch batch) {
        if (humanoidShooting) {
            this.stateTime += Gdx.graphics.getDeltaTime();
        }

        if (this.target != null) {

            if (!target.isAlive()) {
                humanoidShooting = false;
                this.stateTime = 0f;
            }

            Vector2 fromVec = this.pos;
            Vector2 toVec = this.target.getPos();

            double atan = Math.atan2(toVec.y - fromVec.y, toVec.x - fromVec.x);
            double angle = atan * (180 / Math.PI);
            this.setRotation((float) angle);

            if (this.isHumanoidTower()) {
                batch.draw(this.getHumanoidTowerTexture()
                        , fromVec.x
                        , fromVec.y
                        , Labyrinth.CELL_WIDTH / 2
                        , Labyrinth.CELL_HEIGHT / 2
                        , Labyrinth.CELL_WIDTH
                        , Labyrinth.CELL_HEIGHT
                        , 1
                        , 1
                        , this.getRotation()
                        , true);

            } else {
                batch.draw(this.getTexture()
                        , this.pos.x
                        , this.pos.y + Labyrinth.CELL_HEIGHT
                        , Labyrinth.CELL_WIDTH
                        , Labyrinth.CELL_HEIGHT * -1);
            }
        } else {
            if (this.isHumanoidTower()) {
                batch.draw(this.getHumanoidTowerTexture()
                        , this.pos.x
                        , this.pos.y + Labyrinth.CELL_HEIGHT
                        , Labyrinth.CELL_WIDTH
                        , Labyrinth.CELL_HEIGHT * -1);
            } else {
                batch.draw(this.getTexture()
                        , this.pos.x
                        , this.pos.y + Labyrinth.CELL_HEIGHT
                        , Labyrinth.CELL_WIDTH
                        , Labyrinth.CELL_HEIGHT * -1);
            }
        }
    }

    public TextureRegion getHumanoidTowerTexture() {
        TextureRegion currentTexture = this.attackAnimation.getKeyFrame(this.stateTime, true);

        return currentTexture;
    }

    public boolean isHumanoidTower() {
        return this.humanoidTower;
    }
}
