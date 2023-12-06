package fr.insalyon.geometry;

import java.util.Objects;

/**
 * Represents geographical coordinates with latitude and longitude values.
 * Differs from Position as the coordinates are in a spherical coordinate system
 * and not in a Cartesian system.
 */
public class GeoCoordinates {

	private Float latitude;

	private Float longitude;

	/**
	 * Creates an empty instance of GeoCoordinates with null values
	 */
	public GeoCoordinates() {
	}

	/**
	 * Creates an instance of GeoCoordinates with the given latitude and longitude
	 * 
	 * @param latitude  - the latitude of the point
	 * @param longitude - the longitude of the point
	 */
	public GeoCoordinates(Float latitude, Float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Gets the latitude coordinate
	 * 
	 * @return latitude - the latitude coordinate
	 */
	public Float getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude coordinate
	 * 
	 * @param latitude - the latitude coordinate
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude coordinate
	 * 
	 * @return longitude - the longitude coordinate
	 */
	public Float getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude coordinate
	 * 
	 * @param longitude - the longitude coordinate
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		return Objects.hash(latitude, longitude);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeoCoordinates other = (GeoCoordinates) obj;
		return Objects.equals(latitude, other.latitude) && Objects.equals(longitude, other.longitude);
	}

	@Override
	public String toString() {
		return "GeoCoordinates{"
				+ "latitude=" + latitude + ", longitude=" + longitude + "}";
	}
	
}
