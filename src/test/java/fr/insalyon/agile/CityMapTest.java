package fr.insalyon.agile;

import fr.insalyon.agile.mock.MockCityMap;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class CityMapTest {

    private static CityMap cityMap;
    private static Intersection intersection;
    private static Intersection intersection1;
    private static Intersection intersection2;
    private static Intersection intersection3;
    @BeforeAll
    static void createCityMap() {
        intersection = new Intersection(0, new GeoCoordinates(0f, 0f), 0);
        intersection1 = new Intersection(1, new GeoCoordinates(0f, 1f), 1);
        intersection2 = new Intersection(2, new GeoCoordinates(1f, 1f), 2);
        intersection3 = new Intersection(3, new GeoCoordinates(1f, 0f), 3);

        cityMap = new MockCityMap();
        cityMap.setWarehouse(intersection1);
        cityMap.addIntersection(intersection);
        cityMap.addIntersection(intersection1);
        cityMap.addIntersection(intersection2);
        cityMap.addIntersection(intersection3);
    }
    @Test
    public void testGetNorthWestMostCoordinates(){
        GeoCoordinates expected = new GeoCoordinates(1f, 0f);
        assertEquals(expected, cityMap.getNorthWestMostCoordinates());
    }
    @Test
    public void testGetSouthEastMostCoordinates(){
        GeoCoordinates expected = new GeoCoordinates(0f, 1f);
        assertEquals(expected, cityMap.getSouthEastMostCoordinates());
    }

    @Test
    public void getMaxLatitude(){
        float expected = 1f;
        assertEquals(expected, cityMap.getMaxLatitude());
    }

    @Test
    public void getMinLatitude(){
        float expected = 0f;
        assertEquals(expected, cityMap.getMinLatitude());
    }

    @Test
    public void getMaxLongitude(){
        float expected = 1f;
        assertEquals(expected, cityMap.getMaxLongitude());
    }

    @Test
    public void getMinLongitude(){
        float expected = 0f;
        assertEquals(expected, cityMap.getMinLongitude());
    }
    @Test
    public void testToString(){
        String expected = "CityMap{" +
                "warehouse=" + intersection1 +
                ", intersections=" + cityMap.getIntersections() +
                '}';
        assertEquals(expected, cityMap.toString());
    }
}
