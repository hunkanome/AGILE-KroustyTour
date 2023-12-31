package fr.insalyon.algorithm;

import fr.insalyon.model.Path;

public interface Graph {
	/**
	 * @return the number of vertices in <code>this</code>
	 */
    int getNbVertices();

	/**
	 * @param i a vertex
	 * @param j another vertex
	 * @return the cost of arc (i,j) if (i,j) is an arc; -1 otherwise
	 */
    float getCost(int i, int j);

	/**
	 * Returns the shortest path between two vertices
	 * @param i the origin vertex
	 * @param j the destination vertex
	 * @return the shortest path between <code>i</code> and <code>j</code>
	 */
	Path getShortestPath(int i, int j);

	/**
	 * @param i a vertex
	 * @param j another vertex
	 * @return true if <code>(i,j)</code> is an arc of <code>this</code>
	 */
    boolean isArc(int i, int j);
}
