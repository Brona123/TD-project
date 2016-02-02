package fi.joutsijoki;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import fi.joutsijoki.enemy.Enemy;

/**
 * Created by Sami on 23.1.2016.
 */
public class Effect {
    private TYPE type;
    private int frameColumns;
    private int frameRows;
    private Texture animFrame;
    private Animation effectAnimation;
    private TextureRegion currentFrame;
    private float stateTime;

    public enum TYPE {
        SLOW,
        DOT
    }

    public Effect(TYPE type) {
        this.type = type;

        switch (type) {
            case SLOW:
                slowCreate();
                break;
            case DOT:
                dotCreate();
                break;
        }
    }

    private void slowCreate() {
        this.animFrame = AssetLoader.iceSpriteSheet;
        this.frameColumns = 3;
        this.frameRows = 1;
        this.stateTime = 0f;

        this.effectAnimation = AnimationBuilder.buildAnimation(this.animFrame, this.frameColumns, this.frameRows, 0.25f);
    }

    private void dotCreate() {
        this.animFrame = AssetLoader.poisonSpriteSheet;
        this.frameColumns = 3;
        this.frameRows = 1;
        this.stateTime = 0f;

        this.effectAnimation = AnimationBuilder.buildAnimation(this.animFrame, this.frameColumns, this.frameRows, 0.25f);
    }

    public void tick(Enemy e) {
        switch (this.type) {
            case SLOW:
                slowTick(e);
                break;
            case DOT:
                dotTick(e);
                break;
        }
    }

    public void draw(SpriteBatch batch, float x, float y, int width, int height) {
        this.stateTime += Gdx.graphics.getDeltaTime();
        this.currentFrame = this.effectAnimation.getKeyFrame(this.stateTime, true);
        batch.draw(this.currentFrame, x, y, width, height);
    }

    private void slowTick(Enemy e) {
        if (e.getSpeed() > 0.02f) {
            e.setSpeed(e.getSpeed() - 0.001f);
        }
    }

    private void dotTick(Enemy e) {
        e.damage(1);
    }
}
