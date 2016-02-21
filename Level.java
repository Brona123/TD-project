package fi.joutsijoki;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

import fi.joutsijoki.enemy.Enemy;
import fi.joutsijoki.enemy.Mob;
import fi.joutsijoki.enemy.Rat;

/**
 * Created by Sami on 28.1.2016.
 */
public class Level {
    private Array<Wave> waveArray;
    private int currentWaveIndex;
    private String currentLevel;

    public Level(int number) {
        if (number == 1) {
            initLevelOne();
        } else if (number == 2) {
            initLevelTwo();
        } else if (number == 3) {
            initLevelThree();
        }
    }

    private void initLevelOne() {
        currentLevel = "level-one-farmland.json";
        waveArray = new Array<Wave>();

        waveArray.add(new Wave(2, new Rat(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 30, 0, 0, 5)));
        /*
        waveArray.add(new Wave(10, new Rat(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 50, 0, 0, 7)));
        waveArray.add(new Wave(15, new Rat(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 80, 0, 0, 9)));
        waveArray.add(new Wave(25, new Rat(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 110, 0, 0, 11)));
        waveArray.add(new Wave(30, new Rat(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 130, 0, 0, 13)));
        waveArray.add(new Wave(3, new Rat(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 300, 0, 0, 30)));
        */
        currentWaveIndex = 1;
    }

    private void initLevelTwo() {
        currentLevel = "level-two-castle.json";
        waveArray = new Array<Wave>();

        waveArray.add(new Wave(8, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 80, 0, 0, 5)));
        waveArray.add(new Wave(16, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 100, 0, 0, 7)));
        waveArray.add(new Wave(22, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 120, 0, 0, 9)));
        waveArray.add(new Wave(30, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 150, 0, 0, 11)));
        waveArray.add(new Wave(40, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 200, 0, 0, 13)));

        currentWaveIndex = 1;
    }

    private void initLevelThree() {
        currentLevel = "level-three-mountain-pass.json";
        waveArray = new Array<Wave>();

        waveArray.add(new Wave(8, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 80, 0, 0, 5)));
        waveArray.add(new Wave(16, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 100, 0, 0, 7)));
        waveArray.add(new Wave(22, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 120, 0, 0, 9)));
        waveArray.add(new Wave(30, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 150, 0, 0, 11)));
        waveArray.add(new Wave(40, new Mob(AssetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 200, 0, 0, 13)));

        currentWaveIndex = 1;
    }

    public int getCurrentWaveIndex() {
        return this.currentWaveIndex;
    }

    public void setCurrentWaveIndex(int wave) {
        this.currentWaveIndex = wave;
    }

    public boolean isLevelComplete() {
        if (currentWaveIndex > waveArray.size) {
            return true;
        } else {
            return false;
        }
    }

    public Array<Wave> getWaveArr() {
        return this.waveArray;
    }

    public Wave getCurrentWave() {
        return this.waveArray.get(this.currentWaveIndex - 1);
    }

    public boolean spawningComplete(int mobsSpawned) {
        if (mobsSpawned == waveArray.get(currentWaveIndex - 1).waveAmount) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isWaveFinished(Array<Enemy> enemyArray) {
        if (enemyArray.size == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getCurrentLevelMap() {
        return this.currentLevel;
    }

    public class Wave {
        public int waveAmount;
        public Enemy enemy;

        public Wave(int waveAmount, Enemy enemy) {
            this.waveAmount = waveAmount;
            this.enemy = enemy;
        }
    }
}
