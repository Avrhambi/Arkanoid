package geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Characterization tests for {@link Line}. They assert what the code does today,
 * including one surprising case (collinear overlap) that is locked deliberately.
 */
class LineTest {

    private static final double DELTA = 1e-9;

    @Test
    void length_middle_start_end_areGeometric() {
        Line l = new Line(0, 0, 6, 8);
        assertEquals(10.0, l.length(), DELTA);
        assertEquals(3.0, l.middle().getX(), DELTA);
        assertEquals(4.0, l.middle().getY(), DELTA);
        assertEquals(0.0, l.start().getX(), DELTA);
        assertEquals(8.0, l.end().getY(), DELTA);
    }

    @Test
    void equals_matchesSameAndReversedEndpoints_butNotDifferent() {
        Line l = new Line(0, 0, 10, 10);
        assertTrue(l.equals(new Line(0, 0, 10, 10)));
        assertTrue(l.equals(new Line(10, 10, 0, 0)));
        assertFalse(l.equals(new Line(0, 0, 10, 11)));
    }

    @Test
    void intersectionWith_cleanXCrossing_returnsCrossingPoint() {
        Line a = new Line(0, 0, 4, 4);
        Line b = new Line(0, 4, 4, 0);
        Point p = a.intersectionWith(b);
        assertNotNull(p);
        assertEquals(2.0, p.getX(), DELTA);
        assertEquals(2.0, p.getY(), DELTA);
        assertTrue(a.isIntersecting(b));
    }

    @Test
    void intersectionWith_parallelNonTouching_returnsNull() {
        Line a = new Line(0, 0, 10, 0);
        Line b = new Line(0, 5, 10, 5);
        assertNull(a.intersectionWith(b));
        assertFalse(a.isIntersecting(b));
    }

    @Test
    void intersectionWith_collinearOverlappingSegments_returnsNull() {
        // NOTE: two segments lying on the same line and overlapping (x in [5,10])
        // are reported as NOT intersecting. checkCases() only handles shared
        // endpoints, so any other collinear overlap falls through to null.
        Line a = new Line(0, 0, 10, 0);
        Line b = new Line(5, 0, 15, 0);
        assertNull(a.intersectionWith(b));
    }

    @Test
    void intersectionWith_sharedEndpoint_returnsThatEndpoint() {
        Line a = new Line(0, 0, 10, 0);
        Line b = new Line(10, 0, 10, 10);
        Point p = a.intersectionWith(b);
        assertNotNull(p);
        assertEquals(10.0, p.getX(), DELTA);
        assertEquals(0.0, p.getY(), DELTA);
    }

    @Test
    void intersectionWith_bothVerticalCollinearMeetingAtEndpoint_returnsThatPoint() {
        // Both segments are parallel to the Y axis and collinear (x = 0), meeting
        // only at (0,10). Routes through the deltaX1==0 && deltaX2==0 branch into
        // checkCases(), exercising its `this.end.equals(other.start)` positive path.
        Line a = new Line(0, 0, 0, 10);
        Line b = new Line(0, 10, 0, 20);
        Point p = a.intersectionWith(b);
        assertNotNull(p);
        assertEquals(0.0, p.getX(), DELTA);
        assertEquals(10.0, p.getY(), DELTA);
    }

    @Test
    void intersectionWith_crossesInfiniteLineButNotSegment_returnsNull() {
        // Infinite line of `a` (y = 0) meets `b` at (5, 0), but segment `a`
        // stops at x = 2, so inScopeX rejects it.
        Line a = new Line(0, 0, 2, 0);
        Line b = new Line(5, -5, 5, 5);
        assertNull(a.intersectionWith(b));
    }

    @Test
    void closestIntersectionToStartOfLine_throughRectangle_returnsNearerHit() {
        // Square with corners (0,0)-(10,10): lower edge y = upperLeft.y - height.
        Rectangle rect = new Rectangle(new Point(0, 10), 10, 10);
        Line line = new Line(5, 20, 5, -5);
        Point p = line.closestIntersectionToStartOfLine(rect);
        assertNotNull(p);
        assertEquals(5.0, p.getX(), DELTA);
        assertEquals(10.0, p.getY(), DELTA);

        // Reverse the line so the nearer hit is NOT at list index 0: start (5,-5)
        // is closest to the bottom-edge hit (5,0), which intersectionPoints lists
        // second. This forces the min-distance loop to actually pick index 1.
        Line reversed = new Line(5, -5, 5, 20);
        Point q = reversed.closestIntersectionToStartOfLine(rect);
        assertNotNull(q);
        assertEquals(5.0, q.getX(), DELTA);
        assertEquals(0.0, q.getY(), DELTA);
    }

    @Test
    void closestIntersectionToStartOfLine_missingRectangle_returnsNull() {
        Rectangle rect = new Rectangle(new Point(0, 10), 10, 10);
        Line line = new Line(20, 20, 20, -5);
        assertNull(line.closestIntersectionToStartOfLine(rect));
    }
}
