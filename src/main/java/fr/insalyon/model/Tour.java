package fr.insalyon.model;

import java.util.HashSet;
import java.util.Set;

/**
 * A sequence of deliveries located on a single path and handled by a courier.
 * @see Delivery
 * @see Courier
 * @see Path
 * @see CityMap
 */
public class Tour {

    private Set<Delivery> deliveries;

    private Courier courier;

    private CityMap map;

    private Path path;

    /**
     * Construct a new tour with no deliveries
     * Instantiate an empty set of deliveries and the path with an empty set of segments
     * The start intersection is the warehouse, the end intersection is set at null
     * @param courier The courier who handles the deliveries of the tour
     * @param map The map where the deliveries are located
     */
    public Tour(Courier courier, CityMap map) {
        this.courier = courier;
        this.map = map;
        this.deliveries = new HashSet<>();
        this.path = new Path(map.getWarehouse(), null);
    }

    public Set<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(Set<Delivery> deliveries) {
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