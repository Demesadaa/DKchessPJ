package model.pieces;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class Queen implements Piece {
    private final Color color;

    public Queen(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public char getSymbol() {
        return 'Q';
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = {
                {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
                // sort of concatination of rook and bishop
        };

        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];
            for (int i = 1; ; i++) { // Keep moving in the direction until blocked or off-board
                int nextRow = currentPosition.getRow() + i * dr;
                int nextCol = currentPosition.getCol() + i * dc;
                if (!Board.isValid(nextRow, nextCol)) {
                    break;
                }
                Position nextPos = new Position(nextRow, nextCol);
                Piece targetPiece = board.getPiece(nextPos);

                if (targetPiece == null) {
                    moves.add(new Move(currentPosition, nextPos, this, null));
                } else {
                    if (targetPiece.getColor() != this.color) {

                        moves.add(new Move(currentPosition, nextPos, this, targetPiece));
                    }
                    break;
                }
            }
        }
        return moves;
    }

    @Override
    public boolean isValidMove(Board board, Position start, Position end) {
        int rowDiff = Math.abs(start.getRow() - end.getRow());
        int colDiff = Math.abs(start.getCol() - end.getCol());
        boolean isStraight = (rowDiff == 0 && colDiff > 0) || (rowDiff > 0 && colDiff == 0);
        boolean isDiagonal = rowDiff == colDiff && rowDiff > 0;

        if (!isStraight && !isDiagonal) {
            return false;
        }
        return getPseudoLegalMoves(board, start).stream().anyMatch(move -> move.getEndPosition().equals(end));
    }
}