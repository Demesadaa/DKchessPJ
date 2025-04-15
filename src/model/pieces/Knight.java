package model.pieces;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class Knight implements Piece {
    private final Color color;

    public Knight(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public char getSymbol() {
        return 'N';
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();


        int[][] offsets = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
        };

        for (int[] offset : offsets) {
            int nextRow = currentRow + offset[0];
            int nextCol = currentCol + offset[1];

            if (Board.isValid(nextRow, nextCol)) {
                Position targetPos = new Position(nextRow, nextCol);
                Piece targetPiece = board.getPiece(targetPos);


                if (targetPiece == null) {
                    moves.add(new Move(currentPosition, targetPos, this, null));
                } else if (targetPiece.getColor() != this.color) {
                    moves.add(new Move(currentPosition, targetPos, this, targetPiece));
                }
            }
        }
        return moves;
    }

    @Override
    public boolean isValidMove(Board board, Position start, Position end) {

        int rowDiff = Math.abs(start.getRow() - end.getRow());
        int colDiff = Math.abs(start.getCol() - end.getCol());
        boolean isLShape = (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);

        if (!isLShape) {
            return false;
        }
        Piece targetPiece = board.getPiece(end);
        return targetPiece == null || targetPiece.getColor() != this.color;


    }
}