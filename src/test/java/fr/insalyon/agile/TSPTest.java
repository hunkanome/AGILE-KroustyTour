package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.*;

import fr.insalyon.algorithm.DeliveryGraph;
import fr.insalyon.algorithm.Graph;
import fr.insalyon.algorithm.TSP;
import fr.insalyon.algorithm.TSP1;
import fr.insalyon.model.Path;
import org.junit.jupiter.api.Test;

class TSPTest {

    @Test
    void testTSP0Elements() {
        TSP tsp = new TSP1();

        // Create a test graph
        Path[][] costMatrix = new Path[0][0];
        DeliveryGraph g = new DeliveryGraph(costMatrix);

        // Search for a solution
        tsp.searchSolution(10000, g);

        // Print the solution
        assertEquals(-1, tsp.getSolution(0));
    }

    @Test
    void testTSP1Element() {
        TSP tsp = new TSP1();

        // Create a test graph
        Path[][] costMatrix = new Path[1][1];
        costMatrix[0][0] = new Path();
        costMatrix[0][0].setLength(0);
        DeliveryGraph g = new DeliveryGraph(costMatrix);

        // Search for a solution
        tsp.searchSolution(10000, g);

        // Print the solution
        assertEquals(0, tsp.getSolution(0));
    }



    @Test
    void testTSPGeneral() {
        TSP tsp = new TSP1();

        // Create a test graph
        Path[][] costMatrix = new Path[4][4];
        for (int i = 0; i < costMatrix.length; i++) {
            for (int j = 0; j < costMatrix.length; j++) {
                costMatrix[i][j] = new Path();
            }
        }
        costMatrix[0][0].setLength(0);
        costMatrix[0][1].setLength(1);
        costMatrix[0][2].setLength(9);
        costMatrix[0][3].setLength(9);
        costMatrix[1][0].setLength(9);
        costMatrix[1][1].setLength(0);
        costMatrix[1][2].setLength(1);
        costMatrix[1][3].setLength(9);
        costMatrix[2][0].setLength(9);
        costMatrix[2][1].setLength(9);
        costMatrix[2][2].setLength(0);
        costMatrix[2][3].setLength(1);
        costMatrix[3][0].setLength(1);
        costMatrix[3][1].setLength(9);
        costMatrix[3][2].setLength(9);
        costMatrix[3][3].setLength(0);
        DeliveryGraph g = new DeliveryGraph(costMatrix);

        // Search for a solution
        for(int i=0; i<10000; i++) {
            tsp.searchSolution(10000, g);
        }

        // Print the solution
        for (int i = 0; i < costMatrix.length; i++) {
            // The graph is constructed so that the solution is i (0 1 2 3)
            assertEquals(i, tsp.getSolution(i));
        }
    }
}
