package utils;

import model.Position;
import model.pieces.*;
import model.Color;
public class PgnUtils {


    private PgnUtils() {}

    public static Position algebraicToPosition(String alg) {
        return Position.fromAlgebraic(alg);
    }

    public static String positionToAlgebraic(Position pos) {
        if (pos == null) return "-"; // Or throw exception
        return pos.toString();
    }


    public static Class<? extends Piece> getPieceTypeFromChar(char pieceChar) {
        switch (Character.toUpperCase(pieceChar)) {
            case 'K': return King.class;
            case 'Q': return Queen.class;
            case 'R': return Rook.class;
            case 'B': return Bishop.class;
            case 'N': return Knight.class;
            case 'P': return Pawn.class;
            default: return null;
        }
    }

    public static String getPieceCharForPgn(Piece piece) {
        if (piece instanceof Pawn) return "";
        if (piece instanceof King) return "K";
        if (piece instanceof Queen) return "Q";
        if (piece instanceof Rook) return "R";
        if (piece instanceof Bishop) return "B";
        if (piece instanceof Knight) return "N";
        return "?";
    }
}