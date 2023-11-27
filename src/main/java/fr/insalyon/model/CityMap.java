package fr.insalyon.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class CityMap {

    /**
     * Default constructor
     */
    public CityMap() {
        this.intersections = new ArrayList<>();
    }

    /**
     * 
     */
    private Intersection warehouse;

    /**
     * 
     */
    private List<Intersection> intersections;


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
        return "Map{" +
                "warehouse=" + warehouse +
                ", intersections=" + intersections +
                '}';
    }
}