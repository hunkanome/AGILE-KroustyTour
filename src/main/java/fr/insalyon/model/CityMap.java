package fr.insalyon.model;

import fr.insalyon.geometry.GeoCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The map of a city or a part of a city.
 * It contains a list all intersections and the intersection warehouse is located
 * @see Intersection
 */
public class CityMap {

    private Intersection warehouse;

    private List<Intersection> intersections;

    /**
     * Default constructor
     * Instantiate an empty list of intersections
     */
    public CityMap() {
        this.intersections = new ArrayList<>();
    }

    public Intersection getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public void addIntersection(Intersection intersection) {
        this.intersections.add(intersection);
    }

    /**
     * Get an intersection using an id
     * @param id the id of the intersection
     * @return the intersection corresponding to the id
     * @throws IndexOutOfBoundsException if no intersection is found
     */
    public Intersection getIntersectionById(long id) throws IndexOutOfBoundsException {
        for (Intersection intersection : this.intersections) {
            if (intersection.getId() == id) {
                return intersection;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Used to obtain the most north-west coordinates of all intersections
     * @return the geoCoordinate with the maxLat and minLong
     */
    public GeoCoordinates getNorthWestMostCoordinates() {
    	return new GeoCoordinates(this.getMaxLatitude(), this.getMinLongitude());
    }

    /**
     * Used to obtain the most south-east coordinates of all intersections
     * @return the geoCoordinate with the minLat and maxLong
     */
    public GeoCoordinates getSouthEastMostCoordinates() {
    	return new GeoCoordinates(this.getMinLatitude(), this.getMaxLongitude());
    }
    
    /**
     * Used to obtain the maximum latitude of all intersections
     * @return the maximum latitude or Float.MIN_VALUE if there is no intersection
     */
    public float getMaxLatitude() {
        float max = Float.MIN_VALUE;
        for (Intersection intersection : this.intersections)
            if (intersection.getCoordinates().getLatitude() > max)
                max = intersection.getCoordinates().getLatitude();
        return max;
    }

    /**
     * Used to obtain the minimum latitude of all intersections
     * @return the minimum latitude or Float.MAX_VALUE if there is no intersection
     */
    public float getMinLatitude() {
        float min = Float.MAX_VALUE;
        for (Intersection intersection : this.intersections)
            if (intersection.getCoordinates().getLatitude() < min)
                min = intersection.getCoordinates().getLatitude();
        return min;
    }

    /**
     * Used to obtain the maximum longitude of all intersections
     * @return the maximum longitude or Float.MIN_VALUE if there is no intersection
     */
    public float getMaxLongitude() {
        float max = Float.MIN_VALUE;
        for (Intersection intersection : this.intersections)
            if (intersection.getCoordinates().getLongitude() > max)
                max = intersection.getCoordinates().getLongitude();
        return max;
    }

    /**
     * Used to obtain the minimum longitude of all intersections
     * @return the minimum longitude or Float.MAX_VALUE if there is no intersection
     */
    public float getMinLongitude() {
        float min = Float.MAX_VALUE;
        for (Intersection intersection : this.intersections)
            if (intersection.getCoordinates().getLongitude() < min)
                min = intersection.getCoordinates().getLongitude();
        return min;
    }

    @Override
    public String toString() {
        return "CityMap{" +
                "warehouse=" + warehouse +
                ", intersections=" + intersections +
                '}';
    }

	@Override
	public int hashCode() {
		return Objects.hash(intersections, warehouse);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CityMap other = (CityMap) obj;
		return Objects.equals(intersections, other.intersections) && Objects.equals(warehouse, other.warehouse);
	}
    
    
}