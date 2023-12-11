package fr.insalyon.agile;

import fr.insalyon.algorithm.DeliveryGraph;
import fr.insalyon.algorithm.TSP;
import fr.insalyon.algorithm.TSP1;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        costMatrix[0][2].setLength(Float.MAX_VALUE);
        costMatrix[0][3].setLength(Float.MAX_VALUE);
        costMatrix[1][0].setLength(Float.MAX_VALUE);
        costMatrix[1][1].setLength(0);
        costMatrix[1][2].setLength(100);
        costMatrix[1][3].setLength(Float.MAX_VALUE);
        costMatrix[2][0].setLength(Float.MAX_VALUE);
        costMatrix[2][1].setLength(Float.MAX_VALUE);
        costMatrix[2][2].setLength(0);
        costMatrix[2][3].setLength(100);
        costMatrix[3][0].setLength(100);
        costMatrix[3][1].setLength(Float.MAX_VALUE);
        costMatrix[3][2].setLength(Float.MAX_VALUE);
        costMatrix[3][3].setLength(0);
    }

    @Test
    void testTSP0Elements() {
        TSP tsp = new TSP1();

        // Create a test graph
        DeliveryGraph g = new DeliveryGraph();
        Map<TimeWindow, List<Integer>> deliveriesByTimeWindow = new HashMap<>();

        // Search for a solution
        tsp.searchSolution(10000, g, deliveriesByTimeWindow);

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
        List<Delivery> deliveries = new ArrayList<>(1);
        GeoCoordinates coordinates = new GeoCoordinates(0.f, 0.f);
        deliveries.add(
            new Delivery(
                new Courier(),
                new Intersection(0L, coordinates, 0),
                TimeWindow.getTimeWindow(8)));
        Map<TimeWindow, List<Integer>> deliveriesByTimeWindow = new HashMap<>();
        for (Delivery d : deliveries) {
            deliveriesByTimeWindow
                    .computeIfAbsent(d.getTimeWindow(), k -> new ArrayList<>())
                    .add(deliveries.indexOf(d));
        }
        DeliveryGraph graph = new DeliveryGraph(costMatrix);

        // Search for a solution
        tsp.searchSolution(10000, graph, deliveriesByTimeWindow );

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

        TimeWindow tw8 = TimeWindow.getTimeWindow(8);

        List<Delivery> deliveries = new ArrayList<>(4);
        deliveries.add(new Delivery(courier, intersection1, tw8));
        deliveries.add(new Delivery(courier, intersection2, tw8));
        deliveries.add(new Delivery(courier, intersection3, tw8));
        deliveries.add(new Delivery(courier, intersection4, tw8));

        DeliveryGraph graph = new DeliveryGraph(costMatrix);

        Map<TimeWindow, List<Integer>> deliveriesByTimeWindow = new HashMap<>();
        for (Delivery d : deliveries) {
            deliveriesByTimeWindow
                    .computeIfAbsent(d.getTimeWindow(), k -> new ArrayList<>())
                    .add(deliveries.indexOf(d));
        }

        tsp.searchSolution(10000, graph, deliveriesByTimeWindow);

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

        TimeWindow tw8 = TimeWindow.getTimeWindow(8);
        TimeWindow tw9 = TimeWindow.getTimeWindow(9);
        TimeWindow tw10 = TimeWindow.getTimeWindow(10);


        List<Delivery> deliveries = new ArrayList<>(4);
        deliveries.add(new Delivery(courier, intersection1, tw8));
        deliveries.add(new Delivery(courier, intersection2, tw9));
        deliveries.add(new Delivery(courier, intersection3, tw10));
        deliveries.add(new Delivery(courier, intersection4, tw10));

        DeliveryGraph g = new DeliveryGraph(costMatrix);

        Map<TimeWindow, List<Integer>> deliveriesByTimeWindow = new HashMap<>();
        for (Delivery d : deliveries) {
            deliveriesByTimeWindow
                    .computeIfAbsent(d.getTimeWindow(), k -> new ArrayList<>())
                    .add(deliveries.indexOf(d));
        }

        tsp.searchSolution(10000, g, deliveriesByTimeWindow);

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

        TimeWindow tw8 = TimeWindow.getTimeWindow(8);
        TimeWindow tw10 = TimeWindow.getTimeWindow(10);

        List<Delivery> deliveries = new ArrayList<>(4);
        deliveries.add(new Delivery(courier, intersection1, tw8));
        deliveries.add(new Delivery(courier, intersection2, tw8));
        deliveries.add(new Delivery(courier, intersection3, tw10));
        deliveries.add(new Delivery(courier, intersection4, tw10));

        DeliveryGraph g = new DeliveryGraph(costMatrix);

        Map<TimeWindow, List<Integer>> deliveriesByTimeWindow = new HashMap<>();
        for (Delivery d : deliveries) {
            deliveriesByTimeWindow
                    .computeIfAbsent(d.getTimeWindow(), k -> new ArrayList<>())
                    .add(deliveries.indexOf(d));
        }

        tsp.searchSolution(10000, g, deliveriesByTimeWindow);

        // Test the solution
        for (int i = 0; i < costMatrix.length; i++) {
            // The graph is constructed so that the solution is i (0 1 2 3)
            assertEquals(i, tsp.getSolution(i));
        }
    }

    @Test
    void testTSPNoDeliveryInWarehouseTimeWindow() {
        TSP tsp = new TSP1();

        Courier courier = new Courier();
        GeoCoordinates coordinates = new GeoCoordinates(0.f, 0.f);
        Intersection intersection1 = new Intersection(0L, coordinates, 0);
        Intersection intersection2 = new Intersection(1L, coordinates, 1);

        TimeWindow tw8 = TimeWindow.getTimeWindow(8);
        TimeWindow tw10 = TimeWindow.getTimeWindow(10);

        List<Delivery> deliveries = new ArrayList<>(4);
        deliveries.add(new Delivery(courier, intersection1, tw8));
        deliveries.add(new Delivery(courier, intersection2, tw10));

        MockPath[][] otherCostMatrix = new MockPath[2][2];
        for (int i = 0; i < otherCostMatrix.length; i++) {
            for (int j = 0; j < otherCostMatrix.length; j++) {
                otherCostMatrix[i][j] = new MockPath();
            }
        }

        otherCostMatrix[0][0].setLength(0);
        otherCostMatrix[0][1].setLength(100);
        otherCostMatrix[1][0].setLength(Float.MAX_VALUE);
        otherCostMatrix[1][1].setLength(0);

        DeliveryGraph g = new DeliveryGraph(otherCostMatrix);

        Map<TimeWindow, List<Integer>> deliveriesByTimeWindow = new HashMap<>();
        for (Delivery d : deliveries) {
            deliveriesByTimeWindow
                    .computeIfAbsent(d.getTimeWindow(), k -> new ArrayList<>())
                    .add(deliveries.indexOf(d));
        }

        tsp.searchSolution(10000, g, deliveriesByTimeWindow);

        // Test the solution
        for (int i = 0; i < otherCostMatrix.length; i++) {
            // The graph is constructed so that the solution is i (0 1 2 3)
            assertEquals(i, tsp.getSolution(i));
        }
    }
}