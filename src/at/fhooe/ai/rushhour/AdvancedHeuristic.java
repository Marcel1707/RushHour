package at.fhooe.ai.rushhour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a template for the class corresponding to your original
 * advanced heuristic.  This class is an implementation of the
 * <tt>Heuristic</tt> interface.  After thinking of an original
 * heuristic, you should implement it here, filling in the constructor
 * and the <tt>getValue</tt> method.ﬂ
 */
public class AdvancedHeuristic implements Heuristic {

    private final Puzzle puzzle;

    /**
     * This is the required constructor, which must be of the given form.
     */
    public AdvancedHeuristic(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    /**
     * This method returns the value of the heuristic function at the
     * given state.
     */
    public int getValue(State state) {
        if (state.isGoal())
            return 0;

        /* find vehicles that are blocking the goal car in the front */
        Set<Integer> mainBlockingVehicles = HeuristicUtils.findBlockingVehicles(state, puzzle, 0, true);

        int minExpenseForBlockingVehiclesToEvadeSum = 0;
        Set<Integer> alreadyConsideredBlockingVehicles = new HashSet<>();

        for (int blockingVehicleIdx : mainBlockingVehicles) {
            /* find vehicles that are also blocking our blocking vehicles */
            Set<Integer> blockingVehiclesOfBlockingVehicles = HeuristicUtils.findBlockingVehicles(state, puzzle, blockingVehicleIdx, false);

            /* make sure, that when a blocking vehicles blocks multiple vehicles, this vehicle is just considered one time */
            blockingVehiclesOfBlockingVehicles.removeAll(alreadyConsideredBlockingVehicles);

            /* find the minimum expense for the blocking vehicle to evade to the front or to the back */
            int minExpenseForBlockingVehicleToEvade = Math.min(
                    expenseToEvade(blockingVehicleIdx, new ArrayList<>(blockingVehiclesOfBlockingVehicles), alreadyConsideredBlockingVehicles, true), // consider front
                    expenseToEvade(blockingVehicleIdx, new ArrayList<>(blockingVehiclesOfBlockingVehicles), alreadyConsideredBlockingVehicles, false) // consider back
            );

            minExpenseForBlockingVehiclesToEvadeSum += minExpenseForBlockingVehicleToEvade;

        }

        return mainBlockingVehicles.size() + 1 + minExpenseForBlockingVehiclesToEvadeSum;
    }

    private int expenseToEvade(int vehicleIdx, List<Integer> blockingVehiclesIdx, Set<Integer> alreadyConsideredVehicles, boolean front) {
        int goalCarPosition = puzzle.getFixedPosition(0);
        List<Integer> blockingVehiclePositions = blockingVehiclesIdx.stream().map(puzzle::getFixedPosition).collect(Collectors.toList());

        int numberOfFreeFields = 0;
        int costsToEvade = 0;

        // set start position and iterator depending on front or back of vehicle should be considered
        int startPosition = goalCarPosition + (front ? 1 : -1);
        int iterator = front ? 1 : -1;

        for (int position = startPosition; !isStoppingCriteriaFulfilled(position, front); position += iterator) {
            // if a blocking vehicle is on the current position
            if (blockingVehiclePositions.contains(position)) {
                costsToEvade++;
                // make sure that this already considered blocking vehicle is not considered again
                alreadyConsideredVehicles.add(blockingVehiclesIdx.get(blockingVehiclePositions.indexOf(position)));
            }

            numberOfFreeFields++;

            if (numberOfFreeFields >= puzzle.getCarSize(vehicleIdx))
                return costsToEvade;

        }

        // blocking vehicle can't evade to selected side, because of its length
        return Integer.MAX_VALUE;
    }

    private boolean isStoppingCriteriaFulfilled(int position, boolean front) {
        return front ? position == puzzle.getGridSize() : position == -1;
    }

}
