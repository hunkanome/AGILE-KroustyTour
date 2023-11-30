package fr.insalyon.algorithm;

import fr.insalyon.model.*;


public class DeliveryGraph implements Graph {
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
		return cost[i][j].getLength();
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
}
