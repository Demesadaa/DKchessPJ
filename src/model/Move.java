package model;

import model.pieces.Piece;
public class Move {
    private final Position startPosition;
    private final Position endPosition;
    private final Piece pieceMoved;
    private final Piece pieceCaptured;
    private final char promotionChar;
    private final boolean isCastle;
    private final boolean isEnPassant;


    public Move(Position start, Position end, Piece moved, Piece captured) {
        this(start, end, moved, captured, '\0', false, false);
    }
    public Move(Position start, Position end, Piece moved, Piece captured, char promotion) {
        this(start, end, moved, captured, promotion, false, false);
    }
    public Move(Position start, Position end, Piece moved, Piece capturedPawn, boolean isEnPassantCapture) {
        this(start, end, moved, capturedPawn, '\0', false, isEnPassantCapture);
        if (!isEnPassantCapture) {
            throw new IllegalArgumentException("This constructor is only for en passant captures.");
        }
    }
    public Move(Position start, Position end, Piece king, boolean castling) {
        this(start, end, king, null, '\0', castling, false);
        if (!castling) {
            throw new IllegalArgumentException("This constructor is only for castling moves.");
        }
    }

    public Move(Position start, Position end, Piece moved, Piece captured, char promotion, boolean castle, boolean enPassant) {
        this.startPosition = start;
        this.endPosition = end;
        this.pieceMoved = moved;
        this.pieceCaptured = captured;
        this.promotionChar = promotion;
        this.isCastle = castle;
        this.isEnPassant = enPassant;
    }

    // Getters
    public Position getStartPosition() { return startPosition; }
    public Position getEndPosition() { return endPosition; }
    public Piece getPieceMoved() { return pieceMoved; }
    public Piece getPieceCaptured() { return pieceCaptured; }
    public char getPromotionChar() { return promotionChar; }
    public boolean isPromotion() { return promotionChar != '\0'; }
    public boolean isCapture() { return pieceCaptured != null; }
    public boolean isCastle() { return isCastle; }
    public boolean isEnPassant() { return isEnPassant; }


    @Override
    public String toString() {
        return pieceMoved.getSymbol() + "@" + startPosition + " -> " + endPosition +
                (isCapture() ? "x" + pieceCaptured.getSymbol() : "") +
                (isPromotion() ? "=" + promotionChar : "") +
                (isCastle ? " Castle" : "") +
                (isEnPassant ? " EP" : "");
    }
}