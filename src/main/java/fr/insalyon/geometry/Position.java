package fr.insalyon.geometry;

import java.util.Objects;

/**
 * Represents a position in a Cartesian coordinate system. It is composed of two
 * coordinates: x and y.
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

	/**
	 * Gets the distance between the current position and the given position
	 * @param other - the position to calculate the distance with
	 * @return the distance between the current position and the given position
	 */
	public Float distanceTo(Position other) {
		return (float) Math.sqrt(Math.pow(this.x - other.x, 2) + Math.pow(this.y - other.y, 2));
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return Objects.equals(x, other.x) && Objects.equals(y, other.y);
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + "]";
	}
	
	
	/**
	 * Copy the current Position instance
	 * 
	 * @return a copy of the current Position instance
	 */
	public Position copy() {
		return new Position(x, y);
	}

	public void substract(Position translationFactor) {
		this.x -= translationFactor.x;
		this.y -= translationFactor.y;
	}

	public void divide(double scaleFactor) {
        this.x = (float) (this.x / scaleFactor);
		this.y = (float) (this.y / scaleFactor);
	}
}
