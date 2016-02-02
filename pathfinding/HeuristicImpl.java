package fi.joutsijoki.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

import fi.joutsijoki.Labyrinth;

/**
 * Created by Sami on 31.12.2015.
 */
public class HeuristicImpl implements Heuristic<Node> {

    @Override
    public float estimate(Node startNode, Node endNode) {
        int startIndex = startNode.getIndex();
        int endIndex = endNode.getIndex();

        int startY = startIndex / Labyrinth.CELL_WIDTH;
        int startX = startIndex % Labyrinth.CELL_WIDTH;

        int endY = endIndex / Labyrinth.CELL_WIDTH;
        int endX = endIndex % Labyrinth.CELL_WIDTH;

        float distance = Math.abs(startX - endX) + Math.abs(startY - endY);

        return distance;
    }
}
