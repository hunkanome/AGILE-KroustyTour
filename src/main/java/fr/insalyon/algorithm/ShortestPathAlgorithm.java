package fr.insalyon.algorithm;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;

public interface ShortestPathAlgorithm {
    /**
     * Computes a Path, being the shortest path between two nodes
     *
     * @param map the map which intersections and segments are used to compute all shortest paths
     * @param startNode the start node of the path
     * @param endNode the end node of the path
     * @return the shortest path between the two nodes
     * @see CityMap
     * @see Intersection
     */
    Path shortestPath(CityMap map, Intersection startNode, Intersection endNode);
}
