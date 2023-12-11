package fr.insalyon.model;

import fr.insalyon.geometry.GeoCoordinates;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * An intersection at the end of one or more segments
 * 
 * @see Segment
 */
public class Intersection {

	private final int index;

	private final long id;

	private final GeoCoordinates coordinates;

	private final Set<Segment> outwardSegments;

	/**
	 * Construct a new intersection with no outwards segments
	 * 
	 * @param id          identifier of the intersection from the XML file
	 * @param coordinates the coordinates of the intersection
	 * @param index       index in the intersection array of the CityMap class
	 */
	public Intersection(long id, GeoCoordinates coordinates, int index) {
		this.id = id;
		this.coordinates = coordinates;
		this.outwardSegments = new HashSet<>();
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	
	public long getId() {
		return id;
	}

	public GeoCoordinates getCoordinates() {
		return coordinates;
	}
	
	public Set<Segment> getOutwardSegments() {
		return outwardSegments;
	}

	public void addOutwardSegment(Segment segment) {
        this.outwardSegments.add(segment);
	}

	@Override
	public String toString() {
		return "Intersection{" + "id=" + id + ", coordinates=" + coordinates + ", outwardSegments=" + outwardSegments
				+ ", index=" + index + '}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(coordinates, id, index, outwardSegments);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Intersection other = (Intersection) obj;
		/* we don't compare outward segments to avoid recursion and stackoverflow */
		return Objects.equals(coordinates, other.coordinates) && Objects.equals(id, other.id);
	}

}