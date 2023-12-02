package fr.insalyon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to store the data needed by the controllers
 * It contains a city map, a list of tours and a list of couriers.
 * @see CityMap
 * @see Tour
 * @see Courier
 */
public class DataModel {

    private CityMap map;

    private List<Tour> tours;

    private List<Courier> couriers;
    
    /**
     * Construct a new empty data model
     */
    public DataModel() {
	}

	/**
     * Construct a new data model
     * Initialize an empty list of tours and a list containing one courier
     * @param map The stored map
     * @see CityMap
     * @see Courier
     * @see Tour
     */
    public DataModel(CityMap map) {
        this.map = map;
        this.tours = new ArrayList<>();
        this.couriers = new ArrayList<>(1);
        this.couriers.add(new Courier(0));
    }

    /**
     * Construct a new data model
     * Initialize an empty list of tours and a list of nbCouriers couriers
     * @param map The stored map
     * @see CityMap
     * @see Courier
     * @see Tour
     */
    public DataModel(CityMap map, int nbCouriers) {
        this.map = map;
        this.tours = new ArrayList<>();
        this.couriers = new ArrayList<>(nbCouriers);

        for(int i=0; i<nbCouriers; i++) {
            this.couriers.add(new Courier(i));
        }
    }

    public CityMap getMap() {
        return map;
    }

    public void setMap(CityMap map) {
        this.map = map;
    }

    public List<Tour> getTours() {
        return tours;
    }

    public void setTours(List<Tour> tours) {
        this.tours = tours;
    }

    public List<Courier> getCouriers() {
        return couriers;
    }

    public void setCouriers(List<Courier> couriers) {
        this.couriers = couriers;
    }

    /**
     * Get an available couriers
     * @return a courier if one is available, null otherwise
     * @see Courier
     */
    public Courier getAvailableCourier() {
        for (Courier courier : couriers) {
            if (courier.isAvailable()) {
                return courier;
            }
        }
        return null;
    }
}
