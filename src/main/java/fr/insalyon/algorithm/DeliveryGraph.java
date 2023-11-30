package fr.insalyon.algorithm;

import fr.insalyon.model.*;


public class DeliveryGraph implements Graph {
	private static final float AVG_SPEED = 15 * 60 / 3.6f; // m/min
	public static final int DELIVERY_TIME = 1;

	private final int nbVertices;
	private final Path[][] cost;
	private final Delivery[] deliveries;

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
		float value;
		if (i < 0 || i >= nbVertices || j < 0 || j >= nbVertices) {
			value = -1;
		} else if (i == j) {
			value = 0;
		} else if(j==0) {
			value = cost[i][j].getLength() / AVG_SPEED;
		} else {
			value = cost[i][j].getLength() / AVG_SPEED + 5;
		}
		return value;
	}

	public Delivery getDelivery(int i) {
		if (i<0 || i>=nbVertices) {
			return null;
		}
		return deliveries[i];
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
	public TimeWindow getNextTimeWindow(TimeWindow current) {
		TimeWindow next;
		try {
			next = new TimeWindow(current.getStartHour() + 1);
			for (Delivery delivery : deliveries) {
				if (next == delivery.getTimeWindow() || delivery.getTimeWindow().isAfter(next)) {
					return next;
				}
			}
		} catch (IllegalArgumentException e) {
			next = null;
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
		for(int i = 1; i < deliveries.length; i++) {
			if(deliveries[i].getTimeWindow().isRightBefore(current)) {
				return true;
			}
		}
		return false;
	}
}
