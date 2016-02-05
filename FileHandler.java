package fi.joutsijoki;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;

import fi.joutsijoki.pathfinding.Node;

/**
 * Created by Sami on 30.12.2015.
 */
public class FileHandler {
    private final String MAP_FOLDER = Constant.ROOT_ASSET_FOLDER + Constant.SERIALIZED_LEVELS_FOLDER;

    public void exportMap(GameField gameField, String fileName) {
        Json json = new Json();
        System.out.println(json.prettyPrint(gameField));

        FileHandle file = Gdx.files.local(MAP_FOLDER + fileName);
        file.writeString(json.prettyPrint(gameField), false);
    }

    public GameField loadMap(String fileName) {
        FileHandle file = null;

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            file = Gdx.files.internal(MAP_FOLDER + fileName);
        } else if (Gdx.app.getType() == Application.ApplicationType.Desktop){
            file = Gdx.files.local(MAP_FOLDER + fileName);
        }

        if (file != null) {
            String data = file.readString();
            Json json = new Json();
            GameField lab = json.fromJson(GameField.class, data);

            return lab;
        } else {
            return null;
        }
    }

    private void printNodes(Array<Node> nodes) {
        for(Node n : nodes) {
            System.out.println(n.getType());
        }
    }
}
