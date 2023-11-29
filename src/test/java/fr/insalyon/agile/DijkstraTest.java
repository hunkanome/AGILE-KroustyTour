package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Test;

import fr.insalyon.algorithm.CityMapMatrix;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Segment;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DijkstraTest extends TestCase {

    private ArrayList<Intersection> listIntersections = new ArrayList<>();
    private CityMap map = new CityMap();
    private ArrayList<Intersection> listDeliveries = new ArrayList<>();
    private CityMapMatrix mapMatrix;

    @Test
    public void testCityMapCreation() {
        Assertions.assertNull(this.map.getWarehouse());
        for (int i = 0; i < 5; i++) {
            long id = this.map.getIntersections().get(i).getId();
            Assertions.assertEquals(id, i+1);
        }
    }

    @Test
    public void testCityMapMatrixCreation() {
        int nbDeliveries = 2;
        setUpListDeliveries(nbDeliveries);
        this.mapMatrix = new CityMapMatrix(this.map, this.listDeliveries);

        for (int i = 0; i < this.mapMatrix.getArrayPaths().length; i++) {
            for (int j = 0; j < this.mapMatrix.getArrayPaths().length; j++) {
                if (i != j) {
                    Assertions.assertEquals(this.mapMatrix.getArrayPaths()[i][j].getLength(), 16.0f);
                } else {
                    Assertions.assertEquals(this.mapMatrix.getArrayPaths()[i][j].getLength(), 0.0f);
                }
            }
        }
    }

    @Test
    public void testAddIntersection() {
        Intersection i = new Intersection(99L, 1.0f, 1.0f, map.getIntersections().size());

        Segment i_to_d = new Segment(i, map.getIntersections().get(3), "", 3f);
        Segment i_to_e = new Segment(i, map.getIntersections().get(4), "", 4f);

        Segment d_to_i = new Segment(map.getIntersections().get(3), i, "", 3f);
        Segment e_to_i = new Segment(map.getIntersections().get(4), i, "", 4f);

        i.addOutwardSegment(i_to_d);
        i.addOutwardSegment(i_to_e);

        map.getIntersections().get(3).addOutwardSegment(d_to_i);
        map.getIntersections().get(4).addOutwardSegment(e_to_i);

        this.map.addIntersection(i);
        this.listIntersections.add(i);

        CityMapMatrix matrix = new CityMapMatrix(this.map, this.listIntersections);
        matrix.addIntersection(i);

        Assertions.assertEquals(matrix.getArrayPaths()[3][4].getLength(), 7f);
    }

    @BeforeAll
    public void setUpGraph() {
        System.out.println("setUpGraph");
        Intersection s = new Intersection(1L, 1.0f, 1.0f, 0);
        Intersection e = new Intersection(2L, 1.0f, 1.0f, 1);
        Intersection a = new Intersection(3L, 1.0f, 1.0f, 2);
        Intersection b = new Intersection(4L, 1.0f, 1.0f, 3);
        Intersection c = new Intersection(5L, 1.0f, 1.0f, 4);
        Intersection d = new Intersection(6L, 1.0f, 1.0f, 5);

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

        this.listIntersections.add(s); this.listIntersections.add(e); this.listIntersections.add(a);
        this.listIntersections.add(b); this.listIntersections.add(c); this.listIntersections.add(d);
    }
    @BeforeAll
    public void setUpCityMap() {
        System.out.println("setUpCityMap");
        this.map.setIntersections(this.listIntersections);
    }
    protected void setUpListDeliveries(int nbDeliveries) {
        for (int i = 0; i < nbDeliveries; i++) {
            this.listDeliveries.add(this.listIntersections.get(i));
        }
    }
}
