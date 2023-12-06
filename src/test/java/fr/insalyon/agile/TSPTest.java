package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.*;

import fr.insalyon.algorithm.DeliveryGraph;
import fr.insalyon.algorithm.TSP;
import fr.insalyon.algorithm.TSP1;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MockPath extends Path {
    void setLength(float length) {
        this.length = length;
    }
}

class TSPTest {
    private static MockPath[][] costMatrix;

    @BeforeAll
    public static void setCostMatrix(){
        // Create a test graph
        costMatrix = new MockPath[4][4];
        for (int i = 0; i < costMatrix.length; i++) {
            for (int j = 0; j < costMatrix.length; j++) {
                costMatrix[i][j] = new MockPath();
            }
        }

        costMatrix[0][0].setLength(0);
        costMatrix[0][1].setLength(100);
        costMatrix[0][2].setLength(900);
        costMatrix[0][3].setLength(900);
        costMatrix[1][0].setLength(900);
        costMatrix[1][1].setLength(0);
        costMatrix[1][2].setLength(100);
        costMatrix[1][3].setLength(900);
        costMatrix[2][0].setLength(900);
        costMatrix[2][1].setLength(900);
        costMatrix[2][2].setLength(0);
        costMatrix[2][3].setLength(100);
        costMatrix[3][0].setLength(100);
        costMatrix[3][1].setLength(900);
        costMatrix[3][2].setLength(900);
        costMatrix[3][3].setLength(0);
    }

    @Test
    void testTSP0Elements() {
        TSP tsp = new TSP1();

        // Create a test graph
        Path[][] costMatrix = new Path[0][0];
        Delivery[] deliveries = new Delivery[0];
        DeliveryGraph g = new DeliveryGraph(costMatrix, deliveries);

        // Search for a solution
        tsp.searchSolution(10000, g);

        // Print the solution
        assertEquals(-1, tsp.getSolution(0));
    }

    @Test
    void testTSP1Element() {
        TSP tsp = new TSP1();

        // Create a test graph
        MockPath[][] costMatrix = new MockPath[1][1];
        costMatrix[0][0] = new MockPath();
        costMatrix[0][0].setLength(0);
        Delivery[] deliveries = new Delivery[1];
        GeoCoordinates coordinates = new GeoCoordinates(0.f, 0.f);
        deliveries[0] = new Delivery(new Courier(),
                new Intersection(0L, coordinates, 0),
                new TimeWindow(8));
        DeliveryGraph g = new DeliveryGraph(costMatrix, deliveries);

        // Search for a solution
        tsp.searchSolution(10000, g);

        // Print the solution
        assertEquals(0, tsp.getSolution(0));
    }



    @Test
    void testTSPSameTimeWindow() {
        TSP tsp = new TSP1();

        Courier courier = new Courier();
        GeoCoordinates coordinates = new GeoCoordinates(0.f, 0.f);
        Intersection intersection1 = new Intersection(0L, coordinates, 0);
        Intersection intersection2 = new Intersection(1L, coordinates, 1);
        Intersection intersection3 = new Intersection(2L, coordinates, 2);
        Intersection intersection4 = new Intersection(3L, coordinates, 3);

        TimeWindow tw8 = new TimeWindow(8);

        Delivery[] deliveries = new Delivery[4];
        deliveries[0] = new Delivery(courier, intersection1, tw8);
        deliveries[1] = new Delivery(courier, intersection2, tw8);
        deliveries[2] = new Delivery(courier, intersection3, tw8);
        deliveries[3] = new Delivery(courier, intersection4, tw8);

        DeliveryGraph g = new DeliveryGraph(costMatrix, deliveries);

        tsp.searchSolution(10000, g);

        // Test the solution
        for (int i = 0; i < costMatrix.length; i++) {
            // The graph is constructed so that the solution is i (0 1 2 3)
            assertEquals(i, tsp.getSolution(i));
        }
    }

    @Test
    void testTSPDifferentTimeWindows() {
        TSP tsp = new TSP1();

        Courier courier = new Courier();
        GeoCoordinates coordinates = new GeoCoordinates(0.f, 0.f);
        Intersection intersection1 = new Intersection(0L, coordinates, 0);
        Intersection intersection2 = new Intersection(1L, coordinates, 1);
        Intersection intersection3 = new Intersection(2L, coordinates, 2);
        Intersection intersection4 = new Intersection(3L, coordinates, 3);

        TimeWindow tw8 = new TimeWindow(8);
        TimeWindow tw9 = new TimeWindow(9);
        TimeWindow tw10 = new TimeWindow(10);


        Delivery[] deliveries = new Delivery[4];
        deliveries[0] = new Delivery(courier, intersection1, tw8);
        deliveries[1] = new Delivery(courier, intersection2, tw9);
        deliveries[2] = new Delivery(courier, intersection3, tw10);
        deliveries[3] = new Delivery(courier, intersection4, tw10);

        DeliveryGraph g = new DeliveryGraph(costMatrix, deliveries);

        tsp.searchSolution(10000, g);

        // Test the solution
        for (int i = 0; i < costMatrix.length; i++) {
            // The graph is constructed so that the solution is i (0 1 2 3)
            assertEquals(i, tsp.getSolution(i));
        }
    }

    @Test
    void testTSPNonContiguousTimeWindows() {
        TSP tsp = new TSP1();

        Courier courier = new Courier();
        GeoCoordinates coordinates = new GeoCoordinates(0.f, 0.f);
        Intersection intersection1 = new Intersection(0L, coordinates, 0);
        Intersection intersection2 = new Intersection(1L, coordinates, 1);
        Intersection intersection3 = new Intersection(2L, coordinates, 2);
        Intersection intersection4 = new Intersection(3L, coordinates, 3);

        TimeWindow tw8 = new TimeWindow(8);
        TimeWindow tw10 = new TimeWindow(10);

        Delivery[] deliveries = new Delivery[4];
        deliveries[0] = new Delivery(courier, intersection1, tw8);
        deliveries[1] = new Delivery(courier, intersection2, tw8);
        deliveries[2] = new Delivery(courier, intersection3, tw10);
        deliveries[3] = new Delivery(courier, intersection4, tw10);

        DeliveryGraph g = new DeliveryGraph(costMatrix, deliveries);

        tsp.searchSolution(10000, g);

        // Test the solution
        for (int i = 0; i < costMatrix.length; i++) {
            // The graph is constructed so that the solution is i (0 1 2 3)
            assertEquals(i, tsp.getSolution(i));
        }
    }
}