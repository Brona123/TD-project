package fi.joutsijoki.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Created by Sami on 31.12.2015.
 */
public class ConnectionImpl implements Connection<Node> {
    private Node toNode;
    private Node fromNode;
    private float cost;

    public ConnectionImpl() {

    }

    public ConnectionImpl(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }

    public void removeToNode() {
        this.toNode = null;
    }
}
