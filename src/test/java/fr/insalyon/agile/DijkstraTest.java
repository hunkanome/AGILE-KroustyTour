package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.*;

import fr.insalyon.algorithm.CityMapMatrix;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Segment;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 */
class DijkstraTest extends TestCase {

    private ArrayList<Intersection> listIntersections = new ArrayList<>();
    private CityMap map = new CityMap();
    private ArrayList<Intersection> listDeliveries = new ArrayList<>();
    private CityMapMatrix mapMatrix;

    @Test
    void testCityMapCreation() {
        setUpGraph();
        setUpCityMap();

        assertEquals(this.map.toString(), "CityMap{warehouse=null, intersections=[Intersection{id=1, latitude=1.0, longitude=1.0, outwardSegments=[Segment{originID=1, destinationID=5, name='', length=9.0}, Segment{originID=1, destinationID=3, name='', length=3.0}, Segment{originID=1, destinationID=4, name='', length=5.0}], index=0}, Intersection{id=2, latitude=1.0, longitude=1.0, outwardSegments=[Segment{originID=2, destinationID=6, name='', length=10.0}], index=1}, Intersection{id=3, latitude=1.0, longitude=1.0, outwardSegments=[Segment{originID=3, destinationID=5, name='', length=1.0}, Segment{originID=3, destinationID=1, name='', length=3.0}], index=2}, Intersection{id=4, latitude=1.0, longitude=1.0, outwardSegments=[Segment{originID=4, destinationID=6, name='', length=4.0}, Segment{originID=4, destinationID=1, name='', length=5.0}], index=3}, Intersection{id=5, latitude=1.0, longitude=1.0, outwardSegments=[Segment{originID=5, destinationID=3, name='', length=1.0}, Segment{originID=5, destinationID=1, name='', length=9.0}, Segment{originID=5, destinationID=6, name='', length=2.0}], index=4}, Intersection{id=6, latitude=1.0, longitude=1.0, outwardSegments=[Segment{originID=6, destinationID=2, name='', length=10.0}, Segment{originID=6, destinationID=4, name='', length=4.0}, Segment{originID=6, destinationID=5, name='', length=2.0}], index=5}]}");
    }

    @Test
    void testCityMapMatrixCreation() {
        setUpGraph();
        setUpCityMap();
        int nbDeliveries = 2;
        setUpListDeliveries(nbDeliveries);

        this.mapMatrix = new CityMapMatrix(this.map, this.listDeliveries);

        for (int i = 0; i < this.mapMatrix.getArrayPaths().length; i++) {
            for (int j = 0; j < this.mapMatrix.getArrayPaths().length; j++) {
                if (i != j) {
                    assertEquals(this.mapMatrix.getArrayPaths()[i][j].getLength(), 16.0f);
                } else {
                    assertEquals(this.mapMatrix.getArrayPaths()[i][j].getLength(), 0.0f);
                }
            }
        }
    }

    protected void setUpGraph() {
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
    protected void setUpCityMap() {
        this.map.setIntersections(this.listIntersections);
    }
    protected void setUpListDeliveries(int nbDeliveries) {
        for (int i = 0; i < nbDeliveries; i++) {
            this.listDeliveries.add(this.listIntersections.get(i));
        }
    }
}
