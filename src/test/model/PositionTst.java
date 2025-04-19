package test.model;

import model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTst {

    @Test
    void constructorValid() {
        Position pos = new Position(3, 5);
        assertEquals(3, pos.getRow());
        assertEquals(5, pos.getCol());
    }

    @Test
    void constructorInvalidRowLow() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 4));
    }

    @Test
    void constructorInvalidRowHigh() {
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 4));
    }

    @Test
    void constructorInvalidColLow() {
        assertThrows(IllegalArgumentException.class, () -> new Position(2, -1));
    }

    @Test
    void constructorInvalidColHigh() {
        assertThrows(IllegalArgumentException.class, () -> new Position(2, 8));
    }

    @Test
    void getRow() {
        Position pos = new Position(6, 2);
        assertEquals(6, pos.getRow());
    }

    @Test
    void getCol() {
        Position pos = new Position(4, 7);
        assertEquals(7, pos.getCol());
    }

    @Test
    void equalsSameObject() {
        Position pos = new Position(2, 3);
        assertEquals(pos, pos);
    }

    @Test
    void equalsEqualObjects() {
        Position p1 = new Position(5, 2);
        Position p2 = new Position(5, 2);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void equalsDifferentRow() {
        Position p1 = new Position(5, 2);
        Position p2 = new Position(4, 2);
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsDifferentCol() {
        Position p1 = new Position(5, 2);
        Position p2 = new Position(5, 1);
        assertNotEquals(p1, p2);
    }

    @Test
    void equalsNull() {
        Position pos = new Position(3, 3);
        assertNotEquals(pos, null);
    }

    @Test
    void equalsDifferentClass() {
        Position pos = new Position(3, 3);
        assertNotEquals(pos, "not a position");
    }

    @Test
    void hashCodeConsistent() {
        Position pos = new Position(4, 4);
        int hash1 = pos.hashCode();
        int hash2 = pos.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    void hashCodeEqualObjects() {
        Position p1 = new Position(1, 6);
        Position p2 = new Position(1, 6);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void hashCodeDifferentObjects() {
        Position p1 = new Position(1, 6);
        Position p2 = new Position(2, 6);
        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("e2", new Position(1, 4).toString());
        assertEquals("a1", new Position(0, 0).toString());
        assertEquals("h8", new Position(7, 7).toString());
    }

    @Test
    void fromAlgebraicValid() {
        assertEquals(new Position(0, 0), Position.fromAlgebraic("a1"));
        assertEquals(new Position(7, 7), Position.fromAlgebraic("h8"));
        assertEquals(new Position(6, 3), Position.fromAlgebraic("d7"));
    }

    @Test
    void fromAlgebraicInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("99"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("a"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic(""));
    }

    @Test
    void fromAlgebraicInvalidRank() {
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("a9"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("h0"));
    }

    @Test
    void fromAlgebraicInvalidFile() {
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("i2"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraic("z7"));
    }
}
