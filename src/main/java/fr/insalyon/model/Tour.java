package fr.insalyon.model;

import fr.insalyon.algorithm.CityMapMatrix;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * A sequence of deliveries located on a single path and handled by a courier.
 * @see Delivery
 * @see Courier
 * @see Path
 * @see CityMap
 */
public class Tour {

    private final ObservableList<Delivery> deliveriesList = FXCollections.observableArrayList();

    private Courier courier;

    private CityMapMatrix cityMapMatrix;

    private List<Path> pathList = new ArrayList<>();

    public Tour() {
    }

    public Tour(CityMapMatrix cityMapMatrix){
        this.cityMapMatrix = cityMapMatrix;
        this.courier = new Courier();
    }

    /**
     * Construct a new tour with no deliveries
     * Instantiate an empty list of deliveries and the path with an empty list of segments
     * The start intersection is the warehouse, the end intersection is set at null
     * @param courier The courier who handles the deliveries of the tour
     * @param mapMatrix The map where the deliveries are located
     */
    public Tour(Courier courier, CityMapMatrix mapMatrix) {
        this.courier = courier;
        this.cityMapMatrix = mapMatrix;
    }

    public ObservableList<Delivery> getDeliveriesList() {
		return deliveriesList;
	}

    public void addDelivery(Delivery delivery) {
        // TODO : recalculate the path and reorder the deliveries
        this.deliveriesList.add(delivery);
    }

    public Courier getCourier() {
        return courier;
    }

    public CityMapMatrix getCityMapMatrix() {
        return cityMapMatrix;
    }

    public void setCityMap(CityMapMatrix cityMapMatrix) {
        this.cityMapMatrix = cityMapMatrix;
    }

    public List<Path> getPathList() {
        return pathList;
    }

    public void setPathList(List<Path> pathList) {
        this.pathList = pathList;
    }
}