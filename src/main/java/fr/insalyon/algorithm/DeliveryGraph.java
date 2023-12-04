package fr.insalyon.algorithm;

import fr.insalyon.model.Path;

public class DeliveryGraph implements Graph {
	int nbVertices;
	private Path[][] cost;

	/**
	 * Create a complete directed graph such that each edge has a weight within [MIN_COST,MAX_COST]
	 * @param cost
	 */
	public DeliveryGraph(Path[][] cost){
		this.nbVertices = cost.length;
		this.cost = cost;
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	public Path[][] getCost() {
		return cost;
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
}
