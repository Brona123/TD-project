package fi.joutsijoki.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Sami on 31.12.2015.
 */
public class GraphImpl extends DefaultIndexedGraph<Node> {
    private Array<Node> nodes = new Array<Node>();

    public GraphImpl() {
        super();
    }

    public GraphImpl(int capacity) {
        super(capacity);
    }

    public GraphImpl(Array<Node> nodes) {
        super(nodes);
        this.nodes = nodes;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return super.getConnections(fromNode);
    }

    @Override
    public int getNodeCount() {
        return super.getNodeCount();
    }

    public Array<Node> getNodes() {
        return this.nodes;
    }
}
