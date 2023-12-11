package fr.insalyon.model;

import java.util.Objects;

/**
 * A segment of a road between two intersections.
 * The direction of a segment matters, it is not bidirectionnal
 * @see Intersection
 */
public class Segment {

    private final Intersection origin;

    private final Intersection destination;

    private final String name;

    private final float length;

    /**
     * Construct a segment
     * @param origin starting intersection of the segment
     * @param destination arrival intersection of the segment
     * @param name name of the street
     * @param length length of the segment
     */
    public Segment(Intersection origin, Intersection destination, String name, float length) {
        this.origin = origin;
        this.destination = destination;
        this.name = name;
        this.length = length;
    }

    public Intersection getOrigin() {
        return origin;
    }

    public Intersection getDestination() {
        return destination;
    }

    public String getName() {
        return name;
    }

    public float getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "originID=" + origin.getId() +
                ", destinationID=" + destination.getId() +
                ", name='" + name + '\'' +
                ", length=" + length +
                '}';
    }

	@Override
	public int hashCode() {
		return Objects.hash(destination, length, name, origin);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Segment other = (Segment) obj;
		return Objects.equals(destination, other.destination)
				&& Float.floatToIntBits(length) == Float.floatToIntBits(other.length)
				&& Objects.equals(name, other.name) && Objects.equals(origin, other.origin);
	}
}