package fi.joutsijoki;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import fi.joutsijoki.pathfinding.GraphImpl;
import fi.joutsijoki.pathfinding.GraphPathImpl;
import fi.joutsijoki.pathfinding.Node;
import fi.joutsijoki.tower.Tower;

/**
 * Created by Sami on 30.12.2015.
 */
public class Labyrinth implements Json.Serializable {
    private Array<Node> nodes = new Array<Node>();
    private GraphImpl graphImpl;

    public static final int WALL = 0;
    public static final int ROAD = 1;

    public static final int MAP_WIDTH = 30;
    public static final int MAP_HEIGHT = 30;

    public static final int MAP_WIDTH_PIXELS = 900;
    public static final int MAP_HEIGHT_PIXELS = 900;

    public static final int CELL_WIDTH = MAP_WIDTH_PIXELS / MAP_WIDTH;
    public static final int CELL_HEIGHT = MAP_HEIGHT_PIXELS / MAP_HEIGHT;

    private Node startNode;
    private Node endNode;
    private PathHandler pathHandler;

    public static final int MAP_OFFSET_X = 50;
    public static final int MAP_OFFSET_Y = 50;

    private final float DIAGONAL_COST = 14;
    private final float AXIAL_2D_COST = 10;

    private ShapeRenderer shapeRenderer;

    public static AssetLoader assetLoader;

    public static boolean drawGrid;
    public static boolean drawConnections;
    public static boolean drawPaths;
    public static boolean drawObjects;

    private String currentLevelUrl = "level-1.png";
    private Texture currentLevelBackground;

    public static int money = 0;

    public Labyrinth() {
        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Node node = new Node(WALL, j * CELL_WIDTH + MAP_OFFSET_X, i * CELL_HEIGHT + MAP_OFFSET_Y);
                nodes.add(node);
            }
        }

        init(nodes);
    }

    public Labyrinth(Array<Node> localNodes) {
        init(localNodes);
    }

    private void init(Array<Node> nodes) {
        this.nodes = nodes;

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Node targetNode = this.nodes.get(MAP_WIDTH * i + j);

                createAdjacentConnections(targetNode, i, j, false);
            }
        }

        this.graphImpl = new GraphImpl(this.nodes);

        assetLoader = new AssetLoader();
        shapeRenderer = new ShapeRenderer();
        this.currentLevelBackground = assetLoader.getCurrentLevelBackground(this.currentLevelUrl);
    }

    public void createAdjacentConnections(Node targetNode, int i, int j, boolean reconnect) {
        if (i != 0) {
            Node upNode = nodes.get(MAP_WIDTH * (i - 1) + j);
            targetNode.createConnection(upNode, AXIAL_2D_COST);

            if (reconnect) {
                upNode.createConnection(targetNode, AXIAL_2D_COST);
            }

            if (j != 0) {
                Node leftUpNode = nodes.get(MAP_WIDTH * (i - 1) + (j - 1));
                targetNode.createConnection(leftUpNode, DIAGONAL_COST);

                if (reconnect) {
                    leftUpNode.createConnection(targetNode, DIAGONAL_COST);
                }
            }

            if (j != MAP_WIDTH - 1) {
                Node rightUpNode = nodes.get(MAP_WIDTH * (i - 1) + j + 1);
                targetNode.createConnection(rightUpNode, DIAGONAL_COST);

                if (reconnect) {
                    rightUpNode.createConnection(targetNode, DIAGONAL_COST);
                }
            }
        }

        if (i != MAP_HEIGHT - 1) {
            Node downNode = nodes.get(MAP_WIDTH * (i + 1) + j);
            targetNode.createConnection(downNode, AXIAL_2D_COST);

            if (reconnect) {
                downNode.createConnection(targetNode, AXIAL_2D_COST);
            }

            if (j != 0) {
                Node leftDownNode = nodes.get(MAP_WIDTH * (i + 1) + (j - 1));
                targetNode.createConnection(leftDownNode, DIAGONAL_COST);

                if (reconnect) {
                    leftDownNode.createConnection(targetNode, DIAGONAL_COST);
                }
            }

            if (j != MAP_WIDTH - 1) {
                Node rightDownNode = nodes.get(MAP_WIDTH * (i + 1) + (j + 1));
                targetNode.createConnection(rightDownNode, DIAGONAL_COST);

                if (reconnect) {
                    rightDownNode.createConnection(targetNode, DIAGONAL_COST);
                }
            }
        }

        if (j != 0) {
            Node leftNode = nodes.get(MAP_WIDTH * i + j - 1);
            targetNode.createConnection(leftNode, AXIAL_2D_COST);

            if (reconnect) {
                leftNode.createConnection(targetNode, AXIAL_2D_COST);
            }
        }

        if (j != MAP_WIDTH - 1) {
            Node rightNode = nodes.get(MAP_WIDTH * i + j + 1);
            targetNode.createConnection(rightNode, AXIAL_2D_COST);

            if (reconnect) {
                rightNode.createConnection(targetNode, AXIAL_2D_COST);
            }
        }
    }

    public void draw(SpriteBatch batch, boolean runningSimulation) {
        drawLevelBackground(batch);

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Node targetNode = nodes.get(MAP_WIDTH * i + j);

                if (!runningSimulation) {
                    //drawCollisionLayer(batch, targetNode, i, j);
                }

                if (drawPaths) {
                    drawPathLayer(batch, targetNode, i, j);
                }

                if (drawObjects) {
                    drawObjectLayer(batch, targetNode, i, j);
                }

                if (!runningSimulation) {
                    drawTowerLayer(batch, targetNode, i, j);
                }
            }
        }
    }

    private void drawLevelBackground(SpriteBatch batch) {
        if (this.currentLevelBackground != null) {
            batch.draw(this.currentLevelBackground
                    , Labyrinth.MAP_OFFSET_X
                    , Labyrinth.MAP_HEIGHT_PIXELS + Labyrinth.MAP_OFFSET_Y
                    , Labyrinth.MAP_WIDTH_PIXELS
                    , Labyrinth.MAP_HEIGHT_PIXELS * -1);
        }
    }

    public void drawCollisionLayer(SpriteBatch batch, Node targetNode, int i, int j) {
        if (targetNode.getType() == this.WALL) {
            batch.draw(assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.WALL)
                    , j * CELL_WIDTH + MAP_OFFSET_X
                    , i * CELL_HEIGHT + MAP_OFFSET_Y
                    , CELL_WIDTH
                    , CELL_HEIGHT);
        } else if (targetNode.getType() == this.ROAD) {
            batch.draw(assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.ROAD)
                        , j * CELL_WIDTH + MAP_OFFSET_X
                        , i * CELL_HEIGHT + MAP_OFFSET_Y
                        , CELL_WIDTH
                        , CELL_HEIGHT);
        }
    }

    public void drawPathLayer(SpriteBatch batch, Node targetNode, int i, int j) {
        if (targetNode.start) {
            batch.draw(assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.START)
                    , j * CELL_WIDTH + (CELL_WIDTH / 4) + MAP_OFFSET_X
                    , i * CELL_HEIGHT + (CELL_HEIGHT / 4) + MAP_OFFSET_Y
                    , CELL_WIDTH / 2
                    , CELL_HEIGHT / 2);
        } else if (targetNode.end) {
            batch.draw(assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.END)
                    , j * CELL_WIDTH + (CELL_WIDTH / 4) + MAP_OFFSET_X
                    , i * CELL_HEIGHT + (CELL_HEIGHT / 4) + MAP_OFFSET_Y
                    , CELL_WIDTH / 2
                    , CELL_HEIGHT / 2);
        } else if (targetNode.path) {
            batch.draw(assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.PATH)
                    , j * CELL_WIDTH + (CELL_WIDTH / 4) + MAP_OFFSET_X
                    , i * CELL_HEIGHT + (CELL_HEIGHT / 4) + MAP_OFFSET_Y
                    , CELL_WIDTH / 2
                    , CELL_HEIGHT / 2);
        }
    }

    public void drawBuildableLocations(Matrix4 matrix) {
        shapeRenderer.setProjectionMatrix(matrix);

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Node targetNode = nodes.get(MAP_WIDTH * i + j);

                if (targetNode.buildableLocation) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                    shapeRenderer.setColor(0.9f, 0.0f, 0.0f, 0.9f);
                    shapeRenderer.rect(targetNode.x + 2, targetNode.y + 2, CELL_WIDTH - 2, CELL_HEIGHT - 2);
                    shapeRenderer.end();
                }
            }
        }
    }

    public void drawObjectLayer(SpriteBatch batch, Node targetNode, int i, int j) {
        if (targetNode.object == null) {
            return;
        }

        drawObject(batch, assetLoader.getObstacleTexture(targetNode.object), i, j);
    }

    public void drawTowerLayer(SpriteBatch batch, Node targetNode, int i, int j) {
        if (targetNode.tower == null) {
            return;
        }

        drawTower(batch, targetNode.towerRef, i, j);
    }

    public void drawTowerRadius(Matrix4 projectionMatrix) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Node targetNode = nodes.get(MAP_WIDTH * i + j);

                if (targetNode.towerRef != null) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                    shapeRenderer.setColor(0.2f, 0.5f, 0.2f, 0.3f);
                    shapeRenderer.circle(j * CELL_WIDTH + MAP_OFFSET_X + (CELL_WIDTH / 2)
                            , i * CELL_HEIGHT + MAP_OFFSET_Y + (CELL_HEIGHT / 2)
                            , targetNode.towerRef.getRadius() * CELL_WIDTH);
                    shapeRenderer.end();
                }
            }
        }
    }

    public void drawObject(SpriteBatch batch, Texture obstacleTexture, int i, int j) {
        batch.draw(obstacleTexture
                , j * CELL_WIDTH + MAP_OFFSET_X
                , i * CELL_HEIGHT + MAP_OFFSET_Y + CELL_HEIGHT
                , CELL_WIDTH
                , flip(CELL_HEIGHT));
    }

    public void drawTower(SpriteBatch batch, Tower t, int i, int j) {
        if (t.isHumanoidTower()) {
            batch.draw(t.getHumanoidTowerTexture()
                    , j * CELL_WIDTH + MAP_OFFSET_X
                    , i * CELL_HEIGHT + MAP_OFFSET_Y + CELL_HEIGHT
                    , CELL_WIDTH
                    , flip(CELL_HEIGHT));
        } else {
            batch.draw(t.getTexture()
                    , j * CELL_WIDTH + MAP_OFFSET_X
                    , i * CELL_HEIGHT + MAP_OFFSET_Y + CELL_HEIGHT
                    , CELL_WIDTH
                    , flip(CELL_HEIGHT));
        }
    }

    public int flip(int toFlip) {
        return toFlip * -1;
    }

    public void drawOutlines(Matrix4 projectionMatrix) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);

        drawGrid(shapeRenderer);
        drawBox(shapeRenderer, MAP_OFFSET_X, MAP_OFFSET_Y, MAP_OFFSET_X + (CELL_WIDTH * MAP_WIDTH), MAP_OFFSET_Y + (CELL_HEIGHT * MAP_HEIGHT), 3);
        drawConnections(shapeRenderer);
    }

    private void drawGrid(ShapeRenderer shapeRenderer) {
        if (!drawGrid) {
            return;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 1, 0.5f);

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                shapeRenderer.rect(j * CELL_WIDTH + MAP_OFFSET_X, i * CELL_HEIGHT + MAP_OFFSET_Y, CELL_WIDTH, CELL_HEIGHT);
            }
        }
        shapeRenderer.end();
    }

    public void drawConnections(ShapeRenderer shapeRenderer) {
        if (!drawConnections) {
            return;
        }

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 1, 0, 1);

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                Node targetNode = nodes.get(MAP_WIDTH * i + j);

                Array<Connection<Node>> connections = targetNode.getConnections();

                for (Connection<Node> connection : connections) {
                    Node fromNode = connection.getFromNode();
                    Node toNode = connection.getToNode();

                    shapeRenderer.line(fromNode.x + (CELL_WIDTH / 2), fromNode.y + (CELL_HEIGHT / 2), toNode.x + (CELL_WIDTH / 2), toNode.y + (CELL_HEIGHT / 2));
                }
            }
        }

        shapeRenderer.end();
    }

    public void drawBox(ShapeRenderer renderer, int x1, int y1, int x2, int y2, int lineWidth) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0, 0.5f, 1);

        renderer.rectLine(x1, y1, x2, y1, lineWidth);
        renderer.rectLine(x2, y1, x2, y2, lineWidth);
        renderer.rectLine(x2, y2, x1, y2, lineWidth);
        renderer.rectLine(x1, y2, x1, y1, lineWidth);

        shapeRenderer.end();
    }

    public void clearPaths() {
        for (Node node : this.nodes) {
            if (node.start) {
                node.start = false;
            }

            if (node.end) {
                node.end = false;
            }

            if (node.path) {
                node.path = false;
            }

            this.graphImpl = new GraphImpl(this.nodes);
        }
    }

    public void toBuildableLocation(int x, int y) {
        Node node = nodeAtPosition(x, y);

        if (node != null) {
            node.buildableLocation = true;
        }
    }

    public void toNotBuildableLocation(int x, int y) {
        Node node = nodeAtPosition(x, y);

        if (node != null) {
            node.buildableLocation = false;
        }
    }

    public void toRoad(int x, int y) {
        Node node = nodeAtPosition(x, y);

        if (node != null) {
            node.setType(this.ROAD);

            int row = -1;
            int col = -1;

            for (int i = 0; i < MAP_HEIGHT; i++) {
                for (int j = 0; j < MAP_WIDTH; j++) {
                    if (x <= j * CELL_WIDTH + CELL_WIDTH + MAP_OFFSET_X
                            && x > j * CELL_WIDTH + MAP_OFFSET_X
                            && y <= i * CELL_HEIGHT + CELL_HEIGHT + MAP_OFFSET_Y
                            && y > i * CELL_HEIGHT + MAP_OFFSET_Y) {
                        row = j;
                        col = i;
                    }
                }
            }

            if (row != -1 && col != -1) {
                createAdjacentConnections(node, col, row, true);
            }
        }
    }

    public void toObstacle(int x, int y, AssetLoader.OBJECT_TEXTURE selectedObject) {
        Node node = nodeAtPosition(x, y);
        node.object = selectedObject;
    }

    public void toTower(int x, int y, AssetLoader.TOWER_TEXTURE selectedTower) {
        Node node = nodeAtPosition(x, y);
        node.tower = selectedTower;

        TowerStatistics.Statistic statistics = TowerStatistics.getStatisticsObject(selectedTower);
        node.towerRef = new Tower(statistics.damage, statistics.radius, statistics.cooldown, (int)node.x, (int)node.y, selectedTower);
    }

    public void toWall(int x, int y) {
        Node node = nodeAtPosition(x, y);

        if (node != null) {
            node.setType(this.WALL);

            Array<Connection<Node>> connections = this.graphImpl.getConnections(node);

            Array<Connection<Node>> connectionsToRemove = new Array<Connection<Node>>();

            for (Connection<Node> conn : connections) {
                connectionsToRemove.add(conn);

                Array<Connection<Node>> toNodeConnections = this.graphImpl.getConnections(conn.getToNode());

                for (Connection<Node> cn : toNodeConnections) {
                    if (cn.getToNode() == node) {
                        toNodeConnections.removeValue(cn, true);
                    }
                }
            }

            for (Connection<Node> conn : connectionsToRemove) {
                connections.removeValue(conn, true);
            }
        }
    }

    public void beginPath(int x, int y) {
        Node node = nodeAtPosition(x, y);

        if (node != null) {
            node.start = true;
            this.startNode = node;
        }
    }

    public void findPath(int x, int y) {
        Node node = nodeAtPosition(x, y);

        if (node != null) {
            node.end = true;
            this.endNode = node;

            this.pathHandler = new PathHandler(this.graphImpl);
            GraphPathImpl path = this.pathHandler.searchPath(this.startNode, this.endNode);

            if (path.getCount() > 0) {
                node.setPathStartNodeIndex(this.startNode.getIndex());
            }
        }
    }

    public Node nodeAtPosition(int x, int y) {
        int row = -1;
        int col = -1;

        for (int i = 0; i < MAP_HEIGHT; i++) {
            for (int j = 0; j < MAP_WIDTH; j++) {
                if (x <= j * CELL_WIDTH + CELL_WIDTH + MAP_OFFSET_X
                        && x > j * CELL_WIDTH + MAP_OFFSET_X
                        && y <= i * CELL_HEIGHT + CELL_HEIGHT + MAP_OFFSET_Y
                        && y > i * CELL_HEIGHT + MAP_OFFSET_Y) {
                    row = j;
                    col = i;
                }
            }
        }

        if (row != -1 && col != -1) {
            return nodes.get(MAP_WIDTH * col + row);
        } else {
            return null;
        }
    }

    public GraphImpl getGraphImpl() {
        return this.graphImpl;
    }

    public Array<Node> getNodes() {
        return this.nodes;
    }

    @Override
    public void write(Json json) {
        json.writeValue("currentLevelBackgroundUrl", this.currentLevelUrl);
        json.writeValue("nodes", this.nodes);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.currentLevelUrl = jsonData.getString("currentLevelBackgroundUrl");
        this.currentLevelBackground = assetLoader.getCurrentLevelBackground(this.currentLevelUrl);
        JsonValue nodeArray = jsonData.get("nodes");

        Array<Node> serializedNodes = new Array<Node>();

        for (JsonValue node : nodeArray.iterator()) {
            System.out.println(node);
            serializedNodes.add(json.readValue(Node.class, node));
        }

        init(serializedNodes);
    }
}
