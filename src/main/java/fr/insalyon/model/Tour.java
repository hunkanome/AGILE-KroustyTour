package fr.insalyon.model;

import fr.insalyon.algorithm.DeliveryGraph;
import fr.insalyon.algorithm.Graph;
import fr.insalyon.algorithm.ShortestPathAlgorithm;
import fr.insalyon.algorithm.TSP1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * A sequence of deliveries located on a single path and handled by a courier.
 * @see Delivery
 * @see Courier
 * @see Path
 * @see CityMap
 */
public class Tour {

    private final ObservableList<Delivery> deliveriesList = FXCollections.observableArrayList();

    protected Courier courier;

    private List<Path> pathList = new ArrayList<>();

    /**
     * Construct a new tour with no deliveries
     * Instantiate an empty list of deliveries and the path with an empty list of segments
     * The start intersection is the warehouse, the end intersection is set at null
     */
    public Tour() {
        this.courier = new Courier();
    }

    public ObservableList<Delivery> getDeliveriesList() {
		return deliveriesList;
	}

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
        Delivery warehouseDelivery = new Delivery(this.courier, cityMap.getWarehouse(), TimeWindow.getTimeWindow(8));
        deliveries.add(0, warehouseDelivery);
        Graph graph = new DeliveryGraph(this.deliveriesList, cityMap, shortestPathAlgorithm);
        TSP1 tsp1 = new TSP1();

        // Grouping the deliveries by TimeWindow
        Map<TimeWindow, List<Integer>> deliveriesByTimeWindow = new HashMap<>();
        for (Delivery d : deliveries) {
            deliveriesByTimeWindow
                    .computeIfAbsent(d.getTimeWindow(), k -> new ArrayList<>())
                    .add(deliveries.indexOf(d));
        }
        // Computing TSP solution
        tsp1.searchSolution(5000, graph, deliveriesByTimeWindow);
        Integer[] solution = tsp1.getBestSol();

        List<Delivery> newDeliveries = new ArrayList<>(solution.length-1);
        List<Path> newPaths = new ArrayList<>(solution.length-1);

        if(solution.length > 1) {
            newDeliveries.add(deliveries.get(solution[1]));
        }
        for (int i=2; i<solution.length; i++) {
            newDeliveries.add(this.deliveriesList.get(i));
            newPaths.add(graph.getShortestPath(solution[i-1], solution[i]));
        }

        this.deliveriesList.setAll(newDeliveries);
        this.pathList = newPaths;

        System.out.println(Arrays.stream(solution).toList());
        System.out.println(deliveries);
        System.out.println(deliveriesList);
        System.out.println(pathList);


    }

    public Courier getCourier() {
        return courier;
    }

    public List<Path> getPathList() {
        return pathList;
    }
}