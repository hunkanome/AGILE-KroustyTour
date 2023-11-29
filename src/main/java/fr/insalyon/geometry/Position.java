package fr.insalyon.geometry;

/**
 * Represents a position in a Cartesian coordinate system. It is composed of two
 * coordinates: x and y.
 * 
 * Differs from GeoCoordinates as the coordinates are in a Cartesian coordinate
 * system and not in a spherical system.
 */
public class Position {

	private Float x;

	private Float y;

	/**
	 * Creates an empty instance of Position with null values
	 */
	public Position() {
	}

	/**
	 * Creates an instance of Position with the given x and y coordinates
	 * 
	 * @param x - the x coordinate of the point
	 * @param y - the y coordinate of the point
	 */
	public Position(Float x, Float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x coordinate
	 * 
	 * @return x - the x coordinate
	 */
	public Float getX() {
		return x;
	}

	/**
	 * Sets the x coordinate
	 * 
	 * @param x - the x coordinate
	 */
	public void setX(Float x) {
		this.x = x;
	}

	/**
	 * Gets the y coordinate
	 * 
	 * @return y - the y coordinate
	 */
	public Float getY() {
		return y;
	}

	/**
	 * Sets the y coordinate
	 * 
	 * @param y - the y coordinate
	 */
	public void setY(Float y) {
		this.y = y;
	}

}
