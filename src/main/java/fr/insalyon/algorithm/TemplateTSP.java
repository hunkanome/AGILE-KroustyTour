package fr.insalyon.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import fr.insalyon.model.*;

public abstract class TemplateTSP implements TSP {
	private Integer[] bestSol;
	protected DeliveryGraph g;
	protected Delivery[] deliveries;
	private float bestSolCost;
	private int timeLimit;
	private long startTime;
	
	public void searchSolution(int timeLimit, DeliveryGraph g){
		if (timeLimit <= 0) return;
		if (g.getNbVertices()<=0) return;
		startTime = System.currentTimeMillis();	
		this.timeLimit = timeLimit;
		this.g = g;
		this.deliveries = g.deliveries;
		bestSol = new Integer[g.getNbVertices()];
		Collection<Integer> unvisited = new ArrayList<>(g.getNbVertices()-1);
		for (int i=1; i<g.getNbVertices(); i++) unvisited.add(i);
		Collection<Integer> visited = new ArrayList<>(g.getNbVertices());
		visited.add(0); // The first visited vertex is 0
		bestSolCost = Float.MAX_VALUE;
		branchAndBound(0, unvisited, visited, 0, g.getStartTimeWindow());
	}
	
	public int getSolution(int i) {
		if (g != null && i>=0 && i<g.getNbVertices())
			return bestSol[i];
		return -1;
	}
	
	public float getSolutionCost(){
		if (g != null)
			return bestSolCost;
		return -1;
	}
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting 
	 * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
	 */
	protected abstract float bound(Integer currentVertex, Collection<Integer> unvisited);
	
	/**
	 * Method that must be defined in TemplateTSP subclasses
	 * @param currentVertex
	 * @param unvisited
	 * @param g
	 * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
	 */
	protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g);
	
	/**
	 * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
	 * @param currentVertex the last visited vertex
	 * @param unvisited the set of vertex that have not yet been visited
	 * @param visited the sequence of vertices that have been already visited (including currentVertex)
	 * @param currentCost the cost of the path corresponding to <code>visited</code>
	 */	
	private void branchAndBound(int currentVertex, Collection<Integer> unvisited, 
			Collection<Integer> visited, float currentCost, TimeWindow currentTimeWindow){
		if (System.currentTimeMillis() - startTime > timeLimit) return;
		Integer nextVertex;
		if (unvisited.isEmpty()){
	    	if (g.isArc(currentVertex,0) && (currentCost+g.getCost(currentVertex,0) < bestSolCost)){
				visited.toArray(bestSol);
				bestSolCost = currentCost+g.getCost(currentVertex,0);
	    	}
	    } else if (currentCost+bound(currentVertex,unvisited) < bestSolCost){
	        Iterator<Integer> it = iterator(currentVertex, unvisited, g);
			while(true) {
				while (it.hasNext()) {
					nextVertex = it.next();
					if (deliveries[nextVertex].getTimeWindow() == currentTimeWindow) {
						visited.add(nextVertex);
						unvisited.remove(nextVertex);
						branchAndBound(nextVertex, unvisited, visited,
								currentCost + g.getCost(currentVertex, nextVertex), currentTimeWindow);
						visited.remove(nextVertex);
						unvisited.add(nextVertex);
					}
				}
				// Change the time window
				currentTimeWindow = g.getNextTimeWindow(currentTimeWindow);
				it = iterator(currentVertex, unvisited, g);
				if(currentTimeWindow == null) break;
			}
	    }
	}
}