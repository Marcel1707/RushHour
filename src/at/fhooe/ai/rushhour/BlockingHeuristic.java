package at.fhooe.ai.rushhour;

/**
 * This is a template for the class corresponding to the blocking heuristic.
 * This heuristic returns zero for goal states, and otherwise returns one plus
 * the number of cars blocking the path of the goal car to the exit. This class
 * is an implementation of the <tt>Heuristic</tt> interface, and must be
 * implemented by filling in the constructor and the <tt>getValue</tt> method.
 */
public class BlockingHeuristic implements Heuristic {
    private final Puzzle puzzle;

    /**
     * This is the required constructor, which must be of the given form.
     */
    public BlockingHeuristic(Puzzle puzzle) {
        this.puzzle = puzzle;  // Get size of our car
    }

    /**
     * This method returns the value of the heuristic function at the given state.
     */
    public int getValue(State state) {
        if (state.isGoal()) {
            return 0;
        }

        return HeuristicUtils.findBlockingVehicles(state, puzzle, 0, true).size() + 1;
    }

}
