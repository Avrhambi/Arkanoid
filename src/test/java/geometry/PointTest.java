package geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization tests for {@link Point}. These lock in the behaviour of the
 * code as it exists today; they are not a specification of what it "should" do.
 */
class PointTest {

    private static final double DELTA = 1e-9;

    @Test
    void distance_betweenDiagonalPoints_isHypotenuse() {
        Point a = new Point(0, 0);
        Point b = new Point(3, 4);
        assertEquals(5.0, a.distance(b), DELTA);
        assertEquals(5.0, b.distance(a), DELTA);
    }

    @Test
    void distance_toItself_isZero() {
        Point a = new Point(2.5, -7.25);
        assertEquals(0.0, a.distance(a), DELTA);
        assertEquals(0.0, a.distance(new Point(2.5, -7.25)), DELTA);
    }

    @Test
    void equals_whenCoordinatesDifferJustBelowTolerance_isTrue() {
        // Point.equals uses `Math.abs(dx) <= Math.pow(10, -13)`. Math.pow(10,-13)
        // is a deterministic constant equal to the nearest double to 1e-13; the
        // representation error of `1.0 + 9e-14` (~1e-16) is far smaller than the
        // 1e-14 gap to the boundary, so this pins the "just inside" side.
        Point a = new Point(1.0, 1.0);
        Point b = new Point(1.0 + 9e-14, 1.0 - 9e-14);
        assertTrue(a.equals(b));
    }

    @Test
    void equals_whenCoordinatesDifferJustAboveTolerance_isFalse() {
        // 1.1e-13 is just past the 1e-13 boundary -> not equal.
        Point a = new Point(1.0, 1.0);
        Point b = new Point(1.0 + 1.1e-13, 1.0);
        assertFalse(a.equals(b));
    }

    @Test
    void setX_setY_mutateInPlace() {
        Point a = new Point(0, 0);
        a.setX(10);
        a.setY(-5);
        assertEquals(10.0, a.getX(), DELTA);
        assertEquals(-5.0, a.getY(), DELTA);
    }
}
