package fr.insalyon.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Class used to store the data needed by the controllers It contains a city
 * map, a list of tours and a list of couriers.
 * 
 * @see CityMap
 * @see Tour
 * @see Courier
 */
public class DataModel {
// TODO use javafx.beans observables for lists
	private final ObjectProperty<CityMap> cityMap = new SimpleObjectProperty<>(null);

	private List<Tour> tours;

	private List<Courier> couriers;

	private final ObjectProperty<Intersection> selectedIntersection = new SimpleObjectProperty<>(null);

	private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>(null);

	private final ObjectProperty<Delivery> selectedDelivery = new SimpleObjectProperty<>(null);

	/**
	 * Construct a new empty data model
	 */
	public DataModel() {
	}

	/**
	 * Construct a new data model Initialize an empty list of tours and a list
	 * containing one courier
	 * 
	 * @param cityMap The stored map
	 * @see CityMap
	 * @see Courier
	 * @see Tour
	 */
	public DataModel(CityMap cityMap) {
		this.cityMap.set(cityMap);
		this.tours = new ArrayList<>();
		this.couriers = new ArrayList<>(1);
		this.couriers.add(new Courier(0));
	}

	/**
	 * Construct a new data model Initialize an empty list of tours and a list of
	 * nbCouriers couriers
	 * 
	 * @param map The stored map
	 * @see CityMap
	 * @see Courier
	 * @see Tour
	 */
	public DataModel(CityMap cityMap, int nbCouriers) {
		this.cityMap.set(cityMap);
		this.tours = new ArrayList<>();
		this.couriers = new ArrayList<>(nbCouriers);

		for (int i = 0; i < nbCouriers; i++) {
			this.couriers.add(new Courier(i));
		}
	}

	/**
	 * Returns the property representing the city map.
	 *
	 * @return the property representing the city map
	 */
	public ObjectProperty<CityMap> cityMapProperty() {
		return this.cityMap;
	}
	
	/**
	 * Returns the CityMap object.
	 *
	 * @return the CityMap object.
	 */
	public CityMap getCityMap() {
		return cityMapProperty().get();
	}

	/**
	 * Sets the CityMap for the DataModel.
	 * 
	 * @param map the CityMap to set
	 */
	public void setMap(CityMap map) {
		// TODO reset the whole model ?
		cityMapProperty().set(map);
	}

	public List<Tour> getTours() {
		return tours;
	}

	public void setTours(List<Tour> tours) {
		this.tours = tours;
	}

	public List<Courier> getCouriers() {
		return couriers;
	}

	public void setCouriers(List<Courier> couriers) {
		this.couriers = couriers;
	}

	/**
	 * Get an available couriers
	 * 
	 * @return a courier if one is available, null otherwise
	 * @see Courier
	 */
	public Courier getAvailableCourier() {
		for (Courier courier : couriers) {
			if (courier.isAvailable()) {
				return courier;
			}
		}
		return null;
	}

	/**
	 * Returns the property representing the selected intersection.
	 *
	 * @return the property representing the selected intersection
	 */
	public ObjectProperty<Intersection> selectedIntersectionProperty() {
		return selectedIntersection;
	}

	/**
	 * Returns the selected intersection.
	 *
	 * @return The selected intersection.
	 */
	public final Intersection getSelectedIntersection() {
		return selectedIntersectionProperty().get();
	}

	/**
	 * Sets the selected intersection.
	 * 
	 * @param intersection the intersection to be set as selected
	 */
	public final void setSelectedIntersection(Intersection intersection) {
		selectedIntersectionProperty().set(intersection);
	}

	/**
	 * Returns the property representing the selected tour.
	 *
	 * @return the property representing the selected tour
	 */
	public ObjectProperty<Tour> selectedTourProperty() {
		return selectedTour;
	}

	/**
	 * Returns the selected tour.
	 *
	 * @return the selected tour
	 */
	public final Tour getSelectedTour() {
		return selectedTourProperty().get();
	}

	/**
	 * Sets the selected tour.
	 * 
	 * @param tour the tour to be set as selected
	 */
	public final void setSelectedTour(Tour tour) {
		selectedTourProperty().set(tour);
	}

	/**
	 * Returns the property representing the selected delivery.
	 *
	 * @return the property representing the selected delivery
	 */
	public ObjectProperty<Delivery> selectedDeliveryProperty() {
		return selectedDelivery;
	}

	/**
	 * Returns the selected delivery.
	 *
	 * @return The selected delivery.
	 */
	public final Delivery getSelectedDelivery() {
		return selectedDeliveryProperty().get();
	}

	/**
	 * Sets the selected delivery.
	 * 
	 * @param delivery the delivery to be set as selected
	 */
	public final void setSelectedDelivery(Delivery delivery) {
		selectedDeliveryProperty().set(delivery);
	}

}
