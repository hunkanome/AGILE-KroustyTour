package fr.insalyon.algorithm;

import fr.insalyon.model.*;


public class DeliveryGraph implements Graph {
	private static final float AVG_SPEED = 15 * 60 / 3.6f; // m/min
	public static final int DELIVERY_TIME = 5;

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


	/**
	 * @param i the origin vertex
	 * @param j the destination vertex
	 * @return the cost of the edge (i,j), if it exists, or -1 if it does not exist
	 */
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
			value = cost[i][j].getLength() / AVG_SPEED + DELIVERY_TIME;
		}
		return value;
	}

	/**
	 * @param i the index of an intersection (vertex)
	 * @return the delivery associated with the intersection
	 */
	public Delivery getDelivery(int i) {
		if (i<0 || i>=nbVertices) {
			return null;
		}
		return deliveries[i];
	}

	/**
	 * @param i the origin vertex
	 * @param j the destination vertex
	 * @return true if there is an edge between i and j
	 */
	@Override
	public boolean isArc(int i, int j) {
		return getCost(i,j) != -1;
	}

	/**
	 * @param i the index of an intersection (vertex)s
	 * @return the time window associated with the delivery at the intersection
	 */
	public TimeWindow getDeliveryTimeWindow(int i) {
		if (i<0 || i>=nbVertices)
			return null;
		return deliveries[i].getTimeWindow();
	}

	/**
	 * @return the time window of the earliest delivery
	 */
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

	/**
	 * @param current the current time window
	 * @return the next time window after the current one
	 */
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

	/**
	 * @param current the current time window
	 * @return true if there is at least one delivery in the time window preceding the current one
	 */
	public boolean hasDeliveriesInPrecedingTimeWindow(TimeWindow current) {
		for(int i = 1; i < deliveries.length; i++) {
			if(deliveries[i].getTimeWindow().isRightBefore(current)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Debugging method printing the cost graph (with times in minutes)
	 */
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
