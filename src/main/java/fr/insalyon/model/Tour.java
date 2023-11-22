package fr.insalyon.model;

import java.util.*;

/**
 * 
 */
public class Tour {

    /**
     * Default constructor
     */
    public Tour() {
    }

    /**
     * 
     */
    private Set<Delivery> deliveries;

    /**
     * 
     */
    private Courier courier;

    /**
     * 
     */
    private Map map;

    /**
     * 
     */
    private Path path;

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

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }
}