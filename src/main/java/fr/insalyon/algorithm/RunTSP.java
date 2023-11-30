package fr.insalyon.algorithm;

import fr.insalyon.model.*;

public class RunTSP {
	public static void main(String[] args) {
		TSP tsp = new TSP1();

		// Create a test graph
		Path[][] costMatrix = new Path[4][4];
		for (int i = 0; i < costMatrix.length; i++) {
			for (int j = 0; j < costMatrix.length; j++) {
				costMatrix[i][j] = new Path();
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

		Courier courier = new Courier();
		Intersection intersection1 = new Intersection(0L, 1, 1, 0);
		Intersection intersection2 = new Intersection(1L, 1, 1, 1);
		Intersection intersection3 = new Intersection(2L, 1, 1, 2);
		Intersection intersection4 = new Intersection(3L, 1, 1, 3);

		TimeWindow tw8 = new TimeWindow(8);
		TimeWindow tw9 = new TimeWindow(9);
		TimeWindow tw10 = new TimeWindow(10);
		// TimeWindow tw11 = new TimeWindow(11);

		Delivery[] deliveries = new Delivery[4];
		deliveries[0] = new Delivery(courier, intersection1, tw8);
		deliveries[1] = new Delivery(courier, intersection2, tw9);
		deliveries[2] = new Delivery(courier, intersection3, tw8);
		deliveries[3] = new Delivery(courier, intersection4, tw10);

		DeliveryGraph g = new DeliveryGraph(costMatrix, deliveries);
		// Measure the time
		long start = System.currentTimeMillis();

		// Search for a solution
		for(int i=0; i<1; i++) {
			tsp.searchSolution(10000, g);
		}

		// Print time duration
		System.out.println("Duration: " + (System.currentTimeMillis() - start) + " ms");

		// Print the solution
		for (int i = 0; i < costMatrix.length; i++) {
			System.out.print(tsp.getSolution(i) + " ");
		}
		System.out.println("\nSolution cost: " + tsp.getSolutionCost() + " min");

		// Print graph cost with time
		g.printTimeCostGraph();
	}
}
