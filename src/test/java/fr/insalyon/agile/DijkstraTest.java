package fr.insalyon.agile;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.insalyon.algorithm.CityMapMatrix;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Segment;

import java.util.ArrayList;
import java.util.HashSet;

class DijkstraTest {

    static private final ArrayList<Intersection> listIntersections = new ArrayList<>();
    static private final CityMap map = new CityMap();
    static private final ArrayList<Intersection> listDeliveries = new ArrayList<>();
    static private CityMapMatrix mapMatrix;

    @Test
    void testCityMapCreation() {
        Assertions.assertNull(DijkstraTest.map.getWarehouse());
        for (int i = 0; i < 5; i++) {
            long id = DijkstraTest.map.getIntersections().get(i).getId();
            Assertions.assertEquals(id, i);
        }
    }

    /*
    @Test
    void testCityMapMatrixCreation() {
        int nbDeliveries = 2;
        setUpListDeliveries(nbDeliveries);
        DijkstraTest.mapMatrix = new CityMapMatrix(DijkstraTest.map, DijkstraTest.listDeliveries);

        for (int i = 0; i < DijkstraTest.mapMatrix.getGraph().getCost().length; i++) {
            for (int j = 0; j < DijkstraTest.mapMatrix.getGraph().getCost().length; j++) {
                if (i != j) {
                    Assertions.assertEquals(16.0f, DijkstraTest.mapMatrix.getGraph().getCost()[i][j].getLength());
                } else {
                    Assertions.assertEquals( 0.0f, DijkstraTest.mapMatrix.getGraph().getCost()[i][j].getLength());
                }
            }
        }
    }

    @Test
    void testAddIntersection() {
        GeoCoordinates coords = new GeoCoordinates(1f, 1f);
        Intersection i = new Intersection(7357L, coords, DijkstraTest.map.getIntersections().size());

        Segment i_to_d = new Segment(i, DijkstraTest.map.getIntersections().get(5), "", 3f);
        Segment i_to_e = new Segment(i, DijkstraTest.map.getIntersections().get(1), "", 4f);

        Segment d_to_i = new Segment(DijkstraTest.map.getIntersections().get(5), i, "", 3f);
        Segment e_to_i = new Segment(DijkstraTest.map.getIntersections().get(1), i, "", 4f);

        i.addOutwardSegment(i_to_d);
        i.addOutwardSegment(i_to_e);

        DijkstraTest.map.getIntersections().get(5).addOutwardSegment(d_to_i);
        DijkstraTest.map.getIntersections().get(1).addOutwardSegment(e_to_i);

        DijkstraTest.map.addIntersection(i);

        CityMapMatrix matrix = new CityMapMatrix(DijkstraTest.map, DijkstraTest.listIntersections);
        matrix.addIntersection(i);

        // path same as before
        Assertions.assertEquals(3f, matrix.getArrayPaths()[2][5].getLength());

        // path ending at new node
        Assertions.assertEquals(3f, matrix.getArrayPaths()[5][6].getLength());

        // path starting from new node
        Assertions.assertEquals(9f, matrix.getArrayPaths()[6][0].getLength());

        // path passing through new node
        Assertions.assertEquals(7f, matrix.getArrayPaths()[1][5].getLength());
    }
     */

    @BeforeAll
    public static void setUpGraph() {
        System.out.println("setUpGraph");
        GeoCoordinates coords = new GeoCoordinates(1f, 1f);
        Intersection s = new Intersection(0L, coords, 0);
        Intersection e = new Intersection(1L, coords, 1);
        Intersection a = new Intersection(2L, coords, 2);
        Intersection b = new Intersection(3L, coords, 3);
        Intersection c = new Intersection(4L, coords, 4);
        Intersection d = new Intersection(5L, coords, 5);

        Segment s1 = new Segment(a, c, "", 1.0f);
        Segment s3 = new Segment(a, s, "", 3.0f);
        Segment s4 = new Segment(b, d, "", 4.0f);
        Segment s5_bis = new Segment(b, s, "", 5.0f);
        Segment s1_bis = new Segment(c, a, "", 1.0f);
        Segment s2 = new Segment(c, d, "", 2.0f);
        Segment s9_bis = new Segment(c, s, "", 9.0f);
        Segment s2_bis = new Segment(d, c, "", 2.0f);
        Segment s4_bis = new Segment(d, b, "", 4.0f);
        Segment s10 = new Segment(d, e, "", 10.0f);
        Segment s10_bis = new Segment(e, d, "", 10.0f);
        Segment s3_bis = new Segment(s, a, "", 3.0f);
        Segment s5 = new Segment(s, b, "", 5.0f);
        Segment s9 = new Segment(s, c, "", 9.0f);

        HashSet<Segment> setA = new HashSet<>(); setA.add(s1); setA.add(s3);
        HashSet<Segment> setB = new HashSet<>(); setB.add(s4); setB.add(s5_bis);
        HashSet<Segment> setC = new HashSet<>(); setC.add(s1_bis); setC.add(s2); setC.add(s9_bis);
        HashSet<Segment> setD = new HashSet<>(); setD.add(s2_bis); setD.add(s4_bis); setD.add(s10);
        HashSet<Segment> setE = new HashSet<>(); setE.add(s10_bis);
        HashSet<Segment> setS = new HashSet<>(); setS.add(s3_bis); setS.add(s5); setS.add(s9);

        a.setOutwardSegments(setA); b.setOutwardSegments(setB); c.setOutwardSegments(setC);
        d.setOutwardSegments(setD); e.setOutwardSegments(setE); s.setOutwardSegments(setS);

        DijkstraTest.listIntersections.add(s); DijkstraTest.listIntersections.add(e); DijkstraTest.listIntersections.add(a);
        DijkstraTest.listIntersections.add(b); DijkstraTest.listIntersections.add(c); DijkstraTest.listIntersections.add(d);
    }
    @BeforeAll
    public static void setUpCityMap() {
        DijkstraTest.map.setIntersections((ArrayList<Intersection>) DijkstraTest.listIntersections.clone());
    }
    protected void setUpListDeliveries(int nbDeliveries) {
        for (int i = 0; i < nbDeliveries; i++) {
            DijkstraTest.listDeliveries.add(DijkstraTest.listIntersections.get(i));
        }
    }

}
