package fr.insalyon.algorithm;

public interface TSP {
	/**
	 * Search for a shortest cost hamiltonian circuit in <code>g</code> within <code>timeLimit</code> milliseconds
	 * (returns the best found tour whenever the time limit is reached)
	 * Warning: The computed tour always start from vertex 0
	 * @param timeLimit
	 * @param g
	 */
    void searchSolution(int timeLimit, DeliveryGraph g);
	
	/**
	 * @param i
	 * @return the ith visited vertex in the solution computed by <code>searchSolution</code> 
	 * (-1 if <code>searcheSolution</code> has not been called yet, or if i &lt; 0 or i &ge; g.getNbSommets())
	 */
    int getSolution(int i);
	
	/** 
	 * @return the total cost of the solution computed by <code>searchSolution</code> 
	 * (-1 if <code>searcheSolution</code> has not been called yet).
	 */
    float getSolutionCost();

}
