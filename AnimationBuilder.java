package fi.joutsijoki;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Sami on 24.1.2016.
 */
public class AnimationBuilder {

    public static Animation buildAnimation(Texture animFrame, int frameColumns, int frameRows, float animSpeed) {
        TextureRegion[][] tmp = TextureRegion.split(animFrame
                , animFrame.getWidth() / frameColumns
                , animFrame.getHeight() / frameRows);

        TextureRegion[] animFrames = new TextureRegion[frameColumns * frameRows];
        int index = 0;

        for (int i = 0; i < frameRows; i++) {
            for (int j = 0; j < frameColumns; j++) {
                animFrames[index++] = tmp[i][j];
            }
        }

        return new Animation(animSpeed, animFrames);
    }
}
