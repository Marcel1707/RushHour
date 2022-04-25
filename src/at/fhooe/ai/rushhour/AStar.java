package at.fhooe.ai.rushhour;

import java.time.LocalDateTime;
import java.util.*;

/**
 * This is the template for a class that performs A* search on a given rush hour
 * puzzle with a given heuristic. The main search computation is carried out by
 * the constructor for this class, which must be filled in. The solution (a path
 * from the initial state to a goal state) is returned as an array of
 * <tt>State</tt>s called <tt>path</tt> (where the first element
 * <tt>path[0]</tt> is the initial state). If no solution is found, the
 * <tt>path</tt> field should be set to <tt>null</tt>. You may also wish to
 * return other information by adding additional fields to the class.
 */
public class AStar {

    /**
     * The solution path is stored here
     */
    public State[] path;

    /**
     * This is the constructor that performs A* search to compute a
     * solution for the given puzzle using the given heuristic.
     */
    public AStar(Puzzle puzzle, Heuristic heuristic) {
        Node targetNode = search(puzzle, heuristic);

        if (targetNode == null) return;

        List<State> convertedPath = convertPath(new ArrayList<>(), targetNode);

        this.path = convertedPath.toArray(new State[0]);
    }

    private List<State> convertPath(List<State> path, Node currentNode) {
        path.add(currentNode.getState());
        if (currentNode.getParent() != null) {
            return convertPath(path, currentNode.getParent());
        } else {
            Collections.reverse(path);
            return path;
        }
    }

    private Node search(Puzzle puzzle, Heuristic heuristic) {
        Map<Node, LocalDateTime> openListInsertions = new HashMap<>();

        PriorityQueue<Node> openList = new PriorityQueue<>((o1, o2) -> {
            int cmp = Integer.compare(o1.getDepth() + heuristic.getValue(o1.getState()),
                    o2.getDepth() + heuristic.getValue(o2.getState()));
            if (cmp == 0)
                return openListInsertions.get(o2).compareTo(openListInsertions.get(o1));

            return cmp;
        });

        Set<State> closedList = new HashSet<>();
        openListInsertions.put(puzzle.getInitNode(), LocalDateTime.now());
        openList.add(puzzle.getInitNode());

        Heuristic h = new BlockingHeuristic(puzzle);
        h.getValue(puzzle.getInitNode().getState());

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (currentNode.getState().isGoal()) {
                return currentNode;
            }

            if (!closedList.contains(currentNode.getState())) {
                for (Node child : currentNode.expand()) {
                    openListInsertions.put(child, LocalDateTime.now());
                    openList.add(child);
                }
            }

            closedList.add(currentNode.getState());
        }

        return null;
    }


}
