package fi.joutsijoki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Sami on 3.1.2016.
 */
public class AssetLoader {
    private HashMap<OBJECT_TEXTURE, Texture> objectTextureMap;
    private HashMap<PATHFINDING_TEXTURE, Texture> pathfindingTextureMap;
    private HashMap<HUD_TEXTURE, Texture> hudTextureMap;
    private HashMap<ENEMY_TEXTURE, Texture> enemyTextureMap;
    private HashMap<TOWER_TEXTURE, Texture> towerTextureMap;
    private HashMap<PROJECTILE_TEXTURE, Texture> projectileTextureMap;
    private HashMap<LEVEL_TEXTURE, Texture> levelTextureMap;

    public static TextureRegion lightningLine;
    public static TextureRegion lightningHalfCircle;

    // Effect sprite sheets
    public static Texture iceSpriteSheet;
    public static Texture poisonSpriteSheet;
    public static Texture mobWalkAnimation;

    // Audio files
    public static Array<Sound> lightningEffects;
    public static Sound poisonEffect;

    public enum LEVEL_TEXTURE {
        LEVEL_ONE
    }

    public enum OBJECT_TEXTURE {
        BUSH,
        ROCK,
        POND,
        TREE,
        TREE_2
    }

    public enum PATHFINDING_TEXTURE {
        WALL,
        ROAD,
        START,
        END,
        PATH
    }

    public enum ENEMY_TEXTURE {
        MOB,
        RAT,
        HEALTH_BAR_BG,
        HEALTH_BAR_FG
    }

    public enum HUD_TEXTURE {
        CHECKBOX_ON,
        CHECKBOX_OFF,
        BORDER,
        DIALOG_BG
    }

    public enum TOWER_TEXTURE {
        SHADOW,
        LIGHTNING,
        ICE,
        POISON,
        SHADOW_MAGE_IDLE,
        SHADOW_MAGE_ATTACK,
        DWARF_IDLE,
        DWARF_ATTACK
    }

    public enum PROJECTILE_TEXTURE {
        SHADOW,
        LIGHTNING,
        ICE,
        POISON,
        DWARF_HAMMER
    }

    public AssetLoader() {
        // Object layer
        objectTextureMap = new HashMap<OBJECT_TEXTURE, Texture>();
        objectTextureMap.put(OBJECT_TEXTURE.BUSH, newTexture("bush.png"));
        objectTextureMap.put(OBJECT_TEXTURE.POND, newTexture("pond.png"));
        objectTextureMap.put(OBJECT_TEXTURE.ROCK, newTexture("rock.png"));
        objectTextureMap.put(OBJECT_TEXTURE.TREE, newTexture("tree.png"));
        objectTextureMap.put(OBJECT_TEXTURE.TREE_2, newTexture("tree2.png"));

        // Pathfinding layer
        pathfindingTextureMap = new HashMap<PATHFINDING_TEXTURE, Texture>();
        pathfindingTextureMap.put(PATHFINDING_TEXTURE.WALL, newTexture("wall.png"));
        pathfindingTextureMap.put(PATHFINDING_TEXTURE.ROAD, newTexture("road.png"));
        pathfindingTextureMap.put(PATHFINDING_TEXTURE.START, newTexture("start.png"));
        pathfindingTextureMap.put(PATHFINDING_TEXTURE.END, newTexture("end.png"));
        pathfindingTextureMap.put(PATHFINDING_TEXTURE.PATH, newTexture("path.png"));


        // Hud textures
        hudTextureMap = new HashMap<HUD_TEXTURE, Texture>();
        hudTextureMap.put(HUD_TEXTURE.CHECKBOX_ON, newTexture("checkbox-on.png", "hud/"));
        hudTextureMap.put(HUD_TEXTURE.CHECKBOX_OFF, newTexture("checkbox-off.png", "hud/"));
        hudTextureMap.put(HUD_TEXTURE.BORDER, newTexture("border.png", "hud/"));
        hudTextureMap.put(HUD_TEXTURE.DIALOG_BG, newTexture("dialog-bg.png", "hud/"));

        // Enemy textures
        enemyTextureMap = new HashMap<ENEMY_TEXTURE, Texture>();
        enemyTextureMap.put(ENEMY_TEXTURE.MOB, newTexture("mob-walk-spritesheet.png", "enemies/"));
        enemyTextureMap.put(ENEMY_TEXTURE.RAT, newTexture("rat-walk-spritesheet.png", "enemies/"));
        enemyTextureMap.put(ENEMY_TEXTURE.HEALTH_BAR_BG, newTexture("health-bar-bg.png", "enemies/"));
        enemyTextureMap.put(ENEMY_TEXTURE.HEALTH_BAR_FG, newTexture("health-bar-fg.png", "enemies/"));

        // Tower textures
        towerTextureMap = new HashMap<TOWER_TEXTURE, Texture>();
        towerTextureMap.put(TOWER_TEXTURE.ICE, newTexture("ice-tower.png", "towers/"));
        towerTextureMap.put(TOWER_TEXTURE.SHADOW, newTexture("shadow-tower.png", "towers/"));
        towerTextureMap.put(TOWER_TEXTURE.POISON, newTexture("poison-tower.png", "towers/"));
        towerTextureMap.put(TOWER_TEXTURE.LIGHTNING, newTexture("lightning-tower.png", "towers/"));
        towerTextureMap.put(TOWER_TEXTURE.SHADOW_MAGE_IDLE, newTexture("shadow-mage-idle.png", "towers/humanoid/"));
        towerTextureMap.put(TOWER_TEXTURE.SHADOW_MAGE_ATTACK, newTexture("shadow-mage-attack-spritesheet.png", "towers/humanoid/"));
        towerTextureMap.put(TOWER_TEXTURE.DWARF_IDLE, newTexture("dwarf-idle.png", "towers/humanoid/"));
        towerTextureMap.put(TOWER_TEXTURE.DWARF_ATTACK, newTexture("dwarf-attack-spritesheet-2.png", "towers/humanoid/"));

        // Projectile textures
        projectileTextureMap = new HashMap<PROJECTILE_TEXTURE, Texture>();
        projectileTextureMap.put(PROJECTILE_TEXTURE.ICE, newTexture("ice-projectile.png", "projectiles/"));
        projectileTextureMap.put(PROJECTILE_TEXTURE.LIGHTNING, newTexture("chain-lightning.png", "projectiles/"));
        projectileTextureMap.put(PROJECTILE_TEXTURE.POISON, newTexture("poison-projectile.png", "projectiles/"));
        projectileTextureMap.put(PROJECTILE_TEXTURE.SHADOW, newTexture("shadow-projectile.png", "projectiles/"));
        projectileTextureMap.put(PROJECTILE_TEXTURE.DWARF_HAMMER, newTexture("hammer.png", "projectiles/"));

        // Level textures
        levelTextureMap = new HashMap<LEVEL_TEXTURE, Texture>();
        levelTextureMap.put(LEVEL_TEXTURE.LEVEL_ONE, newTexture("level-1.png", "levels/"));

        lightningLine = newTextureRegion("lightning-middle-point.png", "projectiles/");
        lightningHalfCircle = newTextureRegion("lightning-end-point.png", "projectiles/");

        iceSpriteSheet = newTexture("ice-effect-spritesheet.png", "effects/");
        poisonSpriteSheet = newTexture("poison-effect-spritesheet.png", "effects/");
        mobWalkAnimation = newTexture("mob-walk-spritesheet.png", "enemies/");

        // Audio effects
        lightningEffects = new Array<Sound>();
        lightningEffects.add(newSound("lightning-1.wav", "audio/effects/"));
        lightningEffects.add(newSound("lightning-2.wav", "audio/effects/"));
        lightningEffects.add(newSound("lightning-3.wav", "audio/effects/"));
        //lightningEffects.add(newSound("lightning-4.wav", "audio/effects/"));
        poisonEffect = newSound("poison.wav", "audio/effects/");
    }

    public Texture getCurrentLevelBackground(String levelUrl) {
        return newTexture(levelUrl, "levels/");
    }

    public Sound newSound(String soundFile, String folder) {
        return Gdx.audio.newSound(Gdx.files.internal(Constant.ROOT_ASSET_FOLDER + folder + soundFile));
    }

    public Texture getLevelTexture(LEVEL_TEXTURE texture) {
        return this.levelTextureMap.get(texture);
    }

    public TextureRegion newTextureRegion(String file, String folder) {
        Texture t = newTexture(file, folder);
        return new TextureRegion(t, t.getWidth(), t.getHeight());
    }

    public Texture newTexture(String file) {
        return new Texture(Gdx.files.internal(Constant.ROOT_ASSET_FOLDER + file));
    }

    public Texture newTexture(String file, String folder) {
        return new Texture(Gdx.files.internal(Constant.ROOT_ASSET_FOLDER + folder + file));
    }

    public Texture getObstacleTexture(OBJECT_TEXTURE object) {
        return objectTextureMap.get(object);
    }

    public Texture getTowerTexture(TOWER_TEXTURE tower) {
        return towerTextureMap.get(tower);
    }

    public Texture getProjectileTexture(PROJECTILE_TEXTURE projectileTexture) {
        return projectileTextureMap.get(projectileTexture);
    }

    public Texture getPathfindingTexture(PATHFINDING_TEXTURE pathfindingTexture) {
        return pathfindingTextureMap.get(pathfindingTexture);
    }

    public Texture getHudTexture(HUD_TEXTURE hudTexture) {
        return hudTextureMap.get(hudTexture);
    }

    public Texture randomEnemyTexture() {
        Random random = new Random();
        return enemyTextureMap.get(ENEMY_TEXTURE.values()[random.nextInt(ENEMY_TEXTURE.values().length)]);
    }

    public Texture getEnemyTexture(ENEMY_TEXTURE enemyTexture) {
        return enemyTextureMap.get(enemyTexture);
    }
}
