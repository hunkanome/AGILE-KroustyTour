package fr.insalyon.model;

/**
 * A one-hour time interval during which a courier can make a delivery
 * @see Delivery
 * @see Courier
 */
public enum TimeWindow {

    EIGHT(8),
    NINE(9),
    TEN(10),
    ELEVEN(11);

    private int startHour;

    TimeWindow(int hour) {
        this.startHour = hour;
    }
}