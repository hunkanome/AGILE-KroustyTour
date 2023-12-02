package fr.insalyon.observer;

public interface Observer {

	/**
	 * This method is called whenever the observed object is changed. An application
	 * calls an Observable object's notifyObservers method to have all the object's
	 * observers notified of the change.
	 * 
	 * @param o   - the observable object
	 * @param arg - an argument passed to the notifyObservers method. Can be null.
	 *            Can be the caller object.
	 */
	public void update(Observable o, Object arg);

}
