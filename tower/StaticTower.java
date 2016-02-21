package fi.joutsijoki.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.Constant;

/**
 * Created by Sami on 21.2.2016.
 */
public abstract class StaticTower extends Tower {

    public StaticTower(int damage, int radius, int cooldown, int x, int y) {
        super(damage, radius, cooldown, x, y);
    }

    public abstract Texture getTexture();

    public void drawTower(SpriteBatch batch) {
        batch.draw(this.getTexture()
                , this.pos.x
                , this.pos.y + Constant.CELL_HEIGHT
                , Constant.CELL_WIDTH
                , Constant.CELL_HEIGHT * -1);
    }
}
