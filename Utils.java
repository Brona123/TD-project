package fi.joutsijoki;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by Sami on 27.1.2016.
 */
public class Utils {

    public static Vector2 centerPos(Vector2 position) {
        Vector2 centerPos = new Vector2(position.x + Constant.CELL_WIDTH / 2, position.y + Constant.CELL_HEIGHT / 2);

        return centerPos;
    }

    public static SpriteDrawable newSpriteDrawable(Texture t) {
        return new SpriteDrawable(new Sprite(t));
    }

    public static ImageButton buildImageButton(Texture t, String name) {
        ImageButton.ImageButtonStyle ibs = new ImageButton.ImageButtonStyle();
        ibs.imageUp = new SpriteDrawable(new Sprite(t));
        ImageButton ib = new ImageButton(ibs);
        ib.setName(name);
        return ib;
    }
}
