package fr.insalyon.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import fr.insalyon.model.Path;
import fr.insalyon.model.TimeWindow;

public abstract class TemplateTSP implements TSP {
	private Integer[] bestSol;
	protected DeliveryGraph deliveryGraph;
	private float bestSolCost;
	private int timeLimit;
	private long startTime;

	/**
	 * Method that launches the resolution of the TSP
	 * @param timeLimit maximal time limit of the resolution in milliseconds
	 * @param g the graph on which the TSP should be resolved
	 */

	public void searchSolution(int timeLimit, DeliveryGraph g){
		if (timeLimit <= 0) return;
		if (g.getNbVertices()<=0) return;
		this.startTime = System.currentTimeMillis();
		this.timeLimit = timeLimit;
		this.deliveryGraph = g;
		this.bestSol = new Integer[g.getNbVertices()];
		Collection<Integer> unvisited = new ArrayList<>(g.getNbVertices()-1);
		for (int i=1; i<g.getNbVertices(); i++) unvisited.add(i);
		Collection<Integer> visited = new ArrayList<>(g.getNbVertices());
		visited.add(0); // The first visited vertex is 0
		this.bestSolCost = Float.MAX_VALUE;
		branchAndBound(0, unvisited, visited, 0, 0, g.getStartTimeWindow());
	}

	/**
	 * @param i index of the vertex in the current best solution
	 * @return the i-th vertex of the best solution computed so far
	 */
	public int getSolution(int i) {
		if (this.deliveryGraph != null && i>=0 && i< deliveryGraph.getNbVertices())
			return this.bestSol[i];
		return -1;
	}

	/**
	 * @return the cost of the best solution computed so far
	 */
	public float getSolutionCost(){
		if (this.deliveryGraph != null)
			return this.bestSolCost;
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
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param branchCost the cost of the path corresponding to <code>visited</code>
	 * @param currentTimeWindowCost the time spent in the current time window
	 * @param currentTimeWindow the current time window
	 */
	private void branchAndBound(
			int currentVertex,
			Collection<Integer> unvisited,
			Collection<Integer> visited,
			float branchCost,
			float currentTimeWindowCost,
			TimeWindow currentTimeWindow
	){
		// Avoid spending too much time in this method
		if (System.currentTimeMillis() - startTime > timeLimit) {
			return;
		}

		// Update best score if we have visited all the vertices
		if (unvisited.isEmpty()){
			if (deliveryGraph.isArc(currentVertex,0) // If we can go back to the warehouse
				&& (branchCost+ deliveryGraph.getCost(currentVertex,0) < bestSolCost) // If the cost is better than the best solution
			){
				visited.toArray(bestSol);
				bestSolCost = branchCost + deliveryGraph.getCost(currentVertex,0);
			}
			return;
		}

		// Do not explore branch if it cannot improve cost
		if (branchCost + bound(currentVertex,unvisited) >= bestSolCost){
			return;
		}

		// Checking if all the deliveries in the current time window have been visited
		// If it is the case we update the current time window
		TimeWindow finalCurrentTimeWindow1 = currentTimeWindow;
		if (unvisited.stream().noneMatch(i -> deliveryGraph.getDelivery(i).getTimeWindow().equals(finalCurrentTimeWindow1))) {
			// Get the nearest nextTimeWindow
            TimeWindow nextWindow = unvisited
					.stream()
					.map(i -> deliveryGraph.getDelivery(i).getTimeWindow())
                    .min(Comparator.comparing(TimeWindow::getStartHour))
                    .orElseThrow(IllegalStateException::new); // unvisited or subsequent streams cannot be empty
			// Account for a difference between endHour of previous and startHour of next
			currentTimeWindowCost -= (nextWindow.getStartHour() - currentTimeWindow.getStartHour())*60;
			// Update the current time window
			currentTimeWindow = nextWindow;
		}

		final TimeWindow finalCurrentTimeWindow = currentTimeWindow;
		Collection<Integer> nextDeliveries = unvisited
				.stream()
				.filter(i -> deliveryGraph.getDelivery(i).getTimeWindow().equals(finalCurrentTimeWindow))
				.toList();

		// Explore the different path possible
		for (Integer deliveryVertex : nextDeliveries) {
            float newCurrentTimeWindowCost = currentTimeWindowCost + deliveryGraph.getCost(currentVertex, deliveryVertex);
			float newBranchCost = branchCost + deliveryGraph.getCost(currentVertex, deliveryVertex);
			if (newCurrentTimeWindowCost < 5) {
                newCurrentTimeWindowCost = 5 - deliveryGraph.getCost(currentVertex, deliveryVertex);
                newBranchCost = (float) (Math.ceil(branchCost/60) * 60 + currentTimeWindowCost);
			}
            // If we are running out of time, we stop the branch
            if(newCurrentTimeWindowCost > 55) {
                return;
            }
			visited.add(deliveryVertex);
			unvisited.remove(deliveryVertex);
			branchAndBound(deliveryVertex, unvisited, visited,newBranchCost, newCurrentTimeWindowCost, currentTimeWindow);
			visited.remove(deliveryVertex);
			unvisited.add(deliveryVertex);
		}
	}

	public List<Path> getSolutionPaths() {
		ArrayList<Path> pathList = new ArrayList<>(this.bestSol.length-1);

		for (int i=0; i<this.bestSol.length-1; i++) {
			pathList.add(this.deliveryGraph.getCost()[i][i+1]);
		}

		return pathList;
	}
}