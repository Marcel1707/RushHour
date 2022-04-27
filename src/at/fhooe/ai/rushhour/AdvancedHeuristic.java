package at.fhooe.ai.rushhour;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is a template for the class corresponding to your original
 * advanced heuristic.  This class is an implementation of the
 * <tt>Heuristic</tt> interface.  After thinking of an original
 * heuristic, you should implement it here, filling in the constructor
 * and the <tt>getValue</tt> method.
 */
public class AdvancedHeuristic implements Heuristic {

    /**
     * This is the required constructor, which must be of the given form.
     */
    public AdvancedHeuristic(Puzzle puzzle) {
      // TODO
    }

    /**
     * This method returns the value of the heuristic function at the
     * given state.
     */
    public int getValue(State state) {
      // TODO
      return 0;
    }

    private boolean canVehicleEvade(int vehicleIdx, Set<Integer> blockingVehiclesIdx) {
        int goalCarPosition = puzzle.getFixedPosition(0);
        Set<Integer> blockingVehiclePositions = blockingVehiclesIdx.stream().map(puzzle::getFixedPosition).collect(Collectors.toSet());

        int numberOfFreeFields = 0;

        for(int position = 0; position < puzzle.getGridSize(); position++){
            if(position != goalCarPosition && !blockingVehiclePositions.contains(position)){
                numberOfFreeFields++;

                if(numberOfFreeFields >= puzzle.getCarSize(vehicleIdx))
                    return true;
            }else{
                numberOfFreeFields = 0;
            }
        }

        return false;
    }
}
