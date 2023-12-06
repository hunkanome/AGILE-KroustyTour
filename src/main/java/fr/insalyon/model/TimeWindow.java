package fr.insalyon.model;

import java.util.Objects;

/**
 * A one-hour time interval during which a courier can make a delivery
 * @see Delivery
 * @see Courier
 */
public class TimeWindow {
    private final int startHour;

    private static final int[] possibleTimeWindows = {8, 9, 10, 11};

    public TimeWindow(int hour) {
        for (int tw : possibleTimeWindows) {
            if (tw == hour) {
                this.startHour = hour;
                return;
            }
        }
        throw new IllegalArgumentException("Invalid hour for time window");
    }

    public int getStartHour() {
        return startHour;
    }

    public boolean isRightBefore(TimeWindow other) {
        return this.startHour + 1 == other.startHour;
    }

    public boolean isBefore(TimeWindow other) {
        return this.startHour < other.startHour;
    }

    public boolean isAfter(TimeWindow other) {
        return this.startHour > other.startHour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.startHour);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TimeWindow other = (TimeWindow) obj;
        return this.startHour == other.startHour;
    }
}