package model.pieces;

import model.*; // Import necessary classes from model package

import java.util.ArrayList;
import java.util.List;

public class Pawn implements Piece {
    private final Color color;

    public Pawn(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public char getSymbol() {
        return 'P';
    }

    @Override
    public List<Move> getPseudoLegalMoves(Board board, Position currentPosition) {
        List<Move> moves = new ArrayList<>();
        int currentRow = currentPosition.getRow();
        int currentCol = currentPosition.getCol();
        int direction = (color == Color.WHITE) ? 1 : -1;
        int oneStepRow = currentRow + direction;
        if (Board.isValid(oneStepRow, currentCol) && board.getPiece(new Position(oneStepRow, currentCol)) == null) {
            Position endPos = new Position(oneStepRow, currentCol);

            if ((color == Color.WHITE && oneStepRow == 7) || (color == Color.BLACK && oneStepRow == 0)) {
                moves.add(new Move(currentPosition, endPos, this, null, 'Q')); // Default promotion to Queen
                moves.add(new Move(currentPosition, endPos, this, null, 'R'));
                moves.add(new Move(currentPosition, endPos, this, null, 'B'));
                moves.add(new Move(currentPosition, endPos, this, null, 'N'));
            } else {
                moves.add(new Move(currentPosition, endPos, this, null));
            }


            boolean startingRank = (color == Color.WHITE && currentRow == 1) || (color == Color.BLACK && currentRow == 6);
            if (startingRank) {
                int twoStepRow = currentRow + 2 * direction;
                if (Board.isValid(twoStepRow, currentCol) && board.getPiece(new Position(twoStepRow, currentCol)) == null) {
                    moves.add(new Move(currentPosition, new Position(twoStepRow, currentCol), this, null));
                }
            }
        }


        int[] captureCols = {currentCol - 1, currentCol + 1};
        for (int captureCol : captureCols) {
            if (Board.isValid(oneStepRow, captureCol)) {
                Position capturePos = new Position(oneStepRow, captureCol);
                Piece targetPiece = board.getPiece(capturePos);
                if (targetPiece != null && targetPiece.getColor() != this.color) {
                    if ((color == Color.WHITE && oneStepRow == 7) || (color == Color.BLACK && oneStepRow == 0)) {
                        moves.add(new Move(currentPosition, capturePos, this, targetPiece, 'Q')); // Default promotion to Queen
                        moves.add(new Move(currentPosition, capturePos, this, targetPiece, 'R'));
                        moves.add(new Move(currentPosition, capturePos, this, targetPiece, 'B'));
                        moves.add(new Move(currentPosition, capturePos, this, targetPiece, 'N'));
                    } else {
                        moves.add(new Move(currentPosition, capturePos, this, targetPiece));
                    }
                }
                else if (targetPiece == null && capturePos.equals(board.getEnPassantTarget())) {

                    Position capturedPawnPos = new Position(currentRow, captureCol);
                    Piece capturedPawn = board.getPiece(capturedPawnPos);

                    if (capturedPawn instanceof Pawn && capturedPawn.getColor() != this.color) {
                        moves.add(new Move(currentPosition, capturePos, this, capturedPawn, true)); // Mark as en passant
                    }
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