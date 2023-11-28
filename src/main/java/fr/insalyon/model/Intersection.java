package fr.insalyon.model;

import java.util.*;

/**
 * An intersection at the end of one or more segments
 * @see Segment
 */
public class Intersection {

    private int index;

    private final Long id;

    private float latitude;

    private float longitude;

    private Set<Segment> outwardSegments;

    /**
     * Construct a new intersection with no outwards segments
     * @param id identifier of the intersection from the XML file
     * @param latitude latitude of the intersection point
     * @param longitude longitude of the intersection point
     * @param index index in the intersection array of the CityMap class
     */
    public Intersection(Long id, float latitude, float longitude, int index) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outwardSegments = new HashSet<>();
        this.index = index;
    }

    /**
     * Construct a new intersection with some outwards segments
     * @param id identifier of the intersection from the XML file
     * @param latitude latitude of the intersection point
     * @param longitude longitude of the intersection point
     * @param index index in the map array of all intersection
     * @param outwardSegments the list of the segments leaving the intersection
     */
    public Intersection(Long id, float latitude, float longitude, int index, Set<Segment> outwardSegments) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outwardSegments = outwardSegments;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Long getId() {
        return id;
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