package fr.insalyon.model;

import java.util.*;

/**
 * 
 */
public class Map {

    /**
     * Default constructor
     */
    public Map() {
        this.intersections = new HashSet<>();
    }

    /**
     * 
     */
    private Intersection warehouse;

    /**
     * 
     */
    private Set<Intersection> intersections;


    public Intersection getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public Set<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(Set<Intersection> intersections) {
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