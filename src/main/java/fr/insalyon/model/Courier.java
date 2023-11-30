package fr.insalyon.model;

/**
 * A courier than can carry out a delivery or a tour of deliveries
 * @see Delivery
 * @see Tour
 */
public class Courier {

    private int id;

    private boolean available;

    /**
     * Construct a new courier
     * Define that he is available
     * @param id the courier id
     */
    public Courier(int id) {
        this.id = id;
        this.available = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}