package at.fhooe.ai.rushhour;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HeuristicUtils {

    public static Set<Integer> findBlockingVehicles(State state, Puzzle puzzle, int ownVehicleId, boolean considerJustFront) {
        /*
        considerFront == true: if vehicle is horizontal: check all cars to the right, else check all cars to the left
        considerFront == true: if vehicle is vertical: check all cars to the bottom, else check all cars to the top
         */
        Set<Integer> blockingVehicles = new HashSet<>();

        int startPosition = considerJustFront ? state.getVariablePosition(ownVehicleId) + puzzle.getCarSize(ownVehicleId) : 0;
        int endPosition = puzzle.getGridSize();

        boolean ownOrientation = puzzle.getCarOrient(ownVehicleId);
        int ownFixedPosition = puzzle.getFixedPosition(ownVehicleId);

        // find number of blocking cars
        for (int position = startPosition; position < endPosition; position++) {
            for (int carIdx = 1; carIdx < puzzle.getNumCars(); carIdx++) {
                if ((ownVehicleId != carIdx) && (puzzle.getCarOrient(carIdx) != ownOrientation) && (puzzle.getFixedPosition(carIdx) == position)) {
                    int carPos = state.getVariablePosition(carIdx);
                    int carLen = puzzle.getCarSize(carIdx);

                    // check if the other car is blocking
                    if (carPos <= ownFixedPosition && carPos + carLen - 1 >= ownFixedPosition) {
                        blockingVehicles.add(carIdx);
                        break;
                    }
                }
            }
        }
        return blockingVehicles;
    }
}
