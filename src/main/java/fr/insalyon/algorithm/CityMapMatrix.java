package fr.insalyon.algorithm;

import fr.insalyon.model.Delivery;
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
    private DeliveryGraph graph;
    private final CityMap map;
    private final List<Delivery> deliveries;

    /**
     * Construct a new matrix of shortest path between passed locations
     * @param map the map which intersections and segments are used to compute all shortest paths
     * @param deliveries the deliveries locations
     * @see Intersection
     */
    public CityMapMatrix(CityMap map, List<Delivery> deliveries) {
        Path[][] arrayPaths = new Path[deliveries.size()][deliveries.size()];
        this.map = map;
        this.deliveries = deliveries;

        int i = 0;
        for (Delivery startIntersection : deliveries) {
            int j = 0;
            for (Delivery endIntersection : deliveries) {
                if (startIntersection != endIntersection) {
                    // Dijkstra
                    arrayPaths[i][j] = shortestPath(startIntersection.getLocation(), endIntersection.getLocation());
                    arrayPaths[j][i] = shortestPath(endIntersection.getLocation(), startIntersection.getLocation());
                } else {
                    arrayPaths[i][j] = new Path(startIntersection.getLocation(), startIntersection.getLocation(), new ArrayList<>());
                }
                j++;
            }
            i++;
        }
        Delivery[] deliv = new Delivery[deliveries.size()];
        deliveries.toArray(deliv);
        this.graph = new DeliveryGraph(arrayPaths, deliv);
    }

    public DeliveryGraph getGraph() {
        return this.graph;
    }

    public void setGraph(DeliveryGraph graph) {
        this.graph = graph;
    }

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
     * @param newDelivery The new intersection to add
     * @see Intersection
     */
    public void addDelivery(Delivery newDelivery) {
        int size = this.graph.getNbVertices() + 1;
        int i;

        this.deliveries.add(newDelivery);

        // increase the size of the array
        Delivery[] deliv = new Delivery[deliveries.size()];
        this.deliveries.toArray(deliv);
        this.graph = new DeliveryGraph(Arrays.copyOf(this.graph.getCost(), size), deliv);
        this.graph.getCost()[size - 1] = new Path[size];
        for (i = 0; i < size - 1; i++) {
            this.graph.getCost()[i] = Arrays.copyOf(this.graph.getCost()[i], size);
        }

        // fill the array with the new paths
        for (i = 0; i < size; i++) {
            this.graph.getCost()[size - 1][i] = shortestPath(newDelivery.getLocation(), this.deliveries.get(i).getLocation());
            this.graph.getCost()[i][size - 1] = shortestPath(this.deliveries.get(i).getLocation(), newDelivery.getLocation());
        }
    }

}
