package model.pieces;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class King implements Piece {
    private final Color color;

    public King(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public char getSymbol() {
        return 'K';
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();


        int[][] offsets = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},           {0, 1},
                {1, -1}, {1, 0}, {1, 1}
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


        int kingRow = (color == Color.WHITE) ? 0 : 7;
        if (currentPosition.getRow() == kingRow && currentPosition.getCol() == 4) { // King must be on original square
            if (canPotentiallyCastle(board, true)) {
                moves.add(new Move(currentPosition, new Position(kingRow, 6), this, true));
            }
            if (canPotentiallyCastle(board, false)) {
                moves.add(new Move(currentPosition, new Position(kingRow, 2), this, true));
            }
        }

        return moves;
    }

    private boolean canPotentiallyCastle(Board board, boolean kingSide) {
        int kingRow = (color == Color.WHITE) ? 0 : 7;
        int rookCol = kingSide ? 7 : 0;
        Position rookPos = new Position(kingRow, rookCol);
        Piece potentialRook = board.getPiece(rookPos);

        if (!(potentialRook instanceof Rook) || potentialRook.getColor() != this.color) {
            return false;
        }

        if (this.color == Color.WHITE) {
            if (kingSide && !board.canWhiteKingSideCastle()) return false;
            if (!kingSide && !board.canWhiteQueenSideCastle()) return false;
        } else {
            if (kingSide && !board.canBlackKingSideCastle()) return false;
            if (!kingSide && !board.canBlackQueenSideCastle()) return false;
        }

        int startCol = kingSide ? 5 : 1;
        int endCol = kingSide ? 6 : 3;
        for (int c = startCol; c <= endCol; c++) {
            if (board.getPiece(new Position(kingRow, c)) != null) {
                return false;
            }
        }

        return kingSide;
    }


    @Override
    public boolean isValidMove(Board board, Position start, Position end) {

        int rowDiff = Math.abs(start.getRow() - end.getRow());
        int colDiff = Math.abs(start.getCol() - end.getCol());
        boolean isStandardMove = rowDiff <= 1 && colDiff <= 1 && (rowDiff + colDiff > 0);
        boolean isCastleMove = (start.getRow() == end.getRow() && (end.getCol() == 6 || end.getCol() == 2) && colDiff == 2);

        if (!isStandardMove && !isCastleMove) {
            return false;
        }
        if (isStandardMove) {
            Piece targetPiece = board.getPiece(end);
            if (targetPiece != null && targetPiece.getColor() == this.color) {
                return false;
            }
        }
        return getPseudoLegalMoves(board, start).stream().anyMatch(move -> move.getEndPosition().equals(end));
    }
}