package fi.joutsijoki;

import com.badlogic.gdx.Gdx;
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
import fi.joutsijoki.tower.HumanoidTower;
import fi.joutsijoki.tower.Tower;

/**
 * Created by Sami on 30.12.2015.
 */
public class GameField implements Json.Serializable {
    private Array<Node> nodes = new Array<Node>();
    private GraphImpl graphImpl;

    public static final int WALL = 0;
    public static final int ROAD = 1;

    private Node startNode;
    private Node endNode;
    private PathHandler pathHandler;

    private final float DIAGONAL_COST = 14;
    private final float AXIAL_2D_COST = 10;

    private ShapeRenderer shapeRenderer;

    public static AssetLoader assetLoader;

    public static boolean drawGrid;
    public static boolean drawConnections;
    public static boolean drawPaths;
    public static boolean drawObjects;

    private String currentLevelUrl = "level-2.png";
    private Texture currentLevelBackground;

    public static int money = 0;
    private Node selectedLocation;
    // TODO mobiili port, screenit

    public GameField() {
        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                Node node = new Node(WALL
                        , j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                        , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y);
                nodes.add(node);
            }
        }

        init(nodes);
    }

    public GameField(Array<Node> localNodes) {
        init(localNodes);
    }

    private void init(Array<Node> nodes) {
        Array<Node> tmpNodeArr = new Array<Node>();

        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                Node node = new Node(nodes.get(Constant.MAP_WIDTH * i + j)
                        , j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                        , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y);

                tmpNodeArr.add(node);
            }
        }

        this.nodes = tmpNodeArr;

        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                Node targetNode = this.nodes.get(Constant.MAP_WIDTH * i + j);

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
            Node upNode = nodes.get(Constant.MAP_WIDTH * (i - 1) + j);
            targetNode.createConnection(upNode, AXIAL_2D_COST);

            if (reconnect) {
                upNode.createConnection(targetNode, AXIAL_2D_COST);
            }

            if (j != 0) {
                Node leftUpNode = nodes.get(Constant.MAP_WIDTH * (i - 1) + (j - 1));
                targetNode.createConnection(leftUpNode, DIAGONAL_COST);

                if (reconnect) {
                    leftUpNode.createConnection(targetNode, DIAGONAL_COST);
                }
            }

            if (j != Constant.MAP_WIDTH - 1) {
                Node rightUpNode = nodes.get(Constant.MAP_WIDTH * (i - 1) + j + 1);
                targetNode.createConnection(rightUpNode, DIAGONAL_COST);

                if (reconnect) {
                    rightUpNode.createConnection(targetNode, DIAGONAL_COST);
                }
            }
        }

        if (i != Constant.MAP_HEIGHT - 1) {
            Node downNode = nodes.get(Constant.MAP_WIDTH * (i + 1) + j);
            targetNode.createConnection(downNode, AXIAL_2D_COST);

            if (reconnect) {
                downNode.createConnection(targetNode, AXIAL_2D_COST);
            }

            if (j != 0) {
                Node leftDownNode = nodes.get(Constant.MAP_WIDTH * (i + 1) + (j - 1));
                targetNode.createConnection(leftDownNode, DIAGONAL_COST);

                if (reconnect) {
                    leftDownNode.createConnection(targetNode, DIAGONAL_COST);
                }
            }

            if (j != Constant.MAP_WIDTH - 1) {
                Node rightDownNode = nodes.get(Constant.MAP_WIDTH * (i + 1) + (j + 1));
                targetNode.createConnection(rightDownNode, DIAGONAL_COST);

                if (reconnect) {
                    rightDownNode.createConnection(targetNode, DIAGONAL_COST);
                }
            }
        }

        if (j != 0) {
            Node leftNode = nodes.get(Constant.MAP_WIDTH * i + j - 1);
            targetNode.createConnection(leftNode, AXIAL_2D_COST);

            if (reconnect) {
                leftNode.createConnection(targetNode, AXIAL_2D_COST);
            }
        }

        if (j != Constant.MAP_WIDTH - 1) {
            Node rightNode = nodes.get(Constant.MAP_WIDTH * i + j + 1);
            targetNode.createConnection(rightNode, AXIAL_2D_COST);

            if (reconnect) {
                rightNode.createConnection(targetNode, AXIAL_2D_COST);
            }
        }
    }

    public void draw(SpriteBatch batch) {
        drawLevelBackground(batch);

        /*
        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                Node targetNode = nodes.get(Constant.MAP_WIDTH * i + j);

                if (drawPaths) {
                    drawPathLayer(batch, targetNode, i, j);
                }

                if (drawObjects) {
                    drawObjectLayer(batch, targetNode, i, j);
                }
            }
        }
        */
    }

    public void draw(SpriteBatch batch, boolean runningSimulation) {
        drawLevelBackground(batch);

        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                Node targetNode = nodes.get(Constant.MAP_WIDTH * i + j);

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

        if (this.selectedLocation != null) {
            batch.draw(AssetLoader.targetLocation
                    , this.selectedLocation.x
                    , this.selectedLocation.y
                    , Constant.CELL_WIDTH
                    , Constant.CELL_HEIGHT);
        }
    }

    private void drawLevelBackground(SpriteBatch batch) {
        if (this.currentLevelBackground != null) {
            batch.draw(this.currentLevelBackground
                    , Constant.MAP_OFFSET_X
                    , Constant.MAP_HEIGHT_PIXELS + Constant.MAP_OFFSET_Y
                    , Constant.MAP_WIDTH_PIXELS
                    , Constant.MAP_HEIGHT_PIXELS * -1);
        }
    }

    public void drawCollisionLayer(SpriteBatch batch, Node targetNode, int i, int j) {
        if (targetNode.getType() == this.WALL) {
            batch.draw(assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.WALL)
                    , j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                    , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y
                    , Constant.CELL_WIDTH
                    , Constant.CELL_HEIGHT);
        } else if (targetNode.getType() == this.ROAD) {
            batch.draw(assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.ROAD)
                        , j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                        , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y
                        , Constant.CELL_WIDTH
                        , Constant.CELL_HEIGHT);
        }
    }

    public void drawPathLayer(SpriteBatch batch, Node targetNode, int i, int j) {
        Texture t = null;

        if (targetNode.start) {
            t = assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.START);
        } else if (targetNode.end) {
            t = assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.END);
        } else if (targetNode.path) {
            t = assetLoader.getPathfindingTexture(AssetLoader.PATHFINDING_TEXTURE.PATH);
        }

        if (t != null) {
            batch.draw(t
                    , j * Constant.CELL_WIDTH + (Constant.CELL_WIDTH / 4) + Constant.MAP_OFFSET_X
                    , i * Constant.CELL_HEIGHT + (Constant.CELL_HEIGHT / 4) + Constant.MAP_OFFSET_Y
                    , Constant.CELL_WIDTH / 2
                    , Constant.CELL_HEIGHT / 2);
        }
    }

    public void drawBuildableLocations(Matrix4 matrix) {
        shapeRenderer.setProjectionMatrix(matrix);

        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                Node targetNode = nodes.get(Constant.MAP_WIDTH * i + j);

                if (targetNode.buildableLocation) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                    shapeRenderer.setColor(0.9f, 0.0f, 0.0f, 0.9f);
                    shapeRenderer.rect(targetNode.x + 2, targetNode.y + 2, Constant.CELL_WIDTH - 2, Constant.CELL_HEIGHT - 2);
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

        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                Node targetNode = nodes.get(Constant.MAP_WIDTH * i + j);

                if (targetNode.towerRef != null) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                    shapeRenderer.setColor(0.2f, 0.5f, 0.2f, 0.3f);
                    shapeRenderer.circle(j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X + (Constant.CELL_WIDTH / 2)
                            , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y + (Constant.CELL_HEIGHT / 2)
                            , targetNode.towerRef.getRadius() * Constant.CELL_WIDTH);
                    shapeRenderer.end();
                }
            }
        }
    }

    public void drawObject(SpriteBatch batch, Texture obstacleTexture, int i, int j) {
        batch.draw(obstacleTexture
                , j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y + Constant.CELL_HEIGHT
                , Constant.CELL_WIDTH
                , flip(Constant.CELL_HEIGHT));
    }

    public void drawTower(SpriteBatch batch, Tower t, int i, int j) {
        if (t.getClass() == HumanoidTower.class) {
            HumanoidTower ht = (HumanoidTower) t;
            batch.draw(ht.getHumanoidTexture()
                    , j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                    , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y + Constant.CELL_HEIGHT
                    , Constant.CELL_WIDTH
                    , flip(Constant.CELL_HEIGHT));
        } else {
            batch.draw(t.getTexture()
                    , j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                    , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y + Constant.CELL_HEIGHT
                    , Constant.CELL_WIDTH
                    , flip(Constant.CELL_HEIGHT));
        }
    }

    public int flip(int toFlip) {
        return toFlip * -1;
    }

    public void drawOutlines(Matrix4 projectionMatrix) {
        shapeRenderer.setProjectionMatrix(projectionMatrix);

        drawGrid(shapeRenderer);
        drawBox(shapeRenderer
                , Constant.MAP_OFFSET_X
                , Constant.MAP_OFFSET_Y
                , Constant.MAP_OFFSET_X + (Constant.MAP_WIDTH_PIXELS)
                , Constant.MAP_OFFSET_Y + (Constant.MAP_HEIGHT_PIXELS)
                , 3);
        drawConnections(shapeRenderer);
    }

    public void drawGrid(ShapeRenderer shapeRenderer) {
        /*
        if (!drawGrid) {
            return;
        }
        */

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 1, 0.5f);

        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                shapeRenderer.rect(j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                        , i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y
                        , Constant.CELL_WIDTH
                        , Constant.CELL_HEIGHT);
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

        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                Node targetNode = nodes.get(Constant.MAP_WIDTH * i + j);

                Array<Connection<Node>> connections = targetNode.getConnections();

                for (Connection<Node> connection : connections) {
                    Node fromNode = connection.getFromNode();
                    Node toNode = connection.getToNode();

                    shapeRenderer.line(fromNode.x + (Constant.CELL_WIDTH / 2)
                            , fromNode.y + (Constant.CELL_HEIGHT / 2)
                            , toNode.x + (Constant.CELL_WIDTH / 2)
                            , toNode.y + (Constant.CELL_HEIGHT / 2));
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

            for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
                for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                    if (x <= j * Constant.CELL_WIDTH + Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                            && x > j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                            && y <= i * Constant.CELL_HEIGHT + Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y
                            && y > i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y) {
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
        node.towerRef = Tower.createTower(statistics.damage, statistics.radius, statistics.cooldown, (int)node.x, (int)node.y, selectedTower);
    }

    public void toSelectedLocation(int x, int y) {
        Node n = nodeAtPosition(x, y);

        if (n != null) {
            if (n.buildableLocation) {
                this.selectedLocation = n;
            } else {
                System.out.println("INVALID LOCATION");
            }
        } else {
            System.out.println("NO NODE AT THIS POSITION");
        }
    }

    public Tower buildTower(int x, int y, AssetLoader.TOWER_TEXTURE selectedTower) {
        Node node = nodeAtPosition(x, y);

        TowerStatistics.Statistic statistics = TowerStatistics.getStatisticsObject(selectedTower);
        return Tower.createTower(statistics.damage, statistics.radius, statistics.cooldown, (int)node.x, (int)node.y, selectedTower);
    }

    public Tower buildTower(AssetLoader.TOWER_TEXTURE selectedTower, TowerStatistics.Statistic statistic) {
        if (this.selectedLocation != null) {
            return Tower.createTower(statistic.damage, statistic.radius, statistic.cooldown, (int)this.selectedLocation.x, (int)this.selectedLocation.y, selectedTower);
        } else {
            return null;
        }
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

        for (int i = 0; i < Constant.MAP_HEIGHT; i++) {
            for (int j = 0; j < Constant.MAP_WIDTH; j++) {
                if (x <= j * Constant.CELL_WIDTH + Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                        && x > j * Constant.CELL_WIDTH + Constant.MAP_OFFSET_X
                        && y <= i * Constant.CELL_HEIGHT + Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y
                        && y > i * Constant.CELL_HEIGHT + Constant.MAP_OFFSET_Y) {
                    row = j;
                    col = i;
                }
            }
        }

        if (row != -1 && col != -1) {
            return nodes.get(Constant.MAP_WIDTH * col + row);
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
        json.writeValue("width-cells", Constant.MAP_WIDTH);
        json.writeValue("height-cells", Constant.MAP_HEIGHT);
        json.writeValue("nodes", this.nodes);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.currentLevelUrl = jsonData.getString("currentLevelBackgroundUrl");
        this.currentLevelBackground = assetLoader.getCurrentLevelBackground(this.currentLevelUrl);
        Constant.MAP_WIDTH = jsonData.getInt("width-cells");
        Constant.MAP_HEIGHT = jsonData.getInt("height-cells");
        JsonValue nodeArray = jsonData.get("nodes");
        Constant.setDimensions(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 7, Gdx.graphics.getHeight());

        Array<Node> serializedNodes = new Array<Node>();

        for (JsonValue node : nodeArray.iterator()) {
            serializedNodes.add(json.readValue(Node.class, node));
        }

        init(serializedNodes);
    }
}
