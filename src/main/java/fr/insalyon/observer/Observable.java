package fr.insalyon.observer;

import java.util.ArrayList;
import java.util.Collection;

public class Observable {

	private Collection<Observer> obs;

	/**
	 * Construct a new observable object with an empty list of observers.
	 */
	public Observable() {
		this.obs = new ArrayList<>();
	}
	
	/**
	 * Add an observer to the list of observers.
	 * If the observer is already in the list, nothing happens.
	 * 
	 * @param o - the new observer to add
	 */
	public void addObserver(Observer o) {
		if (o != null && !obs.contains(o)) {
			obs.add(o);
		}
	}
	
	/**
	 * Notify all the observers of the observable object.
	 * The order of the notifications is not specified.
	 * 
	 * @param arg - the argument to pass to the observers
	 */
	public void notify(Object arg) {
		for (Observer o : obs) {
			o.update(this, arg);
		}
	}
 	
}
