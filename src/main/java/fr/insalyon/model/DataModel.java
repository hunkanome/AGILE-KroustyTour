package fr.insalyon.model;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Class used to store the data needed by the controllers It contains a city
 * map, a list of tours
 * @see Tour
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
	 * Returns the property representing the city map.
	 * @return the property representing the city map matrix
	 */
	public ObjectProperty<CityMap> cityMapProperty() {
		return this.cityMap;
	}

	public CityMap getCityMap() {
		return cityMapProperty().get();
	}

	public void setMap(CityMap map) {
		this.tours.clear();
		Courier courier = new Courier();
		Tour tour = new Tour(courier);
		this.tours.add(tour);
		this.selectedTour.set(tour);
		this.selectedIntersection.set(null);
		this.selectedDelivery.set(null);
		cityMapProperty().set(map);
	}

	public ObservableList<Tour> getTours() {
		return tours;
	}

	/**
	 * Adds a tour to the list of tours.
	 * @param tour the tour to be added
	 */
	public void addTour(Tour tour) {
		tours.add(tour);
	}

	/**
	 * Removes a tour from the list of tours.
	 * And resets the selected tour if it was the one removed.
	 * @param tour the tour to be removed
	 */
	public void removeTour(Tour tour) {
		if (tour == getSelectedTour()) {
			setSelectedTour(null);
		}
		tours.remove(tour);
	}

	/**
	 * Returns the property representing the selected intersection.
	 * @return the property representing the selected intersection
	 */
	public ObjectProperty<Intersection> selectedIntersectionProperty() {
		return selectedIntersection;
	}

	public final Intersection getSelectedIntersection() {
		return selectedIntersectionProperty().get();
	}

	public final void setSelectedIntersection(Intersection intersection) {
		selectedIntersectionProperty().set(intersection);
	}

	/**
	 * Returns the property representing the selected tour.
	 * @return the property representing the selected tour
	 */
	public ObjectProperty<Tour> selectedTourProperty() {
		return selectedTour;
	}

	public final Tour getSelectedTour() {
		return selectedTourProperty().get();
	}

	public final void setSelectedTour(Tour tour) {
		selectedTourProperty().set(tour);
		selectedDeliveryProperty().set(null);
	}

	/**
	 * Returns the property representing the selected delivery.
	 * @return the property representing the selected delivery
	 */
	public ObjectProperty<Delivery> selectedDeliveryProperty() {
		return selectedDelivery;
	}

	public final Delivery getSelectedDelivery() {
		return selectedDeliveryProperty().get();
	}

	public final void setSelectedDelivery(Delivery delivery) {
		selectedDeliveryProperty().set(delivery);
		for (Tour tour : getTours()) {
			if (tour.getDeliveriesList().contains(delivery)) {
				selectedTourProperty().set(tour);
				return;
			}
		}
	}

}
