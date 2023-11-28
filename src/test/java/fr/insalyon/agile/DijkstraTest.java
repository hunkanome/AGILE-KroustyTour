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
import java.util.List;

/**
 *
 */
class DijkstraTest extends TestCase {

    private ArrayList<Intersection> listIntersections = new ArrayList<>();
    private ArrayList<Segment> listSegments = new ArrayList<>();
    private CityMap map;
    private ArrayList<Intersection> listDeliveries = new ArrayList<>();

    @Test
    void testCityMapCreation() {
        setUpCityMap();


        CityMapMatrix cityMapMatrix = new CityMapMatrix(map, listDeliveries);

    }

    protected void setUpListIntersections() {
        Intersection s = new Intersection(1L, 1.0f, 1.0f, 0); this.listIntersections.add(s);
        Intersection e = new Intersection(2L, 1.0f, 1.0f, 1); this.listIntersections.add(e);
        Intersection a = new Intersection(3L, 1.0f, 1.0f, 2); this.listIntersections.add(a);
        Intersection b = new Intersection(4L, 1.0f, 1.0f, 3); this.listIntersections.add(b);
        Intersection c = new Intersection(5L, 1.0f, 1.0f, 4); this.listIntersections.add(c);
        Intersection d = new Intersection(6L, 1.0f, 1.0f, 5); this.listIntersections.add(d);
    }
    protected void setUpListSegments() {
        /*
        Segment s1 = new Segment(a, c, "", 1.0f); this.listSegments.add(s1);
        Segment s3 = new Segment(a, s, "", 3.0f); this.listSegments.add(s3);
        Segment s4 = new Segment(b, d, "", 4.0f); this.listSegments.add(s4);
        Segment s5_bis = new Segment(b, s, "", 5.0f); this.listSegments.add(s5_bis);
        Segment s1_bis = new Segment(c, a, "", 1.0f); this.listSegments.add(s1_bis);
        Segment s2 = new Segment(c, d, "", 2.0f); this.listSegments.add(s2);
        Segment s9_bis = new Segment(c, s, "", 9.0f); this.listSegments.add(s9_bis);
        Segment s2_bis = new Segment(d, c, "", 2.0f); this.listSegments.add(s2_bis);
        Segment s4_bis = new Segment(d, b, "", 4.0f); this.listSegments.add(s4_bis);
        Segment s10 = new Segment(d, e, "", 10.0f); this.listSegments.add(s10);
        Segment s10_bis = new Segment(e, d, "", 10.0f); this.listSegments.add(s10_bis);
        Segment s3_bis = new Segment(s, a, "", 3.0f); this.listSegments.add(s3_bis);
        Segment s5 = new Segment(s, b, "", 5.0f); this.listSegments.add(s5);
        Segment s9 = new Segment(s, c, "", 9.0f); this.listSegments.add(s9);
        */
    }
    protected void setUpCityMap() {
        /*
        HashSet<Segment> setA = new HashSet<>(); setA.add(s1); setA.add(s3);
        HashSet<Segment> setB = new HashSet<>(); setB.add(s4); setB.add(s5_bis);
        HashSet<Segment> setC = new HashSet<>(); setC.add(s1_bis); setC.add(s2); setC.add(s9_bis);
        HashSet<Segment> setD = new HashSet<>(); setD.add(s2_bis); setD.add(s4_bis); setD.add(s10);
        HashSet<Segment> setE = new HashSet<>(); setE.add(s10_bis);
        HashSet<Segment> setS = new HashSet<>(); setS.add(s3_bis); setS.add(s5); setS.add(s9);

        a.setOutwardSegments(setA); b.setOutwardSegments(setB); c.setOutwardSegments(setC);
        d.setOutwardSegments(setD); e.setOutwardSegments(setE); s.setOutwardSegments(setS);

        ArrayList<Intersection> i = new ArrayList<>();
        i.add(s); i.add(e); i.add(a); i.add(b); i.add(c); i.add(d);

        CityMap map = new CityMap();
        map.setIntersections(i);

        this.map = map;
         */
    }
    protected void setUpListDeliveries() {
        this.listDeliveries.add(this.listIntersections.get(0));
        this.listDeliveries.add(this.listIntersections.get(1));
    }
}
