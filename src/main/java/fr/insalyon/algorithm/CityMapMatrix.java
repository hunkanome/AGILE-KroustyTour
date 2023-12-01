package fr.insalyon.algorithm;

import fr.insalyon.model.Intersection;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Path;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Class used to compute and store the matrix of the shortest path between the delivery intersections
 */
public class CityMapMatrix {

    private final ShortestPathAlgorithm shortestPathAlgorithm = new AStar();

    private Path[][] arrayPaths;

    private final CityMap map;

    private final List<Intersection> intersections;

    /**
     * Construct a new matrix of shortest path between passed locations
     * @param map the map which intersections and segments are used to compute all shortest paths
     * @param deliveries the deliveries locations
     * @see Intersection
     */
    public CityMapMatrix(CityMap map, List<Intersection> deliveries) {
        this.arrayPaths = new Path[deliveries.size()][deliveries.size()];
        this.map = map;
        this.intersections = deliveries;

        int i = 0;
        for (Intersection startIntersection : deliveries) {
            int j = 0;
            for (Intersection endIntersection : deliveries) {
                if (startIntersection != endIntersection) {
                    // Dijkstra
                    this.arrayPaths[i][j] = shortestPath(startIntersection, endIntersection);
                    this.arrayPaths[j][i] = shortestPath(endIntersection, startIntersection);
                } else {
                    this.arrayPaths[i][j] = new Path(startIntersection, startIntersection, new ArrayList<>());
                }
                j++;
            }
            i++;
        }
    }

    public Path[][] getArrayPaths() { return arrayPaths; }

    public void setArrayPaths(Path[][] arrayPaths) { this.arrayPaths = arrayPaths; }

    /**
     * Computes the shortest path between the two intersection
     * @param startNode Starting intersection
     * @param endNode Arrival intersection
     * @return If there is a path between the two intersections, the Path object is returned
     * otherwise an empty Path with no segments is returned
     * @see Intersection
     * @see Path
     */
    private Path shortestPath(Intersection startNode, Intersection endNode) {
        return shortestPathAlgorithm.shortestPath(this.map, startNode, endNode);
    }

    /**
     * Add an intersection to the list and calculates the shortest path between this intersection and all the others
     * @param newIntersection The new intersection to add
     * @see Intersection
     */
    public void addIntersection(Intersection newIntersection) {
        int size = this.arrayPaths.length + 1;
        int i;

        this.intersections.add(newIntersection);

        // increase the size of the array
        this.arrayPaths = Arrays.copyOf(this.arrayPaths, size);
        this.arrayPaths[size - 1] = new Path[size];
        for (i = 0; i < size - 1; i++) {
            this.arrayPaths[i] = Arrays.copyOf(this.arrayPaths[i], size);
        }

        // fill the array with the new paths
        for (i = 0; i < size; i++) {
            this.arrayPaths[size - 1][i] = shortestPath(newIntersection, this.intersections.get(i));
            this.arrayPaths[i][size - 1] = shortestPath(this.intersections.get(i), newIntersection);
        }
    }
}
