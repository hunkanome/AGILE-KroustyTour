package fr.insalyon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The map of a city or a part of a city.
 * It contains a list all intersections and the intersection warehouse is located
 * @see Intersection
 */
public class CityMap {

    private Intersection warehouse;

    private List<Intersection> intersections;

    /**
     * Default constructor
     * Instantiate an empty list of intersections
     */
    public CityMap() {
        this.intersections = new ArrayList<>();
    }

    public Intersection getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public void addIntersection(Intersection intersection) {
        this.intersections.add(intersection);
    }

    public Intersection getIntersectionById(Long id) {
        for (Intersection intersection : this.intersections) {
            if (intersection.getId().equals(id)) {
                return intersection;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "CityMap{" +
                "warehouse=" + warehouse +
                ", intersections=" + intersections +
                '}';
    }
}