package fr.insalyon.model;

import java.util.ArrayList;
import java.util.List;

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

    private final ObservableList<Delivery> deliveriesList = FXCollections.observableArrayList();

    private Courier courier;

    private CityMap cityMap;

    private List<Path> pathList = new ArrayList<>();

    /**
     * Construct a new tour with no deliveries
     * Instantiate an empty list of deliveries and the path with an empty list of segments
     * The start intersection is the warehouse, the end intersection is set at null
     * @param courier The courier who handles the deliveries of the tour
     * @param map The map where the deliveries are located
     */
    public Tour(Courier courier, CityMap map) {
        this.courier = courier;
        this.cityMap = map;
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

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public CityMap getCityMap() {
        return cityMap;
    }

    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    public List<Path> getPathList() {
        return pathList;
    }

    public void setPathList(List<Path> pathList) {
        this.pathList = pathList;
    }
}