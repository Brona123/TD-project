package fi.joutsijoki;

import com.badlogic.gdx.Gdx;

/**
 * Created by Sami on 2.2.2016.
 */
public class Constant {
    public static String ROOT_ASSET_FOLDER = "android/assets/";
    public static final String SERIALIZED_LEVELS_FOLDER = "serialized-levels/";

    public static final int MAP_OFFSET_X = 0;
    public static final int MAP_OFFSET_Y = 0;

    public static int MAP_WIDTH = 20;
    public static int MAP_HEIGHT = 20;

    public static int MAP_WIDTH_PIXELS;
    public static int MAP_HEIGHT_PIXELS;

    public static int CELL_WIDTH;
    public static int CELL_HEIGHT;

    public static void setDimensions(int width, int height) {
        MAP_WIDTH_PIXELS = width;
        MAP_HEIGHT_PIXELS = height;

        CELL_WIDTH = MAP_WIDTH_PIXELS / MAP_WIDTH;
        CELL_HEIGHT = MAP_HEIGHT_PIXELS / MAP_HEIGHT;
    }
}
