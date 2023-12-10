package fr.insalyon.model;

import fr.insalyon.algorithm.CityMapMatrix;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class used to store the data needed by the controllers It contains a city
 * map, a list of tours and a list of couriers.
 *
 * @see CityMapMatrix
 * @see Tour
 * @see Courier
 */
public class DataModel {
	private final ObjectProperty<CityMap> cityMap = new SimpleObjectProperty<>(null);

	private final ObservableList<Tour> tours = FXCollections
			.observableArrayList(tour -> new Observable[] { tour.getDeliveriesList() });
	// we also observe the delivery list of each tour

	private final ObjectProperty<Intersection> selectedIntersection = new SimpleObjectProperty<>(null);

	private final ObjectProperty<Tour> selectedTour = new SimpleObjectProperty<>(null);

	private final ObjectProperty<Delivery> selectedDelivery = new SimpleObjectProperty<>(null);

	/**
	 * Construct a new default data model.<br/>
	 * All collections are empty except the courier list which has an element.
	 */
	public DataModel() {
		// Initialization is already specified
	}

	/**
	 * Returns the property representing the city map.
	 *
	 * @return the property representing the city map matrix
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
	 * Sets the CityMapMatrix for the DataModel.
	 *
	 * @param map the CityMap to set
	 */
	public void setMap(CityMap map) {
		Courier courier = new Courier();
		this.tours.clear();
		CityMapMatrix cityMapMatrix = new CityMapMatrix(map);
		Tour tour = new Tour(courier, cityMapMatrix);
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
		if (tour == getSelectedTour()) {
			setSelectedTour(null);
		}
		tours.remove(tour);
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
