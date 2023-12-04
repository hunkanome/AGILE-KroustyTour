package fr.insalyon.model;

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

    private List<Delivery> deliveries;

    private Courier courier;

    private CityMap map;

    private Path path;

    /**
     * Construct a new tour with no deliveries
     * Instantiate an empty list of deliveries and the path with an empty list of segments
     * The start intersection is the warehouse, the end intersection is set at null
     * @param courier The courier who handles the deliveries of the tour
     * @param map The map where the deliveries are located
     */
    public Tour(Courier courier, CityMap map) {
        this.courier = courier;
        this.map = map;
        this.deliveries = new ArrayList<>();
//        this.path = new Path(map.getWarehouse(), null); // TODO change this to null and verify that it works
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public CityMap getMap() {
        return map;
    }

    public void setMap(CityMap map) {
        this.map = map;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}