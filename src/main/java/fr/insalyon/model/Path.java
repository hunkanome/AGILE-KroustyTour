package fr.insalyon.model;

import java.util.*;

/**
 * 
 */
public class Path {

    /**
     * Default constructor
     */
    public Path() {
    }

    /**
     * 
     */
    private Intersection start;

    /**
     * 
     */
    private Set<Segment> segments;

    /**
     * 
     */
    private Intersection end;

    public Intersection getStart() {
        return start;
    }

    public void setStart(Intersection start) {
        this.start = start;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    public void setSegments(Set<Segment> segments) {
        this.segments = segments;
    }

    public Intersection getEnd() {
        return end;
    }

    public void setEnd(Intersection end) {
        this.end = end;
    }
}