package model;

import model.*;
import model.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MoveInterpreter {

    private static final Pattern CASTLE_KINGSIDE = Pattern.compile("^(O-O|0-0)$");
    private static final Pattern CASTLE_QUEENSIDE = Pattern.compile("^(O-O-O|0-0-0)$");
    private static final Pattern MOVE_PATTERN = Pattern.compile(
            "([KQRNB])?" +
                    "([a-h])?" +
                    "([1-8])?" +
                    "(x)?" +
                    "([a-h][1-8])" +
                    "(?:=([QRNB]))?" +
                    "[+#]?"
    );

    // InterpretationResult class (same as before)
    public static class InterpretationResult {
        public final Move move;
        public final String errorMessage;

        public InterpretationResult(Move move) {
            this.move = move;
            this.errorMessage = null;
        }

        public InterpretationResult(String errorMessage) {
            this.move = null;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return move != null;
        }
    }


    public InterpretationResult interpretMove(Board board, String pgnMove) {
        Color player = board.getCurrentPlayer();
        List<Move> legalMoves = board.generateLegalMoves();

        if (CASTLE_KINGSIDE.matcher(pgnMove).matches()) {
            Move castleMove = findCastleMove(legalMoves, player, true);
            if (castleMove != null) {
                return new InterpretationResult(castleMove);
            } else {
                return new InterpretationResult("Illegal move: Kingside Castling not legal in this position.");
            }
        }
        if (CASTLE_QUEENSIDE.matcher(pgnMove).matches()) {
            Move castleMove = findCastleMove(legalMoves, player, false);
            if (castleMove != null) {
                return new InterpretationResult(castleMove);
            } else {
                return new InterpretationResult("Illegal move: Queenside Castling not legal in this position.");
            }
        }

        Matcher matcher = MOVE_PATTERN.matcher(pgnMove);
        if (!matcher.matches()) {
            if (pgnMove.equals("O-O") || pgnMove.equals("0-0")) {
                Move castleMove = findCastleMove(legalMoves, player, true);
                return castleMove != null ? new InterpretationResult(castleMove) : new InterpretationResult("Illegal move: Kingside Castling not legal (checked again).");
            }
            if (pgnMove.equals("O-O-O") || pgnMove.equals("0-0-0")) {
                Move castleMove = findCastleMove(legalMoves, player, false);
                return castleMove != null ? new InterpretationResult(castleMove) : new InterpretationResult("Illegal move: Queenside Castling not legal (checked again).");
            }
            return new InterpretationResult("Invalid move format: '" + pgnMove + "'");
        }

        String pieceStr = matcher.group(1);
        String disFile = matcher.group(2);
        String disRank = matcher.group(3);
        boolean isCaptureIndicated = matcher.group(4) != null;
        String targetSquareStr = matcher.group(5);
        String promotionStr = matcher.group(6);

        Class<? extends Piece> pieceType = getPieceType(pieceStr);
        Position targetPos = Position.fromAlgebraic(targetSquareStr);
        char promotionChar = (promotionStr != null) ? promotionStr.charAt(0) : '\0';

        List<Move> candidates = new ArrayList<>();
        for (Move legal : legalMoves) {
            Piece movingPiece = legal.getPieceMoved();
            if (legal.isCastle()) continue;
            if (!legal.getEndPosition().equals(targetPos)) continue;
            if (movingPiece.getClass() != pieceType) continue;
            if (promotionChar != '\0' && (!legal.isPromotion() || legal.getPromotionChar() != promotionChar)) continue;
            if (promotionChar == '\0' && legal.isPromotion()) continue;
            if (disFile != null && legal.getStartPosition().getFileChar() != disFile.charAt(0)) continue;
            if (disRank != null && legal.getStartPosition().getRankChar() != disRank.charAt(0)) continue;
            candidates.add(legal);
        }
        List<Move> finalCandidates = candidates.stream()
                .filter(move -> move.isCapture() == isCaptureIndicated)
                .collect(Collectors.toList());

        List<Move> listToUse = finalCandidates.isEmpty() ? candidates : finalCandidates;

        if (listToUse.isEmpty()) {
            String debugLegalMoves = legalMoves.stream().map(Move::toString).collect(Collectors.joining(", "));
            System.err.println("DEBUG: No matching legal move found for '" + pgnMove + "'. Target: " + targetPos + ", PieceType: " + pieceType.getSimpleName() + ", Promo: " + promotionChar + ", disFile: " + disFile + ", disRank: " + disRank);
            System.err.println("DEBUG: Legal moves were: [" + debugLegalMoves + "]");
            return new InterpretationResult("Illegal move: No legal move found matching '" + pgnMove + "'");
        } else if (listToUse.size() == 1) {
            return new InterpretationResult(listToUse.get(0));
        } else {
            return new InterpretationResult("Ambiguous move: '" + pgnMove + "' matches multiple legal moves: " + listToUse);
        }
    }

    private Move findCastleMove(List<Move> legalMoves, Color player, boolean isKingside) {
        for (Move legal : legalMoves) {
            if (legal.isCastle() && legal.getPieceMoved().getColor() == player) {
                boolean kingsideMatch = isKingside && legal.getEndPosition().getCol() == 6;
                boolean queensideMatch = !isKingside && legal.getEndPosition().getCol() == 2;
                if (kingsideMatch || queensideMatch) {
                    return legal;
                }
            }
        }
        return null;
    }

    private Class<? extends Piece> getPieceType(String pieceStr) {
        if (pieceStr == null) return Pawn.class;
        switch (pieceStr) {
            case "K": return King.class;
            case "Q": return Queen.class;
            case "R": return Rook.class;
            case "B": return Bishop.class;
            case "N": return Knight.class;
            default: return Pawn.class;
        }
    }
}