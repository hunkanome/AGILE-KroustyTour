package fr.insalyon.model;

/**
 * A courier than can carry out a delivery or a tour of deliveries
 * @see Delivery
 * @see Tour
 */
public class Courier {

    private static int nbCouriers;

    private final int id;

    private boolean available;

    /**
     * Construct a new courier
     * Define that he is available
     */
    public Courier() {
        this.available = true;
        this.id = nbCouriers;
        nbCouriers++;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}