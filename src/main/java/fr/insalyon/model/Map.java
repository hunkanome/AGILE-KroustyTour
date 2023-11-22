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
    private List<Intersection> intersections;

    /**
     * 
     */
    private List<Segment> segments;


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

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }
}