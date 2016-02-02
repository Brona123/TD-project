package fi.joutsijoki;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Sami on 27.1.2016.
 */
public class Utils {

    public static Vector2 centerPos(Vector2 position) {
        Vector2 centerPos = new Vector2(position.x + Labyrinth.CELL_WIDTH / 2, position.y + Labyrinth.CELL_HEIGHT / 2);

        return centerPos;
    }
}
