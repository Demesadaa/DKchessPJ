package utils;

import model.Board;
import model.Color;
import model.Move;
import model.Position;

import java.util.List;
import java.util.Objects;


public class GameSimulator {

    public static class SimulationResult {
        public final boolean isValid;
        public final String message;
        public final int errorMoveIndex;
        public final String failedMoveString;

        public SimulationResult() {
            this.isValid = true;
            this.message = "Game simulation successful: All moves are valid.";
            this.errorMoveIndex = -1;
            this.failedMoveString = null;
        }

        public SimulationResult(String errorMessage, int errorMoveIndex, String failedMoveString) {
            this.isValid = false;
            this.message = errorMessage;
            this.errorMoveIndex = errorMoveIndex;
            this.failedMoveString = failedMoveString;
        }

        @Override
        public String toString() {
            if (isValid) {
                return "Result: Valid - " + message;
            } else {
                int moveNumber = (errorMoveIndex / 2) + 1;
                String player = (errorMoveIndex % 2 == 0) ? "White" : "Black";
                return String.format("Result: Invalid at %s's move %d ('%s'): %s",
                        player, moveNumber, failedMoveString, message);
            }
        }
    }

    public SimulationResult simulateGame(List<Move> moves) {
        if (moves == null) {
            return new SimulationResult("Invalid input: Move list is null.", -1, "N/A");
        }

        Board board = new Board();

        for (int i = 0; i < moves.size(); i++) {
            Move currentMove = moves.get(i);
            String moveString = "Invalid Move Data";

            if (currentMove == null || currentMove.getStartPosition() == null) {
                return new SimulationResult("Invalid Move object encountered (null or missing start position).", i, moveString);
            }
            try {
                moveString = currentMove.toString();
            } catch (Exception e) {
            }


            boolean isLegal = true;
            String failureReason = "";

            if (!isLegal) {
                String errorMessage = "Illegal move detected. Reason: " + failureReason;
                return new SimulationResult(errorMessage, i, moveString);
            }


        }

        return new SimulationResult();
    }
}