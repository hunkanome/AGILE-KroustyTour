package fr.insalyon.algorithm;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {
	@Override
	protected float bound(Integer currentVertex, Collection<Integer> unvisited) {
		float bound = 0;
		for (Integer i : unvisited) {
			bound += g.getCost(currentVertex, i);
		}
		return bound;
//		return 0;
	}

	@Override
	protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

}
