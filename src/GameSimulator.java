

import model.Board;
import model.Color;
import model.Move;
import model.pieces.Piece;
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
                return String.format("Result: Invalid at %s's move %d ('%s') - Reason: %s",
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
            String moveNotation = "Invalid Move Data";

            if (currentMove == null || currentMove.getStartPosition() == null || currentMove.getEndPosition() == null) {
                return new SimulationResult("Corrupted Move object encountered (null or missing positions).", i, moveNotation);
            }

            try {
                moveNotation = currentMove.toString();
            } catch (Exception e) {
                System.err.println("Warning: Error creating string for move index " + i + ": " + e.getMessage());
            }

            Color activePlayer = board.getCurrentPlayer();
            Position originSquare = currentMove.getStartPosition();
            Piece movingPiece = board.getPiece(originSquare);

            if (movingPiece == null) {
                return new SimulationResult("Illegal move: No piece located at start square " + originSquare, i, moveNotation);
            }

            if (movingPiece.getColor() != activePlayer) {
                return new SimulationResult(String.format("Illegal move: Piece at %s (%s) belongs to %s, but it's %s's turn",
                        originSquare, movingPiece.getSymbol(), movingPiece.getColor(), activePlayer),
                        i, moveNotation);
            }

            List<Move> possibleMoves;
            try {
                possibleMoves = board.generateLegalMoves();
            } catch (Exception e) {
                System.err.println("Exception generating legal moves at index " + i + ": " + e.getMessage());
                e.printStackTrace();
                return new SimulationResult("Internal error during legal move generation: " + e.getMessage(), i, moveNotation);
            }

            boolean moveIsAllowed = false;
            for (Move legalOption : possibleMoves) {
                if (Objects.equals(legalOption.getStartPosition(), currentMove.getStartPosition()) &&
                        Objects.equals(legalOption.getEndPosition(), currentMove.getEndPosition()) &&
                        legalOption.getPromotionChar() == currentMove.getPromotionChar()) {
                    moveIsAllowed = true;
                    break;
                }
            }

            if (!moveIsAllowed) {
                return new SimulationResult("Illegal move: The specified move is not permitted in the current position based on generated legal moves.", i, moveNotation);
            }

            try {
                board.makeMove(currentMove);
            } catch (Exception e) {
                System.err.println("Exception executing move " + moveNotation + " at index " + i + ": " + e.getMessage());
                e.printStackTrace();
                return new SimulationResult("Internal error while applying validated move: " + e.getMessage(), i, moveNotation);
            }
        }

        return new SimulationResult();
    }
}