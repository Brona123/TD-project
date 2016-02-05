package fi.joutsijoki.lightning;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import fi.joutsijoki.AssetLoader;

/**
 * Created by Sami on 20.1.2016.
 */
public class Line {
    public Vector2 a;
    public Vector2 b;
    public float thickness;

    public Line() { }

    public Line(Vector2 a, Vector2 b, float thickness) {
        this.a = new Vector2(a);
        this.b = new Vector2(b);
        this.thickness = thickness;
    }

    public void draw(SpriteBatch spriteBatch, Color color) {
        Vector2 tangent = new Vector2(b).sub(new Vector2(a));
        float theta = (float)Math.toDegrees(Math.atan2(tangent.y, tangent.x));

        final float imageThickness = 80;
        float thicknessScale = this.thickness / imageThickness;

        Color prevColor = spriteBatch.getColor();
        spriteBatch.setColor(color);

        int srcFunc = spriteBatch.getBlendSrcFunc();
        int dstFunc = spriteBatch.getBlendDstFunc();
        spriteBatch.setBlendFunction(spriteBatch.getBlendSrcFunc(), Pixmap.Blending.SourceOver.ordinal());

        spriteBatch.draw(AssetLoader.lightningLine, a.x, a.y, 0, thickness / 2, getLength(), thickness, 1, 1, theta);
        spriteBatch.draw(AssetLoader.lightningHalfCircle, a.x, a.y, 0, thickness / 2, thicknessScale * AssetLoader.lightningHalfCircle.getRegionWidth(), thickness, 1, 1, theta);
        spriteBatch.draw(AssetLoader.lightningHalfCircle, b.x, b.y, 0, thickness / 2, thicknessScale * AssetLoader.lightningHalfCircle.getRegionWidth(), thickness, 1, 1, theta);

        spriteBatch.setColor(prevColor);
        spriteBatch.setBlendFunction(srcFunc, dstFunc);
    }

    public float getLength(){
        float len = (float)Math.sqrt((a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y));
        return len;
    }
}
