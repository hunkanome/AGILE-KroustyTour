package fr.insalyon.algorithm;

import fr.insalyon.model.Path;

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
		Graph g = new DeliveryGraph(costMatrix);

		// Measure the time
		long start = System.currentTimeMillis();

		// Search for a solution
		for(int i=0; i<10000; i++) {
			tsp.searchSolution(10000, g);
		}

		// Print time duration
		System.out.println("Duration: " + (System.currentTimeMillis() - start) + " ms");

		// Print the solution
		for (int i = 0; i < costMatrix.length; i++) {
			System.out.print(tsp.getSolution(i) + " ");
		}
	}
}
