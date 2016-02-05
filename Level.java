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
    private Texture backgroundTexture;

    public Level(int number) {
        if (number == 1) {
            waveArray = new Array<Wave>();

            this.backgroundTexture = GameField.assetLoader.getLevelTexture(AssetLoader.LEVEL_TEXTURE.LEVEL_ONE);

            waveArray.add(new Wave(5, new Mob(GameField.assetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 50, 0, 0, 5)));
            waveArray.add(new Wave(10, new Mob(GameField.assetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 70, 0, 0, 7)));
            waveArray.add(new Wave(15, new Mob(GameField.assetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 90, 0, 0, 9)));
            waveArray.add(new Wave(25, new Mob(GameField.assetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.MOB), 110, 0, 0, 11)));
            waveArray.add(new Wave(30, new Rat(GameField.assetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.RAT), 130, 0, 0, 13)));

            currentWaveIndex = 1;
        }
    }

    public Texture getLevelTexture() {
        return this.backgroundTexture;
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

    public class Wave {
        public int waveAmount;
        public Enemy enemy;

        public Wave(int waveAmount, Enemy enemy) {
            this.waveAmount = waveAmount;
            this.enemy = enemy;
        }
    }
}
