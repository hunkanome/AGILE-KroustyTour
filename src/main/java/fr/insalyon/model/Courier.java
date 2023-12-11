package fr.insalyon.model;

/**
 * A courier than can carry out a delivery or a tour of deliveries
 * @see Delivery
 * @see Tour
 */
public class Courier {

    private static int nbCouriers;

    private final int id;

    /**
     * Construct a new courier
     */
    public Courier() {
        this.id = nbCouriers;
        nbCouriers++;
    }

    public int getId() {
        return id;
    }
}