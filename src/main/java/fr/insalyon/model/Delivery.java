package fr.insalyon.model;

import java.util.*;

/**
 * 
 */
public class Delivery {

    /**
     * Default constructor
     */
    public Delivery() {
    }

    /**
     * 
     */
    private Courier courier;

    /**
     * 
     */
    private Intersection location;

    /**
     * 
     */
    private TimeWindow timeWindow;

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Intersection getLocation() {
        return location;
    }

    public void setLocation(Intersection location) {
        this.location = location;
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(TimeWindow timeWindow) {
        this.timeWindow = timeWindow;
    }
}