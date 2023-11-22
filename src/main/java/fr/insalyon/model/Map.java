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
    }

    /**
     * 
     */
    private Intersection warehouse;

    /**
     * 
     */
    private Set<Intersection> intersections;

    /**
     * 
     */
    private Set<Segment> segments;


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

    public Set<Segment> getSegments() {
        return segments;
    }

    public void setSegments(Set<Segment> segments) {
        this.segments = segments;
    }
}