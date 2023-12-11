package fr.insalyon.model;

import java.time.Duration;

/**
 * A delivery at an intersection during a time window
 * @see Intersection
 * @see TimeWindow
 */
public class Delivery {
    public static final Duration DURATION = Duration.ofMinutes(5);

    private final Intersection location;

    private final TimeWindow timeWindow;

    /**
     * Construct a new delivery
     * @param location The intersection where delivery is requested
     * @param timeWindow The time window during which delivery must be made
     */
    public Delivery(Intersection location, TimeWindow timeWindow) {
        this.location = location;
        this.timeWindow = timeWindow;
    }

    public Intersection getLocation() {
        return location;
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }
}