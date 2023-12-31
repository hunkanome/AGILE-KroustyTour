package fr.insalyon.algorithm;

import java.util.Collection;

public class TSP1 extends TemplateTSP {

	/**
	 * @param currentVertex vertex where we are located
	 * @param unvisited vertices left to be explored
	 * @return the lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>,
	 */
	@Override
	protected float bound(Integer currentVertex, Collection<Integer> unvisited) {
		float bound = 0;
		for (Integer i : unvisited) {
			float cost = graph.getCost(currentVertex, i);
			if (cost < bound) {
				bound = cost;
			}
		}
		return bound;
	}

}
