package fr.insalyon.model;

import java.util.Objects;

/**
 * A courier than can carry out a delivery or a tour of deliveries
 * 
 * @see Delivery
 * @see Tour
 */
public class Courier {

	private static int nbCouriers;

	private final int id;

	/**
	 * Construct a new courier
	 */
	public Courier() {
		this.id = nbCouriers;
		nbCouriers++;
	}

	/**
	 * Construct a Courier with the given id
	 * @param id - the id of the Courier
	 */
	public Courier(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Courier other = (Courier) obj;
		return id == other.id;
	}
	
}