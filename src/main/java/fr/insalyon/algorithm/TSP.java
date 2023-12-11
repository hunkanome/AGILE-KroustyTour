package fr.insalyon.algorithm;

import fr.insalyon.model.TimeWindow;

import java.util.List;
import java.util.Map;

public interface TSP {
	/**
	 * Search for the shortest cost hamiltonian circuit in <code>g</code> within <code>timeLimit</code> milliseconds
	 * (returns the best found tour whenever the time limit is reached)
	 * Warning: The computed tour always start from vertex 0
	 * @param timeLimit maximal time allowed to find a solution
	 * @param graph the DeliveryGraph in which a TSP tour must be computed
	 * @param deliveriesByTimeWindow the list of index of deliveries to be visited, grouped by time window
	 */
    void searchSolution(int timeLimit, Graph graph, Map<TimeWindow, List<Integer>> deliveriesByTimeWindow);
	
	/**
	 * @param i the index of the vertex in the solution
	 * @return the ith visited vertex in the solution computed by <code>searchSolution</code> 
	 * (-1 if <code>searchSolution</code> has not been called yet, or if i &lt; 0 or i &ge; g.getNbSommets())
	 */
    int getSolution(int i);
}
