package fi.joutsijoki.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.Constant;

/**
 * Created by Sami on 21.2.2016.
 */
public abstract class HumanoidTower extends Tower {

    public HumanoidTower(int damage, int radius, int cooldown, int x, int y) {
        super(damage, radius, cooldown, x, y);

    }

    public abstract TextureRegion getHumanoidTexture();

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

            batch.draw(this.getHumanoidTexture()
                    , fromVec.x
                    , fromVec.y
                    , Constant.CELL_WIDTH / 2
                    , Constant.CELL_HEIGHT / 2
                    , Constant.CELL_WIDTH
                    , Constant.CELL_HEIGHT
                    , 1
                    , 1
                    , this.getRotation()
                    , true);
        } else {
            batch.draw(this.getHumanoidTexture()
                    , this.pos.x
                    , this.pos.y + Constant.CELL_HEIGHT
                    , Constant.CELL_WIDTH
                    , Constant.CELL_HEIGHT * -1);
        }
    }
}
