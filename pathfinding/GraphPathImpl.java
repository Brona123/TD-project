package fi.joutsijoki.pathfinding;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by Sami on 31.12.2015.
 */
public class GraphPathImpl implements GraphPath<Node> {
    private Array<Node> nodes = new Array<Node>();

    public GraphPathImpl() {

    }

    @Override
    public int getCount() {
        return nodes.size;
    }

    @Override
    public Node get(int index) {
        return nodes.get(index);
    }

    @Override
    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    public Array<Node> getNodes() {
        return this.nodes;
    }

}
