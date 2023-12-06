package fr.insalyon.model;

import java.time.Duration;

/**
 * A delivery at an intersection handled by a courier during a time window
 * @see Intersection
 * @see Courier
 * @see TimeWindow
 */
public class Delivery {
    public static final Duration DURATION = Duration.ofMinutes(5);

	private Courier courier;

    private Intersection location;

    private TimeWindow timeWindow;

    /**
     * Construct a new delivery
     * @param courier The courier handling the delivery
     * @param location The intersection where delivery is requested
     * @param timeWindow The time window during which delivery must be made
     */
    public Delivery(Courier courier, Intersection location, TimeWindow timeWindow) {
        this.courier = courier;
        this.location = location;
        this.timeWindow = timeWindow;
    }


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