package fi.joutsijoki.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import fi.joutsijoki.AssetLoader;
import fi.joutsijoki.GameField;
import fi.joutsijoki.tower.Tower;

/**
 * Created by Sami on 31.12.2015.
 */
public class Node implements IndexedNode<Node>, Json.Serializable {
    private Array<Connection<Node>> connections = new Array<Connection<Node>>();
    private int type;
    private int index;

    public boolean start;
    public boolean end;
    public boolean path;
    public boolean buildableLocation;

    public float x;
    public float y;

    public AssetLoader.OBJECT_TEXTURE object;
    public AssetLoader.TOWER_TEXTURE tower;
    public Tower towerRef;

    private int pathStartNodeIndex;

    public Node() {

    }

    public Node(int type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;

        index = Node.Indexer.getIndex();
    }

    public Node(Node node, float x, float y) {
        this.type = node.type;
        this.index = node.index;
        this.start = node.start;
        this.end = node.end;
        this.path = node.path;
        this.buildableLocation = node.buildableLocation;

        this.x = x;
        this.y = y;

        this.object = node.object;
        this.tower = node.tower;
        this.towerRef = node.towerRef;

        this.pathStartNodeIndex = node.pathStartNodeIndex;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    public void createConnection(Node toNode, float cost) {
        if (this.type == GameField.WALL || toNode.type == GameField.WALL) {
            // No connections to walls
        } else {
            connections.add(new ConnectionImpl(this, toNode, cost));
        }
    }

    @Override
    public void write(Json json) {
        json.writeValue("type", type);
        json.writeValue("index", index);
        json.writeValue("start", start);
        json.writeValue("end", end);
        json.writeValue("path", path);
        json.writeValue("x", x);
        json.writeValue("y", y);
        json.writeValue("selected-object", enumToInt(object));
        json.writeValue("path-start-node-index", this.pathStartNodeIndex);
        json.writeValue("buildable-location", this.buildableLocation);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.type = jsonData.getInt("type");
        this.index = jsonData.getInt("index");
        this.start = jsonData.getBoolean("start");
        this.end = jsonData.getBoolean("end");
        this.path = jsonData.getBoolean("path");
        this.x = jsonData.getFloat("x");
        this.y = jsonData.getFloat("y");
        this.object = intToEnum(jsonData.getInt("selected-object"));
        this.pathStartNodeIndex = jsonData.getInt("path-start-node-index");

        try {
            this.buildableLocation = jsonData.getBoolean("buildable-location");
        } catch(IllegalArgumentException e) {
            System.out.println("No buildable location");
        }
    }

    private static class Indexer {
        private static int index = 0;

        public static int getIndex() {
            return index++;
        }
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int enumToInt(AssetLoader.OBJECT_TEXTURE object) {
        if (object == null) {
            return -1;
        }

        switch(object) {
            case BUSH:
                return 0;
            case ROCK:
                return 1;
            case TREE:
                return 2;
            case TREE_2:
                return 3;
            case POND:
                return 4;
            default:
                return -1;
        }
    }

    public AssetLoader.OBJECT_TEXTURE intToEnum(int i) {
        switch(i) {
            case 0:
                return AssetLoader.OBJECT_TEXTURE.BUSH;
            case 1:
                return AssetLoader.OBJECT_TEXTURE.ROCK;
            case 2:
                return AssetLoader.OBJECT_TEXTURE.TREE;
            case 3:
                return AssetLoader.OBJECT_TEXTURE.TREE_2;
            case 4:
                return AssetLoader.OBJECT_TEXTURE.POND;
            default:
                return null;
        }
    }

    public void setPathStartNodeIndex(int n) {
        this.pathStartNodeIndex = n;
    }

    public int getPathStartNodeIndex() {
        return this.pathStartNodeIndex;
    }
}
