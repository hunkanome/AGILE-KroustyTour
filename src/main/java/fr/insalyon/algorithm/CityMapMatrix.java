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
        Delivery[] newDeliveries = new Delivery[deliveries.size()];
        deliveries.toArray(newDeliveries);
        this.graph = new DeliveryGraph(arrayPaths, newDeliveries);
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
     * Add a delivery to the list and calculates the shortest path between this delivery intersection and all the others
     * @param newDelivery The new intersection to add
     * @see Delivery
     * @see Intersection
     */
    public void addDelivery(Delivery newDelivery) {
        int size = this.graph.getNbVertices() + 1;
        int i;

        this.deliveries.add(newDelivery);

        // increase the size of the array
        Delivery[] newDeliveries = new Delivery[deliveries.size()];
        this.deliveries.toArray(newDeliveries);
        this.graph = new DeliveryGraph(Arrays.copyOf(this.graph.getCost(), size), newDeliveries);
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

    /**
     * Remove a delivery from the list and remove the related paths
     * @param delivery The delivery to remove
     * @see Delivery
     * @see Intersection
     */
    public void removeDelivery(Delivery delivery) {
        int deliveryIndex = this.deliveries.indexOf(delivery);

        if (deliveryIndex == -1) {
            return;
        }

        Path[][] newArrayPaths = new Path[deliveries.size()-1][deliveries.size()-1];

        for (int i=0; i<this.deliveries.size(); i++) {

            if (i == deliveryIndex) {
                continue;
            }

            for (int j=0; j<this.deliveries.size(); j++) {

                if (j == deliveryIndex) {
                    continue;
                }

                if (i > deliveryIndex && j > deliveryIndex) {
                    newArrayPaths[i-1][j-1] = this.graph.getCost()[i][j];
                } else if (i > deliveryIndex) {
                    newArrayPaths[i-1][j] = this.graph.getCost()[i][j];
                } else if (j > deliveryIndex) {
                    newArrayPaths[i][j-1] = this.graph.getCost()[i][j];
                }
            }
        }

        this.deliveries.remove(deliveryIndex);

        this.graph = new DeliveryGraph(newArrayPaths, this.deliveries.toArray(new Delivery[0]));
    }
}
