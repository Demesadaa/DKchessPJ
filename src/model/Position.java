package model;

import java.util.Objects;


public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            throw new IllegalArgumentException("Invalid row or column index: " + row + ", " + col);
        }
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isValid() {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        char file = (char) ('a' + col);
        char rank = (char) ('1' + row);
        return "" + file + rank;
    }

    public static Position fromAlgebraic(String alg) {
        if (alg == null || alg.length() != 2) {
            throw new IllegalArgumentException("Invalid algebraic notation: " + alg);
        }
        char fileChar = Character.toLowerCase(alg.charAt(0));
        char rankChar = alg.charAt(1);

        if (fileChar < 'a' || fileChar > 'h' || rankChar < '1' || rankChar > '8') {
            throw new IllegalArgumentException("Invalid algebraic notation: " + alg);
        }

        int col = fileChar - 'a';
        int row = rankChar - '1';
        return new Position(row, col);
    }
}