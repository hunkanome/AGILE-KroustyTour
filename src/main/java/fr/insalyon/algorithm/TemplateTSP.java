package fr.insalyon.algorithm;

import fr.insalyon.model.TimeWindow;

import java.util.*;

public abstract class TemplateTSP implements TSP {

	private Integer[] bestSol;

	protected Graph graph;

	private float bestSolCost;

	private int timeLimit;

	private long startTime;

	public Integer[] getBestSol() {
		return bestSol;
	}

	/**
	 * Method that launches the resolution of the TSP
	 * @param timeLimit maximal time limit of the resolution in milliseconds
	 * @param graph the graph on which the TSP should be resolved
	 * @param deliveriesByTimeWindow the list of index of deliveries to be visited, grouped by time window
	 */
	public void searchSolution(int timeLimit, Graph graph, Map<TimeWindow, List<Integer>> deliveriesByTimeWindow){
		if (timeLimit <= 0 || graph.getNbVertices() <= 0) {
			return;
		}
		this.startTime = System.currentTimeMillis();
		this.timeLimit = timeLimit;
		this.graph = graph;
		this.bestSol = new Integer[graph.getNbVertices()];
		this.bestSolCost = Float.MAX_VALUE;

		Collection<Integer> visited = new ArrayList<>(graph.getNbVertices());
		visited.add(0); // The first visited vertex is 0, corresponding to the warehouse
		deliveriesByTimeWindow.get(TimeWindow.getTimeWindow(8)).remove(0); // We remove the warehouse from the list of deliveries to be visited

		branchAndBound(0, deliveriesByTimeWindow, visited, 0, 0, TimeWindow.getTimeWindow(8));
	}

	/**
	 * @param i index of the vertex in the current best solution
	 * @return the i-th vertex of the best solution computed so far
	 */
	public int getSolution(int i) {
		if (this.graph != null && i>=0 && i< graph.getNbVertices())
			return this.bestSol[i];
		return -1;
	}

	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex vertex where we are located
	 * @param unvisited vertices left to be explored
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract float bound(Integer currentVertex, Collection<Integer> unvisited);

	/**
	 * Branch and bound algorithm for solving the TSP in <code>g</code>. Includes consideration of time windows.
	 * @param currentVertex the last visited vertex
	 * @param unvisitedByTimeWindow the set of vertices that have not yet been visited indexed by TimeWindow
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param branchCost the cost of the path corresponding to <code>visited</code>
	 * @param currentTimeWindowCost the time spent in the current time window
	 * @param currentTimeWindow the current time window
	 */
	private void branchAndBound(
			int currentVertex,
			Map<TimeWindow, List<Integer>> unvisitedByTimeWindow,
			Collection<Integer> visited,
			float branchCost,
			float currentTimeWindowCost,
			TimeWindow currentTimeWindow
	){
		// Avoid spending too much time in this method
		if (System.currentTimeMillis() - startTime > timeLimit) {
			return;
		}

		// Do not explore branch if it cannot improve cost
		if (branchCost + bound(currentVertex,unvisitedByTimeWindow.values().stream().flatMap(Collection::stream).toList()) >= bestSolCost){
			return;
		}

		// Checking if all the deliveries in the current time window have been visited
		// If it is the case we update the current time window
		if (unvisitedByTimeWindow.get(currentTimeWindow) == null || unvisitedByTimeWindow.get(currentTimeWindow).isEmpty()) {
			unvisitedByTimeWindow.remove(currentTimeWindow);
			if (unvisitedByTimeWindow.isEmpty()) {// If we have entirely explored all the TimeWindow
				if (branchCost + this.graph.getCost(currentVertex, 0) < this.bestSolCost) {
					visited.toArray(this.bestSol);
					this.bestSolCost = branchCost + this.graph.getCost(currentVertex, 0);
				}
				return;
			}

			// Get the nearest nextTimeWindow
			TimeWindow nextWindow = unvisitedByTimeWindow
					.keySet()
					.stream()
					.min(Comparator.comparing(TimeWindow::getStartHour))
					.orElseThrow(IllegalStateException::new);

			currentTimeWindowCost -= (nextWindow.getStartHour() - currentTimeWindow.getStartHour())*60;

			currentTimeWindow = nextWindow;
		}

		List<Integer> unvisited = unvisitedByTimeWindow.get(currentTimeWindow).stream().toList();
		// Explore the different paths possible
		for (Integer deliveryVertex : unvisited) {
            float newCurrentTimeWindowCost = currentTimeWindowCost + graph.getCost(currentVertex, deliveryVertex);
			float newBranchCost = branchCost + graph.getCost(currentVertex, deliveryVertex);
			if (newCurrentTimeWindowCost < 5) {
                newCurrentTimeWindowCost = 5 - graph.getCost(currentVertex, deliveryVertex);
                newBranchCost = (float) (Math.ceil(branchCost/60) * 60 + currentTimeWindowCost);
			}
            // If we are running out of time, we stop the branch
            if(newCurrentTimeWindowCost > 55) {
                return;
            }
			visited.add(deliveryVertex);
			unvisitedByTimeWindow.get(currentTimeWindow).remove(deliveryVertex);
			branchAndBound(deliveryVertex, unvisitedByTimeWindow, visited,newBranchCost, newCurrentTimeWindowCost, currentTimeWindow);
			visited.remove(deliveryVertex);
			unvisitedByTimeWindow.computeIfAbsent(currentTimeWindow, k -> new ArrayList<>()).add(deliveryVertex);
		}
	}
}