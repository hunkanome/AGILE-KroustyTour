package fr.insalyon.algorithm;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;

public interface ShortestPathAlgorithm {
    final int NO_PREDECESSOR = -1;
    final int START_NODE_PREDECESSOR = -2;
    Path shortestPath(CityMap map, Intersection startNode, Intersection endNode);
}
