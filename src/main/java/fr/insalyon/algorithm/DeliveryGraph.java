package fr.insalyon.algorithm;

import fr.insalyon.model.Delivery;
import fr.insalyon.model.Path;
import fr.insalyon.model.TimeWindow;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryGraph implements Graph {
	private static final float AVG_SPEED = 15 * 60 / 3.6f; // m/min
	public static final int DELIVERY_TIME = (int) Delivery.DURATION.toMinutes();

	private final int nbVertices;
	private final Path[][] cost;
	private final Delivery[] deliveries;

	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param cost matrix of path from each delivery to each delivery
	 */
	public DeliveryGraph(Path[][] cost, Delivery[] deliveries){
		this.nbVertices = cost.length;
		this.cost = cost;
		this.deliveries = deliveries;
	}

	@Override
	public int getNbVertices() {
		return this.nbVertices;
	}

	@Override
	public Path[][] getCost() {
		return this.cost;
	}

	/**
	 * @param i the origin vertex
	 * @param j the destination vertex
	 * @return the cost of the edge (i,j), if it exists, or -1 if it does not exist
	 */
	@Override
	public float getCost(int i, int j) {
		float value;
		if (i < 0 || i >= this.nbVertices || j < 0 || j >= this.nbVertices) {
			value = -1;
		} else if (i == j) {
			value = 0;
		} else if(j==0) {
			value = this.cost[i][j].getLength() / AVG_SPEED;
		} else {
			value = this.cost[i][j].getLength() / AVG_SPEED + DELIVERY_TIME;
		}
		return value;
	}

	/**
	 * @param i the index of an intersection (vertex)
	 * @return the delivery associated with the intersection
	 */
	public Delivery getDelivery(int i) {
		if (i < 0 || i >= this.nbVertices) {
			throw new IndexOutOfBoundsException("The index " + i + " is out of bounds");
		}
		return this.deliveries[i];
	}

	/**
	 * @param i the origin vertex
	 * @param j the destination vertex
	 * @return true if there is an edge (i, j)
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
		if (i < 0 || i >= this.nbVertices)
			return null;
		return this.deliveries[i].getTimeWindow();
	}

	/**
	 * @return the time window of the earliest delivery
	 */
	public TimeWindow getStartTimeWindow() {
		if(this.nbVertices == 0) {
			return null;
		}
		TimeWindow earliest = this.deliveries[0].getTimeWindow();
		for (Delivery delivery : this.deliveries) {
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
			for (Delivery delivery : this.deliveries) {
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
	 * Debugging method printing the cost graph (with times in minutes)
	 */
	public void printTimeCostGraph() {
		Logger logger = Logger.getLogger(getClass().getName());
		logger.info("Time cost graph:\n");
		for (int i = 0; i < this.cost.length; i++) {
			for (int j = 0; j < this.cost.length; j++) {
				logger.log(Level.INFO ,"{0} ", getCost(i, j));
			}
			logger.info("\n");
		}
		logger.info("\n");
	}
}
