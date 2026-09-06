package collidable;

import geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Characterization tests for {@link Velocity}.
 *
 * <p>{@code fromAngleAndSpeed} subtracts 90 from the angle internally, so angle 0
 * points straight up (negative dy in screen coordinates) and angle 90 points right.
 */
class VelocityTest {

    private static final double DELTA = 1e-9;

    @Test
    void constructorAndAccessors_roundTrip() {
        Velocity v = new Velocity(3, -4);
        assertEquals(3.0, v.getDx(), DELTA);
        assertEquals(-4.0, v.getDy(), DELTA);
        v.setDx(7);
        v.setDy(9);
        assertEquals(7.0, v.getDx(), DELTA);
        assertEquals(9.0, v.getDy(), DELTA);
    }

    @Test
    void fromAngleAndSpeed_angleZero_pointsStraightUp() {
        Velocity v = Velocity.fromAngleAndSpeed(0, 5);
        assertEquals(0.0, v.getDx(), DELTA);
        assertEquals(-5.0, v.getDy(), DELTA);
    }

    @Test
    void fromAngleAndSpeed_angleNinety_pointsRight() {
        Velocity v = Velocity.fromAngleAndSpeed(90, 5);
        assertEquals(5.0, v.getDx(), DELTA);
        assertEquals(0.0, v.getDy(), DELTA);
    }

    @Test
    void fromAngleAndSpeed_diagonal_splitsBySqrtTwo() {
        Velocity v = Velocity.fromAngleAndSpeed(45, 5);
        double component = 5 * Math.sqrt(2) / 2;
        assertEquals(component, v.getDx(), DELTA);
        assertEquals(-component, v.getDy(), DELTA);
    }

    @Test
    void applyToPoint_returnsShiftedPoint_andDoesNotMutateInput() {
        Velocity v = new Velocity(2, 3);
        Point original = new Point(10, 20);
        Point moved = v.applyToPoint(original);
        assertEquals(12.0, moved.getX(), DELTA);
        assertEquals(23.0, moved.getY(), DELTA);
        assertEquals(10.0, original.getX(), DELTA);
        assertEquals(20.0, original.getY(), DELTA);
    }
}
