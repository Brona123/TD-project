package fi.joutsijoki;

import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;

import fi.joutsijoki.pathfinding.GraphImpl;
import fi.joutsijoki.pathfinding.GraphPathImpl;
import fi.joutsijoki.pathfinding.HeuristicImpl;
import fi.joutsijoki.pathfinding.Node;

/**
 * Created by Sami on 30.12.2015.
 */
public class PathHandler {
    private IndexedAStarPathFinder<Node> pathFinder;
    private GraphPathImpl resultPath;

    public PathHandler(GraphImpl graphImpl) {
        pathFinder = new IndexedAStarPathFinder<Node>(graphImpl, false);
    }

    public GraphPathImpl searchPath(Node startNode, Node endNode) {
        resultPath = new GraphPathImpl();
        pathFinder.searchNodePath(startNode, endNode, new HeuristicImpl(), resultPath);

        return resultPath;
    }
}
