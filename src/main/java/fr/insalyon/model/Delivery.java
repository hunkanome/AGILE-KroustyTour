package fr.insalyon.model;

/**
 * A delivery at an intersection handled by a courier during a time window
 * @see Intersection
 * @see Courier
 * @see TimeWindow
 */
public class Delivery {

    private Courier courier;

    private Intersection location;

    private TimeWindow timeWindow;

    /**
     * Default constructor
     */
    public Delivery() {}

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