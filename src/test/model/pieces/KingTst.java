package test.model.pieces;


import org.junit.jupiter.api.Test;

class KingTest {

    @Test
    void testValidMoveOneSquareForward() {}

    @Test
    void testValidMoveOneSquareBackward() {}

    @Test
    void testValidMoveOneSquareLeft() {}

    @Test
    void testValidMoveOneSquareRight() {}

    @Test
    void testValidMoveOneSquareDiagonalForwardRight() {}

    @Test
    void testValidMoveOneSquareDiagonalBackwardLeft() {}

    @Test
    void testInvalidMoveTwoSquaresForward() {}

    @Test
    void testInvalidMoveTwoSquaresDiagonal() {}

    @Test
    void testValidCaptureEnemyPiece() {}

    @Test
    void testInvalidMoveOntoFriendlyPiece() {}

    @Test
    void testValidCastleKingsideClearPathNotCheck() {}

    @Test
    void testValidCastleQueensideClearPathNotCheck() {}

    @Test
    void testInvalidCastleKingMoved() {}

    @Test
    void testInvalidCastleRookMoved() {}

    @Test
    void testInvalidCastlePathBlocked() {}

    @Test
    void testInvalidCastleWhileInCheck() {}

    @Test
    void testInvalidCastleThroughCheck() {}

}