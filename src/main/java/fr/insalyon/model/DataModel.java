package fr.insalyon.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

	private final ObservableList<Tour> tours = FXCollections
			.observableArrayList(tour -> new Observable[] { tour.getDeliveriesList() });
	// we also observe the delivery list of each tour

	private List<Courier> couriers;

	private final ObjectProperty<Intersection> selectedIntersection = new SimpleObjectProperty<>(null);

	private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>(null);

	private final ObjectProperty<Delivery> selectedDelivery = new SimpleObjectProperty<>(null);

	/**
	 * Construct a new default data model.<br/>
	 * All collections are empty except the courier list which has an element. 
	 */
	public DataModel() {
		this.couriers = new ArrayList<>(1);
		this.couriers.add(new Courier(0));
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
		this.couriers.clear();
		Courier courier = new Courier(0);
		this.couriers.add(courier);
		this.tours.clear();
		Tour tour = new Tour(courier, map);
		this.tours.add(tour);
		this.selectedTour.set(tour);
		this.selectedIntersection.set(null);
		this.selectedDelivery.set(null);
		cityMapProperty().set(map);
	}

	public ObservableList<Tour> getTours() {
		return tours;
	}

	public void addTour(Tour tour) {
		tours.add(tour);
	}

	public void removeTour(Tour tour) {
		tours.remove(tour);
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
