package fr.insalyon.model;

import java.util.*;

/**
 * 
 */
public class Intersection {

    /**
     * Default constructor
     */
    public Intersection() {
    }

    /**
     * 
     */
    private int id;

    /**
     * 
     */
    private float latitude;

    /**
     * 
     */
    private float longitude;

    /**
     * 
     */
    private Set<Segment> outwardSegments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public Set<Segment> getOutwardSegments() {
        return outwardSegments;
    }

    public void setOutwardSegments(Set<Segment> outwardSegments) {
        this.outwardSegments = outwardSegments;
    }
}