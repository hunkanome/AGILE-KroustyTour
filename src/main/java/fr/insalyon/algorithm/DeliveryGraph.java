package fr.insalyon.algorithm;

import fr.insalyon.model.*;


public class DeliveryGraph implements Graph {
	private static final float AVG_SPEED = 15 * 60 / 3.6f; // m/min

	int nbVertices;
	Path[][] cost;
	Delivery[] deliveries;

	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param cost
	 */
	public DeliveryGraph(Path[][] cost, Delivery[] deliveries){
		this.nbVertices = cost.length;
		this.cost = cost;
		this.deliveries = deliveries;
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public float getCost(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return -1;
		if (i == j)
			return 0;
		return cost[i][j].getLength() / AVG_SPEED + 5;
	}

	@Override
	public boolean isArc(int i, int j) {
		return getCost(i,j) != -1;
	}

	public TimeWindow getDeliveryTimeWindow(int i) {
		if (i<0 || i>=nbVertices)
			return null;
		return deliveries[i].getTimeWindow();
	}

	// get the earliest Time Window of all deliveries
	public TimeWindow getStartTimeWindow() {
		if(nbVertices == 0) return null;
		TimeWindow earliest = deliveries[0].getTimeWindow();
		for (Delivery delivery : deliveries) {
			if (delivery.getTimeWindow().isBefore(earliest)) {
				earliest = delivery.getTimeWindow();
			}
		}
		return earliest;
	}

	public TimeWindow getNextTimeWindow(TimeWindow current) {
		TimeWindow nextTimeWindow = null;
		for (Delivery delivery : deliveries) {
			if (current.isRightBefore(delivery.getTimeWindow())) {
				nextTimeWindow = delivery.getTimeWindow();
				break;
			}
		}
		return nextTimeWindow;
	}

	public void printTimeCostGraph() {
		System.out.println("Time cost graph:");
		for (int i = 0; i < cost.length; i++) {
			for (int j = 0; j < cost.length; j++) {
				System.out.print(getCost(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
