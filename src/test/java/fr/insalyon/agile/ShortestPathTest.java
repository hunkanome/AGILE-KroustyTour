package fr.insalyon.agile;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.algorithm.Dijkstra;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ShortestPathTest {
    CityMap map;
    Intersection start;
    Intersection end;

    @BeforeAll
    public void createGraph() {
        Intersection start = new Intersection(0, new GeoCoordinates(1f,1f), 0);
        Intersection nodeA = new Intersection(1, new GeoCoordinates(2f,2f), 1);
        Intersection nodeB = new Intersection(2, new GeoCoordinates(3f,3f), 2);
        Intersection nodeC = new Intersection(3, new GeoCoordinates(4f,4f), 3);
        Intersection nodeD = new Intersection(4, new GeoCoordinates(5f,5f), 4);
        Intersection end = new Intersection(5, new GeoCoordinates(6f,6f), 5);
        Segment s_to_a = new Segment(start, nodeA, "", 10);
        Segment s_to_c = new Segment(start, nodeC, "", 20);
        Segment a_to_b = new Segment(nodeA, nodeB, "", 5);
        Segment b_to_d = new Segment(nodeB, nodeD, "", 20);
        Segment c_to_d = new Segment(nodeC, nodeD, "", 10);
        Segment d_to_e = new Segment(nodeD, end, "", 10);
        start.addOutwardSegment(s_to_a);
        start.addOutwardSegment(s_to_c);
        nodeA.addOutwardSegment(a_to_b);
        nodeB.addOutwardSegment(b_to_d);
        nodeC.addOutwardSegment(c_to_d);
        nodeD.addOutwardSegment(d_to_e);
        this.map = new CityMap();
        this.map.addIntersection(start);
        this.map.addIntersection(nodeA);
        this.map.addIntersection(nodeB);
        this.map.addIntersection(nodeC);
        this.map.addIntersection(nodeD);
        this.map.addIntersection(end);

        this.start = start;
        this.end = end;
    }

    @Test
    void testSimplePathDijkstra() {
        Intersection start = new Intersection(0, new GeoCoordinates(1f,1f), 0);
        Intersection end = new Intersection(1, new GeoCoordinates(2f,2f), 1);
        Segment segment = new Segment(start, end, "", 10);
        start.addOutwardSegment(segment);
        CityMap map = new CityMap();
        map.addIntersection(start);
        map.addIntersection(end);

        Dijkstra dijkstra = new Dijkstra();
        Path path = dijkstra.shortestPath(map, start, end);

        assertEquals(10f, path.getLength());
        assertEquals(1, path.getSegments().size());
        assertEquals(0, path.getSegments().get(0).getOrigin().getId());
        assertEquals(1, path.getSegments().get(0).getDestination().getId());
    }
    @Test
    void testNoPathDijkstra() {
        Intersection start = new Intersection(0, new GeoCoordinates(1f,1f), 0);
        Intersection end = new Intersection(1, new GeoCoordinates(2f,2f), 1);
        Segment segment = new Segment(start, end, "", 10);
        CityMap map = new CityMap();
        map.addIntersection(start);
        map.addIntersection(end);

        Dijkstra dijkstra = new Dijkstra();
        Path path = dijkstra.shortestPath(map, start, end);

        assertEquals(0f, path.getLength());
        assertEquals(0, path.getSegments().size());
    }
    @Test
    void testDoublePathDijkstra() {
        Intersection start = new Intersection(0, new GeoCoordinates(1f,1f), 0);
        Intersection middle = new Intersection(1, new GeoCoordinates(2f,2f), 1);
        Intersection end = new Intersection(2, new GeoCoordinates(3f,3f), 2);
        Segment firstSegment = new Segment(start, middle, "", 10);
        Segment secondSegment = new Segment(middle, end, "", 10);
        start.addOutwardSegment(firstSegment);
        middle.addOutwardSegment(secondSegment);
        CityMap map = new CityMap();
        map.addIntersection(start);
        map.addIntersection(middle);
        map.addIntersection(end);

        Dijkstra dijkstra = new Dijkstra();
        Path path = dijkstra.shortestPath(map, start, end);

        assertEquals(20f, path.getLength());
        assertEquals(2, path.getSegments().size());
        assertEquals(0, path.getSegments().get(0).getOrigin().getId());
        assertEquals(1, path.getSegments().get(0).getDestination().getId());
        assertEquals(1, path.getSegments().get(1).getOrigin().getId());
        assertEquals(2, path.getSegments().get(1).getDestination().getId());
    }
    @Test
    void testGraphDijkstra() {
        Dijkstra dijkstra = new Dijkstra();
        Path path = dijkstra.shortestPath(this.map, this.start, this.end);

        assertEquals(40f, path.getLength());
        assertEquals(3, path.getSegments().size());
    }

    @Test
    void testSimplePathAStar() {
        Intersection start = new Intersection(0, new GeoCoordinates(1f,1f), 0);
        Intersection end = new Intersection(1, new GeoCoordinates(2f,2f), 1);
        Segment segment = new Segment(start, end, "", 10);
        start.addOutwardSegment(segment);
        CityMap map = new CityMap();
        map.addIntersection(start);
        map.addIntersection(end);

        AStar aStar = new AStar();
        Path path = aStar.shortestPath(map, start, end);

        assertEquals(10f, path.getLength());
        assertEquals(1, path.getSegments().size());
        assertEquals(0, path.getSegments().get(0).getOrigin().getId());
        assertEquals(1, path.getSegments().get(0).getDestination().getId());
    }
    @Test
    void testNoPathAStar() {
        Intersection start = new Intersection(0, new GeoCoordinates(1f,1f), 0);
        Intersection end = new Intersection(1, new GeoCoordinates(2f,2f), 1);
        Segment segment = new Segment(start, end, "", 10);
        CityMap map = new CityMap();
        map.addIntersection(start);
        map.addIntersection(end);

        AStar aStar = new AStar();
        Path path = aStar.shortestPath(map, start, end);

        assertEquals(0f, path.getLength());
        assertEquals(0, path.getSegments().size());
    }
    @Test
    void testDoublePathAStar() {
        Intersection start = new Intersection(0, new GeoCoordinates(1f,1f), 0);
        Intersection middle = new Intersection(1, new GeoCoordinates(2f,2f), 1);
        Intersection end = new Intersection(2, new GeoCoordinates(3f,3f), 2);
        Segment firstSegment = new Segment(start, middle, "", 10);
        Segment secondSegment = new Segment(middle, end, "", 10);
        start.addOutwardSegment(firstSegment);
        middle.addOutwardSegment(secondSegment);
        CityMap map = new CityMap();
        map.addIntersection(start);
        map.addIntersection(middle);
        map.addIntersection(end);

        AStar aStar = new AStar();
        Path path = aStar.shortestPath(map, start, end);

        assertEquals(20f, path.getLength());
        assertEquals(2, path.getSegments().size());
        assertEquals(0, path.getSegments().get(0).getOrigin().getId());
        assertEquals(1, path.getSegments().get(0).getDestination().getId());
        assertEquals(1, path.getSegments().get(1).getOrigin().getId());
        assertEquals(2, path.getSegments().get(1).getDestination().getId());
    }
    @Test
    void testGraphAStar() {
        AStar aStar = new AStar();
        Path path = aStar.shortestPath(this.map, this.start, this.end);

        assertEquals(40f, path.getLength());
        assertEquals(3, path.getSegments().size());
    }

}
