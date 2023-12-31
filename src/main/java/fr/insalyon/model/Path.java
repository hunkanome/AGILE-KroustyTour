package fr.insalyon.model;

import java.util.List;
import java.util.ArrayList;

/**
 * A path between two intersections containing one or several segments
 * @see Intersection
 * @see Segment
 */
public class Path {

    protected final Intersection start;

    protected final List<Segment> segments;

    protected final Intersection end;

    protected float length;

    /**
     * Default constructor
     * initialize the start and the end of the segments with null, the length with 0
     * and instantiate the segments array
     */
    public Path() {
        this.start = null;
        this.end = null;
        this.segments = new ArrayList<>();
        this.length = 0;
    }

    /**
     * Construct a path with a start and an end intersection
     * @param startIntersection starting intersection of the path
     * @param endIntersection arrival intersection of the path
     */
    public Path(Intersection startIntersection, Intersection endIntersection) {
        this.start = startIntersection;
        this.end = endIntersection;
        this.segments = new ArrayList<>();
        this.length = 0;
    }

    /**
     * Construct a path with a start intersection, an end intersection and a list of segments
     * @param startIntersection first intersection of the path
     * @param endIntersection last intersection of the path
     * @param segments list of the segments composing the path
     */
    public Path(Intersection startIntersection, Intersection endIntersection, List<Segment> segments) {
        this.start = startIntersection;
        this.end = endIntersection;
        this.segments = segments;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public float getLength() {
        return length;
    }

    /**
     * Append a segment at the end of the path
     * @param segment the segment to append
     */
    public void appendSegment(Segment segment) {
        this.segments.add(segment);
        this.length += segment.getLength();
    }

    @Override
    public String toString() {
        return "Path{" +
                "From I" + start.getId() +
                ", to I" + end.getId() +
                ", in " + length +
                ", meters through " + segments +
                '}';
    }
}