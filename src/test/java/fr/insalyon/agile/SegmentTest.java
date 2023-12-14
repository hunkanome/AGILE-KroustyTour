package fr.insalyon.agile;

import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Segment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTest {

    @Test
    void testSegmentEquals() {
        Intersection intersection1 = new Intersection(1L, new GeoCoordinates(0f, 0f), 15);
        Intersection intersection2 = new Intersection(2L, new GeoCoordinates(0f, 0f), 16);
        String street1 = "Rue un";
        String street2 = "Rue deux";
        float length1 = 10f;
        float length2 = 20f;

        Segment segment1 = new Segment(intersection1, intersection2, street1, length1);
        Segment segment2 = new Segment(intersection1, intersection2, street1, length1);

        assertEquals(segment1, segment1);
        assertNotEquals(segment1, null);
        assertNotEquals(segment1, new Object());
        assertEquals(segment1, segment2);

        // If origin is different we ignore it to avoid infinite recursion with Intersection

        // If destination is different
        segment2 = new Segment(intersection1, intersection1, street1, length1);
        assertNotEquals(segment1, segment2);

        // If name is different
        segment2 = new Segment(intersection1, intersection2, street2, length1);
        assertNotEquals(segment1, segment2);

        // If length is different
        segment2 = new Segment(intersection1, intersection2, street1, length2);
        assertNotEquals(segment1, segment2);
    }
}
