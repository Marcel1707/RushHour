package at.fhooe.ai.rushhour;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final Puzzle puzzle;

    private final Heuristic heuristic;
    private final Map<State, Integer> heuristics = new HashMap<>();
    private final Map<State, Node> optimalNodeForState = new HashMap<>();
    private final Map<Node, Integer> openListInsertions = new HashMap<>();
    private final Set<State> closedList = new HashSet<>();
    private final PriorityQueue<Node> openList;


    /**
     * This is the constructor that performs A* search to compute a
     * solution for the given puzzle using the given heuristic.
     */
    public AStar(Puzzle puzzle, Heuristic heuristic) {
        this.puzzle = puzzle;
        this.heuristic = heuristic;

        Comparator<Node> comparator = (o1, o2) -> {
            int cmp = Integer.compare(o1.getDepth() + heuristics.get(o1.getState()),
                    o2.getDepth() + heuristics.get(o2.getState()));

            /* if the depth is equal, select the most recently added node (LIFO) */
            if (cmp == 0)
                return Integer.compare(openListInsertions.get(o2), openListInsertions.get(o1));

            return cmp;
        };

        this.openList = new PriorityQueue<>(comparator);

        Node targetNode = search();

        if (targetNode == null)
            return;

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


    private Node search() {
        heuristics.clear();
        openListInsertions.clear();
        openList.clear();
        closedList.clear();
        optimalNodeForState.clear();

        AtomicInteger insertionIdx = new AtomicInteger(0);

        addToOpenList(puzzle.getInitNode(), insertionIdx);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (currentNode.getState().isGoal()) {
                return currentNode;
            }

            if (!closedList.contains(currentNode.getState())) {
                for (Node child : currentNode.expand()) {
                    addToOpenList(child, insertionIdx);
                }
            }else{
                closedList.add(currentNode.getState());
            }
        }

        return null;
    }

    private void addToOpenList(Node node, AtomicInteger insertionIdx) {
        // cache heuristic for node state
        if (!heuristics.containsKey(node.getState()))
            heuristics.put(node.getState(), heuristic.getValue(node.getState()));

        Node existingNode = optimalNodeForState.get(node.getState());

        if (existingNode == null || node.getDepth() < existingNode.getDepth()) {
            optimalNodeForState.put(node.getState(), node);
            openListInsertions.put(node, insertionIdx.getAndIncrement());
            openList.add(node);

            // if the new node has lower costs than the existing one, replace it
            if (existingNode != null && node.getDepth() < existingNode.getDepth()) {
                openList.remove(existingNode);
                openListInsertions.remove(existingNode);
            }
        }
    }


}
