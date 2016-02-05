package fi.joutsijoki.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.Constant;
import fi.joutsijoki.GameField;

/**
 * Created by Sami on 22.1.2016.
 */
public class HealthBar {
    private final int OFFSET_Y = -5;
    private final int HEALTHBAR_HEIGHT = 8;
    private final Texture FG = GameField.assetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.HEALTH_BAR_FG);
    private final Texture BG = GameField.assetLoader.getEnemyTexture(AssetLoader.ENEMY_TEXTURE.HEALTH_BAR_BG);

    public void draw(SpriteBatch batch, Vector2 pos, float healthPercentage) {
        batch.draw(BG, pos.x, pos.y + OFFSET_Y, Constant.CELL_WIDTH, HEALTHBAR_HEIGHT);
        batch.draw(FG, pos.x, pos.y + OFFSET_Y, Constant.CELL_WIDTH * healthPercentage, HEALTHBAR_HEIGHT);
    }
}
