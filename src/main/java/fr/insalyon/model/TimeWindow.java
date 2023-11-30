package fr.insalyon.model;

/**
 * A one-hour time interval during which a courier can make a delivery
 * @see Delivery
 * @see Courier
 */
public class TimeWindow {
    private int startHour;

    public TimeWindow(int hour) {
        int[] possibleTimeWindows = {8, 9, 10, 11};
        for (final int tw : possibleTimeWindows) {
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

    public boolean equals(TimeWindow other) {
        return this.startHour == other.startHour;
    }
}