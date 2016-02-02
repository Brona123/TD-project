package fi.joutsijoki;

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

    public void exportMap(Labyrinth labyrinth, String fileName) {
        Json json = new Json();
        System.out.println(json.prettyPrint(labyrinth));

        FileHandle file = Gdx.files.local(MAP_FOLDER + fileName);
        file.writeString(json.prettyPrint(labyrinth), false);
    }

    public Labyrinth loadMap(String fileName) {
        FileHandle file = Gdx.files.local(MAP_FOLDER + fileName);
        String data = file.readString();
        Json json = new Json();
        Labyrinth lab = json.fromJson(Labyrinth.class, data);

        return lab;
    }

    private void printNodes(Array<Node> nodes) {
        for(Node n : nodes) {
            System.out.println(n.getType());
        }
    }
}
