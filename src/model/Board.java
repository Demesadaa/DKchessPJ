package model;

import model.pieces.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Piece[][] squares;
    private Color currentPlayer;
    private boolean whiteKingSideCastle;
    private boolean whiteQueenSideCastle;
    private boolean blackKingSideCastle;
    private boolean blackQueenSideCastle;
    private Position enPassantTarget;
    private int halfMoveClock;
    private int fullMoveNumber;

    public Board() {
        squares = new Piece[8][8];
        initialize();
    }

    public void initialize() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                squares[r][c] = null;
            }
        }
        for (int c = 0; c < 8; c++) {
            squares[1][c] = new Pawn(Color.WHITE);
            squares[6][c] = new Pawn(Color.BLACK);
        }
        squares[0][0] = new Rook(Color.WHITE); squares[0][7] = new Rook(Color.WHITE);
        squares[7][0] = new Rook(Color.BLACK); squares[7][7] = new Rook(Color.BLACK);
        squares[0][1] = new Knight(Color.WHITE); squares[0][6] = new Knight(Color.WHITE);
        squares[7][1] = new Knight(Color.BLACK); squares[7][6] = new Knight(Color.BLACK);
        squares[0][2] = new Bishop(Color.WHITE); squares[0][5] = new Bishop(Color.WHITE);
        squares[7][2] = new Bishop(Color.BLACK); squares[7][5] = new Bishop(Color.BLACK);
        squares[0][3] = new Queen(Color.WHITE);
        squares[7][3] = new Queen(Color.BLACK);
        squares[0][4] = new King(Color.WHITE);
        squares[7][4] = new King(Color.BLACK);
        currentPlayer = Color.WHITE;
        whiteKingSideCastle = true;
        whiteQueenSideCastle = true;
        blackKingSideCastle = true;
        blackQueenSideCastle = true;
        enPassantTarget = null;
        halfMoveClock = 0;
        fullMoveNumber = 1;
    }

    public Piece getPiece(Position pos) {
        if (!pos.isValid()) return null;
        return squares[pos.getRow()][pos.getCol()];
    }

    private void setPiece(Position pos, Piece piece) {
        if (!pos.isValid()) return;
        squares[pos.getRow()][pos.getCol()] = piece;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public Position getEnPassantTarget() {
        return enPassantTarget;
    }

    public static boolean isValid(int row, int col) {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    public void makeMove(Move move) {
        Position start = move.getStartPosition();
        Position end = move.getEndPosition();
        Piece movedPiece = move.getPieceMoved();
        setPiece(start, null);

        if (move.isPromotion()) {
            setPiece(end, createPromotedPiece(move.getPromotionChar(), movedPiece.getColor()));
        } else if (move.isCastle()) {
            setPiece(end, movedPiece);
            Position rookStart, rookEnd;
            if (end.getCol() > start.getCol()) {
                rookStart = new Position(start.getRow(), 7);
                rookEnd = new Position(start.getRow(), 5);
            } else {
                rookStart = new Position(start.getRow(), 0);
                rookEnd = new Position(start.getRow(), 3);
            }
            setPiece(rookEnd, getPiece(rookStart));
            setPiece(rookStart, null);
        } else if (move.isEnPassant()) {
            setPiece(end, movedPiece);
            int capturedPawnRow = start.getRow();
            int capturedPawnCol = end.getCol();
            setPiece(new Position(capturedPawnRow, capturedPawnCol), null);
        } else {
            setPiece(end, movedPiece);
        }

        Position previousEPTarget = enPassantTarget;
        enPassantTarget = null;
        if (movedPiece instanceof Pawn && Math.abs(end.getRow() - start.getRow()) == 2) {
            int targetRow = (start.getRow() + end.getRow()) / 2;
            enPassantTarget = new Position(targetRow, start.getCol());
        }

        if (movedPiece instanceof King) {
            if (movedPiece.getColor() == Color.WHITE) {
                whiteKingSideCastle = false;
                whiteQueenSideCastle = false;
            } else {
                blackKingSideCastle = false;
                blackQueenSideCastle = false;
            }
        } else if (movedPiece instanceof Rook) {
            if (movedPiece.getColor() == Color.WHITE) {
                if (start.equals(new Position(0, 0))) whiteQueenSideCastle = false;
                if (start.equals(new Position(0, 7))) whiteKingSideCastle = false;
            } else {
                if (start.equals(new Position(7, 0))) blackQueenSideCastle = false;
                if (start.equals(new Position(7, 7))) blackKingSideCastle = false;
            }
        }

        Piece captured = move.getPieceCaptured();
        if (captured instanceof Rook) {
            if (end.equals(new Position(0, 0))) whiteQueenSideCastle = false;
            if (end.equals(new Position(0, 7))) whiteKingSideCastle = false;
            if (end.equals(new Position(7, 0))) blackQueenSideCastle = false;
            if (end.equals(new Position(7, 7))) blackKingSideCastle = false;
        }

        if (movedPiece instanceof Pawn || move.isCapture()) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }

        if (currentPlayer == Color.BLACK) {
            fullMoveNumber++;
        }

        currentPlayer = currentPlayer.opposite();
    }

    private Piece createPromotedPiece(char promotionChar, Color color) {
        switch (Character.toUpperCase(promotionChar)) {
            case 'Q': return new Queen(color);
            case 'R': return new Rook(color);
            case 'B': return new Bishop(color);
            case 'N': return new Knight(color);
            default: throw new IllegalArgumentException("Invalid promotion piece: " + promotionChar);
        }
    }

    public boolean isSquareAttacked(Position target, Color attackerColor) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position currentPos = new Position(r, c);
                Piece p = getPiece(currentPos);
                if (p != null && p.getColor() == attackerColor) {
                    if (p instanceof Pawn) {
                        int direction = (attackerColor == Color.WHITE) ? 1 : -1;
                        int targetRow = target.getRow();
                        int targetCol = target.getCol();
                        if (targetRow == r + direction && (targetCol == c + 1 || targetCol == c - 1)) {
                            return true;
                        }
                    } else {
                        if (p.isValidMove(this, currentPos, target)) {
                            List<Move> moves = p.getPseudoLegalMoves(this, currentPos);
                            for (Move m : moves) {
                                if (m.getEndPosition().equals(target)) return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public Position findKing(Color kingColor) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position pos = new Position(r, c);
                Piece p = getPiece(pos);
                if (p instanceof King && p.getColor() == kingColor) {
                    return pos;
                }
            }
        }
        return null;
    }

    public boolean isInCheck(Color kingColor) {
        Position kingPos = findKing(kingColor);
        if (kingPos == null) {
            System.err.println("Error: King not found for color " + kingColor);
            return false;
        }
        return isSquareAttacked(kingPos, kingColor.opposite());
    }

    public List<Move> generateLegalMoves() {
        List<Move> legalMoves = new ArrayList<>();
        Color player = getCurrentPlayer();

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Position startPos = new Position(r, c);
                Piece piece = getPiece(startPos);

                if (piece != null && piece.getColor() == player) {
                    List<Move> pseudoLegal = piece.getPseudoLegalMoves(this, startPos);

                    for (Move move : pseudoLegal) {
                        Board tempBoard = this.copy();
                        tempBoard.makeMove(move);
                        if (!tempBoard.isInCheck(player)) {
                            if (move.isCastle()) {
                                if (!canCastle(move)) continue;
                            }
                            legalMoves.add(move);
                        }
                    }
                }
            }
        }
        return legalMoves;
    }

    private boolean canCastle(Move castleMove) {
        Color player = castleMove.getPieceMoved().getColor();
        Position kingStart = castleMove.getStartPosition();
        Position kingEnd = castleMove.getEndPosition();

        if (isInCheck(player)) return false;

        int rookCol = (kingEnd.getCol() > kingStart.getCol()) ? 7 : 0;
        int step = (kingEnd.getCol() > kingStart.getCol()) ? 1 : -1;

        for (int c = kingStart.getCol() + step; c != rookCol; c += step) {
            if (getPiece(new Position(kingStart.getRow(), c)) != null) return false;
        }

        Position intermediateSquare;
        if (kingEnd.getCol() == 2) {
            intermediateSquare = new Position(kingStart.getRow(), 3);
        } else {
            intermediateSquare = new Position(kingStart.getRow(), 5);
        }

        if (isSquareAttacked(intermediateSquare, player.opposite())) return false;

        if (player == Color.WHITE) {
            if (rookCol == 7 && !whiteKingSideCastle) return false;
            if (rookCol == 0 && !whiteQueenSideCastle) return false;
        } else {
            if (rookCol == 7 && !blackKingSideCastle) return false;
            if (rookCol == 0 && !blackQueenSideCastle) return false;
        }

        return true;
    }


    public Board copy() {
        Board newBoard = new Board();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                newBoard.squares[r][c] = this.squares[r][c];
            }
        }
        newBoard.currentPlayer = this.currentPlayer;
        newBoard.whiteKingSideCastle = this.whiteKingSideCastle;
        newBoard.whiteQueenSideCastle = this.whiteQueenSideCastle;
        newBoard.blackKingSideCastle = this.blackKingSideCastle;
        newBoard.blackQueenSideCastle = this.blackQueenSideCastle;
        newBoard.enPassantTarget = this.enPassantTarget;
        newBoard.halfMoveClock = this.halfMoveClock;
        newBoard.fullMoveNumber = this.fullMoveNumber;
        return newBoard;
    }

    public boolean canWhiteKingSideCastle() { return whiteKingSideCastle; }
    public boolean canWhiteQueenSideCastle() { return whiteQueenSideCastle; }
    public boolean canBlackKingSideCastle() { return blackKingSideCastle; }
    public boolean canBlackQueenSideCastle() { return blackQueenSideCastle; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        for (int r = 7; r >= 0; r--) {
            sb.append(r + 1).append(" ");
            for (int c = 0; c < 8; c++) {
                Piece p = squares[r][c];
                char symbol = '.';
                if (p != null) {
                    symbol = p.getSymbol();
                    if (p.getColor() == Color.BLACK) {
                        symbol = Character.toLowerCase(symbol);
                    }
                }
                sb.append(symbol).append(" ");
            }
            sb.append(r + 1).append("\n");
        }
        sb.append("  a b c d e f g h\n");
        sb.append("Turn: ").append(currentPlayer);
        sb.append(" | Castle: ");
        sb.append(whiteKingSideCastle ? "K" : "-");
        sb.append(whiteQueenSideCastle ? "Q" : "-");
        sb.append(blackKingSideCastle ? "k" : "-");
        sb.append(blackQueenSideCastle ? "q" : "-");
        sb.append(" | EP: ").append(enPassantTarget != null ? enPassantTarget.toString() : "-");
        sb.append(" | Halfmove: ").append(halfMoveClock);
        sb.append(" | Fullmove: ").append(fullMoveNumber);
        sb.append("\n");
        return sb.toString();
    }
}
