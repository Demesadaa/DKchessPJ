package test.model;

import model.*;
import model.pieces.Pawn;
import model.pieces.Rook;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTst {

    @Test
    public void testBasicMove() {
        Position start = new Position(1, 0);
        Position end = new Position(2, 0);
        Pawn pawn = new Pawn(Color.WHITE);

        Move move = new Move(start, end, pawn, null);

        assertEquals(start, move.getStartPosition());
        assertEquals(end, move.getEndPosition());
        assertEquals(pawn, move.getPieceMoved());
        assertNull(move.getPieceCaptured());
        assertFalse(move.isCapture());
        assertFalse(move.isPromotion());
        assertFalse(move.isCastle());
        assertFalse(move.isEnPassant());
    }

    @Test
    public void testCaptureMove() {
        Position start = new Position(1, 0);
        Position end = new Position(2, 0);
        Pawn attacker = new Pawn(Color.WHITE);
        Pawn captured = new Pawn(Color.BLACK);

        Move move = new Move(start, end, attacker, captured);

        assertTrue(move.isCapture());
        assertEquals(captured, move.getPieceCaptured());
    }

    @Test
    public void testPromotionMove() {
        Position start = new Position(6, 0);
        Position end = new Position(7, 0);
        Pawn pawn = new Pawn(Color.WHITE);

        Move move = new Move(start, end, pawn, null, 'Q');

        assertTrue(move.isPromotion());
        assertEquals('Q', move.getPromotionChar());
    }

    @Test
    public void testEnPassantMove() {
        Position start = new Position(4, 4);
        Position end = new Position(5, 5);
        Pawn pawn = new Pawn(Color.WHITE);
        Pawn capturedPawn = new Pawn(Color.BLACK);

        Move move = new Move(start, end, pawn, capturedPawn, true);

        assertTrue(move.isEnPassant());
        assertTrue(move.isCapture());
        assertEquals(capturedPawn, move.getPieceCaptured());
    }

    @Test
    public void testCastlingMove() {
        Position start = new Position(7, 4);
        Position end = new Position(7, 6);
        Rook rook = new Rook(Color.WHITE); // assuming you pass king to Move but used Rook for testing

        Move move = new Move(start, end, rook, true);

        assertTrue(move.isCastle());
        assertFalse(move.isCapture());
    }

    @Test
    public void testToStringContainsPromotion() {
        Position start = new Position(6, 0);
        Position end = new Position(7, 0);
        Pawn pawn = new Pawn(Color.WHITE);
        Move move = new Move(start, end, pawn, null, 'Q');

        String result = move.toString();
        assertTrue(result.contains("="));
        assertTrue(result.contains("Q"));
    }

    @Test
    public void testToStringContainsEnPassant() {
        Position start = new Position(4, 4);
        Position end = new Position(5, 5);
        Pawn pawn = new Pawn(Color.WHITE);
        Pawn captured = new Pawn(Color.BLACK);

        Move move = new Move(start, end, pawn, captured, true);
        String result = move.toString();

        assertTrue(result.contains("EP"));
    }

    @Test
    public void testToStringContainsCastle() {
        Position start = new Position(7, 4);
        Position end = new Position(7, 6);
        Rook rook = new Rook(Color.WHITE);

        Move move = new Move(start, end, rook, true);
        String result = move.toString();

        assertTrue(result.contains("Castle"));
    }
}
