package test.model;


import model.Board;
import model.pieces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BoardTst {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board(); // Assumes constructor calls initialize()
    }

    @Test
    void initializeDefaultPosition() {
        // Verify pieces are in correct starting squares
        // Verify starting player is WHITE
        // Verify castling rights are initially all true
        // Verify en passant target is null
        // Verify move clocks are 0 / 1
    }

    @Test
    void getPieceValid() {
        // Test getting a piece from a known square (e.g., white pawn at e2)
    }

    @Test
    void getPieceEmptySquare() {
        // Test getting null from an empty square (e.g., e3)
    }

    @Test
    void getCurrentPlayerInitial() {
        // Test initial player
    }

    @Test
    void makeMoveSimplePawn() {
        // Make move e2-e4
        // Verify piece moved from e2
        // Verify piece arrived at e4
        // Verify player switched to BLACK
        // Verify en passant target set correctly
    }

    @Test
    void makeMoveCapture() {
        // Set up a capture scenario (e.g., e4, d5, exd5)
        // Make the capture move
        // Verify captured piece is gone
        // Verify capturing piece is on target square
        // Verify player switched
        // Verify half move clock reset
    }

    @Test
    void makeMoveEnPassant() {
        // Set up en passant scenario (e.g., e4, c5, e5, d5, exd6 e.p.)
        // Make the en passant capture
        // Verify pawn moved correctly
        // Verify captured pawn removed correctly (from d5, not d6)
        // Verify player switched
    }

    @Test
    void makeMovePromotion() {
        // Set up promotion scenario (e.g., pawn reaches 8th rank)
        // Make the promotion move (e.g., to Queen)
        // Verify correct promoted piece is on square
        // Verify player switched
    }

    @Test
    void makeMoveCastleKingSideWhite() {
        // Remove f1, g1 pieces
        // Make O-O move (assuming Move object represents it)
        // Verify King moved to g1
        // Verify Rook moved to f1
        // Verify white castling rights updated (both K and Q side false)
        // Verify player switched
    }

    @Test
    void makeMoveCastleQueenSideBlack() {
        // Setup scenario for black O-O-O
        // Make the move
        // Verify king/rook positions
        // Verify black castling rights updated
        // Verify player switched
    }

    @Test
    void makeMoveUpdatesCastlingRightsKingMove() {
        // Move white king
        // Verify white K/Q side castle rights are false
    }

    @Test
    void makeMoveUpdatesCastlingRightsRookMove() {
        // Move white a1 rook
        // Verify white Q side castle right is false, K side remains true
    }

    @Test
    void makeMoveUpdatesCastlingRightsCaptureRook() {
        // Setup scenario where black captures h1 rook
        // Verify white K side castle right becomes false
    }

    @Test
    void makeMoveUpdatesHalfMoveClockPawn() {
        // Make pawn move
        // Verify half move clock is 0
    }

    @Test
    void makeMoveUpdatesHalfMoveClockCapture() {
        // Make capture move
        // Verify half move clock is 0
    }

    @Test
    void makeMoveUpdatesHalfMoveClockPieceMove() {
        // Make knight move (non-capture)
        // Verify half move clock incremented
    }

    @Test
    void makeMoveUpdatesFullMoveClock() {
        // Make white move, then black move
        // Verify full move number incremented after black's move
    }


    @Test
    void findKing() {
        // Test finding white and black kings in initial position
    }

    @Test
    void isSquareAttacked() {
        // Test squares attacked by various pieces
        // Test squares not attacked
    }

    @Test
    void isInCheckInitial() {
        // Test neither king is in check initially
    }

    @Test
    void isInCheckScenario() {
        // Set up a check scenario (e.g., Fool's Mate setup)
        // Verify the correct king is in check
    }

    @Test
    void generateLegalMovesInitial() {
        // Verify initial legal moves (20 total: 16 pawn, 4 knight)
        // Check count and perhaps specific moves like e2-e4, Ng1-f3
    }

    @Test
    void generateLegalMovesCheckScenario() {
        // Set up a check scenario
        // Verify generated moves are only those that block check or move king out of check
    }

    @Test
    void generateLegalMovesPinnedPiece() {
        // Set up a scenario where a piece is pinned to the king
        // Verify the pinned piece can only move along the pin line (or not at all if absolutely pinned)
    }

    @Test
    void generateLegalMovesCastlingAllowed() {
        // From initial position, check if O-O and O-O-O moves are generated (they shouldn't be directly, they come from King)
        // OR, if your generateLegalMoves includes special castle moves, test their presence
    }

    @Test
    void generateLegalMovesCastlingBlockedPath() {
        // Place piece between king/rook
        // Verify castling move is not generated
    }

    @Test
    void generateLegalMovesCastlingThroughCheck() {
        // Set up scenario where square king moves over is attacked
        // Verify castling move is not generated
    }

    @Test
    void generateLegalMovesCastlingInCheck() {
        // Put king in check
        // Verify castling move is not generated
    }

    @Test
    void generateLegalMovesEnPassant() {
        // Set up valid en passant capture scenario
        // Verify the en passant capture move is generated for the pawn
    }

    @Test
    void generateLegalMovesPromotion() {
        // Set up pawn near promotion rank
        // Verify promotion moves (to Q, R, B, N) are generated
    }

    @Test
    void copyBoardState() {
        // Make a move on original board
        // Copy the board
        // Verify copy has same pieces, player turn, castle rights, ep target etc.
        // Make another move on original - verify copy is unchanged
    }

    @Test
    void testToString() {
        // Check if board representation looks reasonable
    }
}
