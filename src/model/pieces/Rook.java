package model.pieces;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class Rook implements Piece {
    private final Color color;

    public Rook(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public char getSymbol() {
        return 'R';
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}}; // Right, Left, Up, Down
        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];
            for (int i = 1; ; i++) {
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
        return getPseudoLegalMoves(board, start).stream().anyMatch(move -> move.getEndPosition().equals(end));
    }
}