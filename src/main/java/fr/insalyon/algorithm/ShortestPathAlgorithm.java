package fr.insalyon.algorithm;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;

public interface ShortestPathAlgorithm {
    final int NO_PREDECESSORS = -2;
    Path shortestPath(CityMap map, Intersection startNode, Intersection endNode);
}
