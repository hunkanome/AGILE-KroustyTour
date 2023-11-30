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
		if(j==0) {
			return cost[i][j].getLength() / AVG_SPEED;
		}
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

	// Get the next Time Window after current
	// Algorithm : get minimal Time Window that is after current
	public TimeWindow getNextTimeWindow(TimeWindow current) {
		TimeWindow next = null;
		for (Delivery delivery : deliveries) {
			if (delivery.getTimeWindow().isAfter(current)) {
				if (next == null) {
					next = delivery.getTimeWindow();
				} else if (delivery.getTimeWindow().isBefore(next)) {
					next = delivery.getTimeWindow();
				}
			}
		}
		return next;
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

	// Returns true if there is deliveries in the preceding Time Window
	public boolean hasDeliveriesInPrecedingTimeWindow(TimeWindow current) {
		for (Delivery delivery : deliveries) {
			if (delivery.getTimeWindow().isRightBefore(current)) {
				return true;
			}
		}
		return false;
	}
}
