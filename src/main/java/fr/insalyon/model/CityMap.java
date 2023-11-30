package fr.insalyon.model;

import java.util.ArrayList;
import java.util.List;

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

    public Intersection getIntersectionById(Long id) throws IndexOutOfBoundsException {
        for (Intersection intersection : this.intersections) {
            if (intersection.getId().equals(id)) {
                return intersection;
            }
        }
        throw new IndexOutOfBoundsException();
    }

    /**
     * Used to obtain the maximum latitude of all intersections
     * @return the maximum latitude or Float.MIN_VALUE if there is no intersection
     */
    public float getMaxLatitude() {
        float max = Float.MIN_VALUE;
        for (Intersection intersection : this.intersections)
            if (intersection.getLatitude() > max)
                max = intersection.getLatitude();
        return max;
    }

    /**
     * Used to obtain the minimum latitude of all intersections
     * @return the minimum latitude or Float.MAX_VALUE if there is no intersection
     */
    public float getMinLatitude() {
        float min = Float.MAX_VALUE;
        for (Intersection intersection : this.intersections)
            if (intersection.getLatitude() < min)
                min = intersection.getLatitude();
        return min;
    }

    /**
     * Used to obtain the maximum longitude of all intersections
     * @return the maximum longitude or Float.MIN_VALUE if there is no intersection
     */
    public float getMaxLongitude() {
        float max = Float.MIN_VALUE;
        for (Intersection intersection : this.intersections)
            if (intersection.getLongitude() > max)
                max = intersection.getLongitude();
        return max;
    }

    /**
     * Used to obtain the minimum longitude of all intersections
     * @return the minimum longitude or Float.MAX_VALUE if there is no intersection
     */
    public float getMinLongitude() {
        float min = Float.MAX_VALUE;
        for (Intersection intersection : this.intersections)
            if (intersection.getLongitude() < min)
                min = intersection.getLongitude();
        return min;
    }

    @Override
    public String toString() {
        return "CityMap{" +
                "warehouse=" + warehouse +
                ", intersections=" + intersections +
                '}';
    }
}