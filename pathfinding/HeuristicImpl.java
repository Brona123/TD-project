package fi.joutsijoki.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

import fi.joutsijoki.Constant;

/**
 * Created by Sami on 31.12.2015.
 */
public class HeuristicImpl implements Heuristic<Node> {

    @Override
    public float estimate(Node startNode, Node endNode) {
        int startIndex = startNode.getIndex();
        int endIndex = endNode.getIndex();

        int startY = startIndex / Constant.CELL_WIDTH;
        int startX = startIndex % Constant.CELL_WIDTH;

        int endY = endIndex / Constant.CELL_WIDTH;
        int endX = endIndex % Constant.CELL_WIDTH;

        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }
}
