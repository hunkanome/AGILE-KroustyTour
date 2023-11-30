package fr.insalyon.model;

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

    public Intersection getIntersectionById(Long id) throws IndexOutOfBoundsException {
        for (Intersection intersection : this.intersections) {
            if (intersection.getId().equals(id)) {
                return intersection;
            }
        }
        throw new IndexOutOfBoundsException();
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