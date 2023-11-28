package fr.insalyon.model;

import java.util.*;

/**
 * 
 */
public class Path {


    public Path() {
        this.start = null;
        this.end = null;
        this.segments = new ArrayList<>();
        this.length = 0;
    }

    /**
     * Default constructor
     */
    public Path(Intersection startIntersection, Intersection endIntersection) {
        this.start = startIntersection;
        this.end = endIntersection;
        this.segments = new ArrayList<>();
        this.length = 0;
    }

    /**
     * Constructor with set of segments
     */
    public Path(Intersection startIntersection, Intersection endIntersection, List<Segment> segments) {
        this.start = startIntersection;
        this.end = endIntersection;
        this.segments = segments;
    }

    /**
     * 
     */
    private Intersection start;

    /**
     * 
     */
    private List<Segment> segments;

    /**
     * 
     */
    private Intersection end;

    /**
     *
     */
    private float length;

    public Intersection getStart() {
        return start;
    }

    public void setStart(Intersection start) {
        this.start = start;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public Intersection getEnd() {
        return end;
    }

    public void setEnd(Intersection end) {
        this.end = end;
    }

    public float getLength() { return length; }

    public void setLength(float length) { this.length = length; }

    public void appendSegment(Segment segment) {
        this.segments.add(segment);
        this.length += segment.getLength();
    }

    @Override
    public String toString() {
        return "Path{" +
                "start=" + start.getId() +
                ", segments=" + segments +
                ", end=" + end.getId() +
                ", length=" + length +
                '}';
    }
}