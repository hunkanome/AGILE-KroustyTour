package fr.insalyon.model;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insalyon.algorithm.DeliveryGraph;
import fr.insalyon.algorithm.Graph;
import fr.insalyon.algorithm.ShortestPathAlgorithm;
import fr.insalyon.algorithm.TSP1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * A sequence of deliveries located on a single path and handled by a courier.
 * @see Delivery
 * @see Courier
 * @see Path
 * @see CityMap
 */
public class Tour {
    private static final int MAX_TSP_TIME = 10000;
    public static final LocalTime TOUR_START = LocalTime.of(8, 0);

    private final ObservableList<Delivery> deliveriesList = FXCollections.observableArrayList();

    protected final Courier courier;

    private List<Path> pathList = new ArrayList<>();

    /**
     * Construct a new tour with no deliveries
     * Instantiate an empty list of deliveries and the path with an empty list of segments
     * The start intersection is the warehouse, the end intersection is set at null
     * @param courier the courier handling the deliveries
     */
    public Tour(Courier courier){
        this.courier = courier;
    }

    public ObservableList<Delivery> getDeliveriesList() {
		return deliveriesList;
	}

    /**
     * Add a delivery to the tour and recalculate the graph
     * @param delivery the delivery to add
     * @param cityMap the city map used to recalculate the graph
     * @param shortestPathAlgorithm the algorithm used to recalculate the graph
     */
    public void addDelivery(Delivery delivery, CityMap cityMap, ShortestPathAlgorithm shortestPathAlgorithm) {
        this.deliveriesList.add(delivery);
        recalculateGraph(cityMap, shortestPathAlgorithm);
    }

    public void removeDelivery(Delivery delivery, CityMap cityMap, ShortestPathAlgorithm shortestPathAlgorithm) {
        this.deliveriesList.remove(delivery);
        recalculateGraph(cityMap, shortestPathAlgorithm);
    }

    private void recalculateGraph(CityMap cityMap, ShortestPathAlgorithm shortestPathAlgorithm){
        List<Delivery> deliveries = new ArrayList<>(this.deliveriesList);
        Delivery warehouseDelivery = new Delivery(cityMap.getWarehouse(), TimeWindow.getTimeWindow(8));
        deliveries.add(0, warehouseDelivery);
        Graph graph = new DeliveryGraph(deliveries, cityMap, shortestPathAlgorithm);
        TSP1 tsp1 = new TSP1();

        // Grouping the deliveries by TimeWindow
        Map<TimeWindow, List<Integer>> deliveriesByTimeWindow = new HashMap<>();
        for (Delivery d : deliveries) {
            deliveriesByTimeWindow
                    .computeIfAbsent(d.getTimeWindow(), k -> new ArrayList<>())
                    .add(deliveries.indexOf(d));
        }
        // Computing TSP solution
        tsp1.searchSolution(MAX_TSP_TIME, graph, deliveriesByTimeWindow);
        Integer[] solution = tsp1.getBestSol();

        List<Delivery> newDeliveries = new ArrayList<>(solution.length - 1);
        List<Path> newPaths = new ArrayList<>(solution.length - 1);
        
        for (int i = 1; i < solution.length; i++) {
            newDeliveries.add(this.deliveriesList.get(solution[i] - 1));
            newPaths.add(graph.getShortestPath(solution[i - 1], solution[i]));
        }

        this.pathList = newPaths;
        this.deliveriesList.setAll(newDeliveries);

    }

    public Courier getCourier() {
        return courier;
    }

    public List<Path> getPathList() {
        return pathList;
    }
}