package fr.insalyon.algorithm;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * A complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
 * It is internally represented with an adjacency matrix.
 * @see Graph
 */
public class DeliveryGraph implements Graph {
	protected static final float AVG_SPEED = 15 * 1000 / 60.f; // m/min
	protected static final int DELIVERY_TIME = (int) Delivery.DURATION.toMinutes();
	protected final Path[][] paths;

	public DeliveryGraph(){
		this.paths = new Path[0][0];
	}

	/**
	 * Create a complete directed graph such that each edge has a weight
	 * @param deliveries the list of deliveries, the first element should be the warehouse
	 * @param map the city map
	 * @param shortestPathAlgorithm the algorithm used to compute the shortest path
	 */
	public DeliveryGraph(List<Delivery> deliveries, CityMap map, ShortestPathAlgorithm shortestPathAlgorithm){
		this.paths = new Path[deliveries.size()][deliveries.size()];
		int i = 0;
		for (Delivery startIntersection : deliveries) {
			int j = 0;
			for (Delivery endIntersection : deliveries) {
				if (startIntersection != endIntersection) {
					// Dijkstra
					paths[i][j] = shortestPathAlgorithm.shortestPath(map, startIntersection.getLocation(), endIntersection.getLocation());
					paths[j][i] = shortestPathAlgorithm.shortestPath(map, endIntersection.getLocation(), startIntersection.getLocation());
				} else {
					paths[i][j] = new Path(startIntersection.getLocation(), startIntersection.getLocation(), new ArrayList<>());
				}
				j++;
			}
			i++;
		}
	}

	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param paths matrix of path from each delivery to each delivery
	 */
	public DeliveryGraph(Path[][] paths){
		this.paths = paths;
	}

	/**
	 * @return the number of vertices of the graph
	 */
	@Override
	public int getNbVertices() {
		return this.paths.length;
	}

	/**
	 * Gives the shortest path between vertex i and vertex j
	 * @param i the origin vertex
	 * @param j the destination vertex
	 * @return a <code>Path</code> object representing the shortest path from i to j
	 */
	@Override
	public Path getShortestPath(int i, int j) {
		if (i<0 || i > this.getNbVertices() || j<0 || j > this.getNbVertices() || i == j) {
			return null;
		}
		return this.paths[i][j];
	}

	/**
	 * Gives the cost of the shortest between vertices i and j
	 * @param i the origin vertex
	 * @param j the destination vertex
	 * @return the cost of the edge (i,j), if it exists, or -1 if it does not exist
	 */
	@Override
	public float getCost(int i, int j) {
		float value;
		if (i < 0 || i >= this.getNbVertices() || j < 0 || j >= this.getNbVertices()) {
			value = -1;
		} else if (i == j) {
			value = 0;
		} else if(j==0) {
			value = this.paths[i][j].getLength() / AVG_SPEED;
		} else {
			value = this.paths[i][j].getLength() / AVG_SPEED + DELIVERY_TIME;
		}
		return value;
	}

	/**
	 * Checks if a path exists between vertices i and j
	 * @param i the origin vertex
	 * @param j the destination vertex
	 * @return true if there is an edge (i, j)
	 */
	@Override
	public boolean isArc(int i, int j) {
		return this.paths[i][j].getLength() != 0;
	}
}