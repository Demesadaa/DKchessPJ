package test.model.pieces;

import org.junit.jupiter.api.Test;

class KnightTest {

    @Test
    void testValidLShapeMoveUp2Right1() {}

    @Test
    void testValidLShapeMoveUp2Left1() {}

    @Test
    void testValidLShapeMoveDown2Right1() {}

    @Test
    void testValidLShapeMoveDown2Left1() {}

    @Test
    void testValidLShapeMoveRight2Up1() {}

    @Test
    void testValidLShapeMoveRight2Down1() {}

    @Test
    void testValidLShapeMoveLeft2Up1() {}

    @Test
    void testValidLShapeMoveLeft2Down1() {}

    @Test
    void testInvalidStraightMove() {}

    @Test
    void testInvalidDiagonalMove() {}

    @Test
    void testValidJumpOverFriendlyPiece() {}

    @Test
    void testValidJumpOverEnemyPiece() {}

    @Test
    void testValidCaptureEnemyOnLandingSquare() {}

    @Test
    void testInvalidMoveOntoFriendlyPiece() {}

    @Test
    void testMoveFromCorner() {}
}