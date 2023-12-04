package fr.insalyon.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class TemplateTSP implements TSP {
	private Integer[] bestSol;
	protected Graph g;
	private float bestSolCost;
	private int timeLimit;
	private long startTime;
	
	public void searchSolution(int timeLimit, Graph g){
		if (timeLimit <= 0) return;
		if (g.getNbVertices()<=0) return;
		this.startTime = System.currentTimeMillis();
		this.timeLimit = timeLimit;
		this.g = g;
		this.bestSol = new Integer[g.getNbVertices()];
		Collection<Integer> unvisited = new ArrayList<>(g.getNbVertices()-1);
		for (int i=1; i<g.getNbVertices(); i++) unvisited.add(i);
		Collection<Integer> visited = new ArrayList<>(g.getNbVertices());
		visited.add(0); // The first visited vertex is 0
		this.bestSolCost = Float.MAX_VALUE;
		branchAndBound(0, unvisited, visited, 0);
	}
	
	public int getSolution(int i){
		if (this.g != null && i>=0 && i<g.getNbVertices())
			return this.bestSol[i];
		return -1;
	}
	
	public float getSolutionCost(){
		if (this.g != null)
			return this.bestSolCost;
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
			Collection<Integer> visited, float currentCost){
		if (System.currentTimeMillis() - this.startTime > this.timeLimit) return;
		if (unvisited.isEmpty()){
	    	if (g.isArc(currentVertex,0) && (currentCost+g.getCost(currentVertex,0) < this.bestSolCost)) {
				visited.toArray(this.bestSol);
				this.bestSolCost = currentCost + this.g.getCost(currentVertex,0);
	    	}
	    } else if (currentCost+bound(currentVertex,unvisited) < this.bestSolCost){
	        Iterator<Integer> it = iterator(currentVertex, unvisited, this.g);
	        while (it.hasNext()) {
	        	Integer nextVertex = it.next();
	        	visited.add(nextVertex);
	            unvisited.remove(nextVertex);
	            branchAndBound(nextVertex, unvisited, visited, 
	            		currentCost + this.g.getCost(currentVertex, nextVertex));
	            visited.remove(nextVertex);
	            unvisited.add(nextVertex);
	        }	    
	    }
	}

}
