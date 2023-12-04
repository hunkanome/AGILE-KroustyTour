package fr.insalyon.model;

import java.util.ArrayList;
import java.util.List;

import fr.insalyon.observer.Observable;
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
public class DataModel extends Observable {
// TODO use javafx.beans observables
	private CityMap map;

	private List<Tour> tours;

	private List<Courier> couriers;

	private Intersection selectedIntersection;

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
	 * @param map The stored map
	 * @see CityMap
	 * @see Courier
	 * @see Tour
	 */
	public DataModel(CityMap map) {
		this.map = map;
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
	public DataModel(CityMap map, int nbCouriers) {
		this.map = map;
		this.tours = new ArrayList<>();
		this.couriers = new ArrayList<>(nbCouriers);

		for (int i = 0; i < nbCouriers; i++) {
			this.couriers.add(new Courier(i));
		}
	}

	public CityMap getMap() {
		return map;
	}

	/**
	 * Sets the map with the given one.<br/>
	 * Notifies all the observers of the update of the map
	 * 
	 * @param map - the map to set
	 */
	public void setMap(CityMap map) {
		// TODO reset the whole model ?
		this.map = map;
		this.notify(map);
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

	public Intersection getSelectedIntersection() {
		return selectedIntersection;
	}

	public void setSelectedIntersection(Intersection selectedIntersection) {
		this.selectedIntersection = selectedIntersection;
		this.notify(selectedIntersection);
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

	public ObjectProperty<Tour> selectedTourProperty() {
		return selectedTour;
	}

	public final Tour getSelectedTour() {
		return selectedTourProperty().get();
	}

	public final void setSelectedTour(Tour tour) {
		selectedTourProperty().set(tour);
	}

	public ObjectProperty<Delivery> selectedDeliveryProperty() {
		return selectedDelivery;
	}

	public final Delivery getSelectedDelivery() {
		return selectedDeliveryProperty().get();
	}

	public final void setSelectedDelivery(Delivery delivery) {
		selectedDeliveryProperty().set(delivery);
	}

}
