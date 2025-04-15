package model.pieces;

import model.Board;
import model.Color;
import model.Move;
import model.Position;

import java.util.List;

public interface Piece {

    Color getColor();


    char getSymbol();


    List<Move> getPseudoLegalMoves(Board board, Position currentPosition);

    boolean isValidMove(Board board, Position start, Position end);


}