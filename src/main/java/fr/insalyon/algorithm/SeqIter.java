package fr.insalyon.algorithm;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SeqIter implements Iterator<Integer> {
	private final Integer[] candidates;
	private int nbCandidates;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited vertices left to be explored
	 * @param currentVertex vertex where we are located
	 * @param g the graph
	 */
	public SeqIter(Collection<Integer> unvisited, int currentVertex, Graph g){
		this.candidates = new Integer[unvisited.size()];
		for (Integer s : unvisited){
			if (g.isArc(currentVertex, s))
				candidates[nbCandidates++] = s;
		}
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidates > 0;
	}

	@Override
	public Integer next() {
		if (!hasNext()){
			throw new NoSuchElementException();
		}
		nbCandidates--;
		return candidates[nbCandidates];
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
