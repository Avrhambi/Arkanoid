package geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization tests for {@link Rectangle}.
 *
 * <p>Coordinate convention: Y grows upward, so the rectangle's lower edge sits
 * at {@code upperLeft.y - height}. The square used here spans (0,0)-(10,10).
 */
class RectangleTest {

    private static final double DELTA = 1e-9;

    private Rectangle square() {
        return new Rectangle(new Point(0, 10), 10, 10);
    }

    @Test
    void accessors_returnConstructorValues() {
        Rectangle r = square();
        assertEquals(10.0, r.getWidth(), DELTA);
        assertEquals(10.0, r.getHeight(), DELTA);
        assertEquals(0.0, r.getUpperLeft().getX(), DELTA);
        assertEquals(10.0, r.getUpperLeft().getY(), DELTA);
    }

    @Test
    void nonSquare_keepsWidthAndHeightDistinct() {
        // A 20-wide, 5-tall rectangle: x in [0,20], y in [5,10]. If the ctor
        // swapped width/height, it would be x in [0,5] and a vertical line at
        // x=15 would miss it entirely instead of crossing top and bottom edges.
        Rectangle r = new Rectangle(new Point(0, 10), 20, 5);
        assertEquals(20.0, r.getWidth(), DELTA);
        assertEquals(5.0, r.getHeight(), DELTA);

        List<Point> pts = r.intersectionPoints(new Line(15, 20, 15, -5));
        assertEquals(2, pts.size());
        assertTrue(pts.stream().anyMatch(p -> p.equals(new Point(15, 10))));
        assertTrue(pts.stream().anyMatch(p -> p.equals(new Point(15, 5))));
    }

    @Test
    void intersectionPoints_lineCrossingTwoEdges_returnsTwoPoints() {
        Line line = new Line(5, 20, 5, -5);
        List<Point> pts = square().intersectionPoints(line);
        assertEquals(2, pts.size());
        assertTrue(pts.stream().anyMatch(p -> p.equals(new Point(5, 10))));
        assertTrue(pts.stream().anyMatch(p -> p.equals(new Point(5, 0))));
    }

    @Test
    void intersectionPoints_lineTouchingOneCorner_returnsSinglePoint() {
        // NOTE: the (0,0) corner is hit by both the left edge and the bottom
        // edge. Rectangle.isPointInList (whose name is inverted: it returns true
        // when the point is ABSENT) dedups the second hit, so the list holds one.
        Line line = new Line(-5, 5, 5, -5);
        List<Point> pts = square().intersectionPoints(line);
        assertEquals(1, pts.size());
        assertTrue(pts.get(0).equals(new Point(0, 0)));
    }

    @Test
    void intersectionPoints_lineEntirelyOutside_returnsEmptyList() {
        Line line = new Line(20, 20, 20, 30);
        assertTrue(square().intersectionPoints(line).isEmpty());
    }
}
