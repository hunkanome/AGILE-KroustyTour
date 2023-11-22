package fr.insalyon.model;

import java.util.*;

/**
 * 
 */
public class Segment {

    /**
     * Default constructor
     */
    public Segment() {
    }

    /**
     * 
     */
    private Intersection origin;

    /**
     * 
     */
    private Intersection destination;

    /**
     * 
     */
    private String name;

    /**
     * 
     */
    private float length;

    public Intersection getOrigin() {
        return origin;
    }

    public void setOrigin(Intersection origin) {
        this.origin = origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }
}