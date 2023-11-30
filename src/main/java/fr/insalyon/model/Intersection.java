package fr.insalyon.model;

import java.util.*;

import fr.insalyon.geometry.GeoCoordinates;

/**
 * An intersection at the end of one or more segments
 * @see Segment
 */
public class Intersection {

    private int index;

    private final Long id;

    private GeoCoordinates coordinates;

    private Set<Segment> outwardSegments;

    /**
     * Construct a new intersection with no outwards segments
     * @param id identifier of the intersection from the XML file
     * @param coordinates the coordinates of the intersection
     * @param index index in the intersection array of the CityMap class
     */
    public Intersection(Long id, GeoCoordinates coordinates, int index) {
        this.id = id;
        this.coordinates = coordinates;
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
    public Intersection(Long id, GeoCoordinates coordinates, int index, Set<Segment> outwardSegments) {
        this.id = id;
        this.coordinates = coordinates;
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

    public GeoCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoCoordinates coordinates) {
        this.coordinates = coordinates;
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
                ", coordinates=" + coordinates+
                ", outwardSegments=" + outwardSegments +
                ", index=" + index +
                '}';
    }
}