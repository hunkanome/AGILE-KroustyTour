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

    public Intersection(Long id, float latitude, float longitude, int index) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outwardSegments = new HashSet<>();
        this.index = index;
    }

    public Intersection(Long id, float latitude, float longitude, Set<Segment> outwardSegments) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outwardSegments = outwardSegments;
    }

    /**
     *
     */
    private int index;

    /**
     * 
     */
    private Long id;

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

    public int getIndex() {
        return index;
    }

    public void setId(int index) {
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public void addOutwardSegment(Segment segment) {
        this.outwardSegments.add(segment);
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", outwardSegments=" + outwardSegments +
                ", index=" + index +
                '}';
    }
}