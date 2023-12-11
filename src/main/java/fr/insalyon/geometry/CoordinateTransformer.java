package fr.insalyon.geometry;

/**
 * Transforms the coordinates in spherical coordinate system into a position in
 * the Cartesian coordinate system The position is calculated by mapping each
 * coordinate between a max and a min of this coordinate a position between 0
 * and the size of the image target
 * 
 * @see <a href=
 *      "https://fr.wikipedia.org/wiki/Aide:Syst%C3%A8mes_de_projection">Wikipedia
 *      - Aide:Systèmes de projection</a>
 */
public class CoordinateTransformer {

	private final GeoCoordinates northWestMostPoint;

	private final GeoCoordinates southEastMostPoint;

	private final Float targetWidth;

	private final Float targetHeight;

	/**
	 * Creates a CoordinateTransformer configured with the border of the map
	 * (North-West most and South-East most points) and with the size of the image
	 * target.
	 * 
	 * @param northWestMostPoint - the North-West most point of the map
	 * @param southEastMostPoint - the South-East most point of the map
	 * @param targetWidth 	  - the width of the image target
	 * @param targetHeight 	  - the height of the image target
	 */
	public CoordinateTransformer(GeoCoordinates northWestMostPoint, GeoCoordinates southEastMostPoint,
			Float targetWidth, Float targetHeight) {
		this.northWestMostPoint = northWestMostPoint;
		this.southEastMostPoint = southEastMostPoint;
		this.targetWidth = targetWidth;
		this.targetHeight = targetHeight;
	}

	/**
	 * Transforms the coordinates in the spherical coordinate system into a position
	 * in the Cartesian coordinate system
	 * 
	 * @param coord - the coordinates in the spherical coordinate system. Must be in the bound of the map
	 * @return the position in the Cartesian system
	 * @throws IllegalArgumentException if the coordinate is out of bound
	 * @see <a href=
	 *      "https://fr.wikipedia.org/wiki/Aide:Syst%C3%A8mes_de_projection#De_la_formule_math%C3%A9matique_%C3%A0_celle_du_mod%C3%A8le">
	 *      Wikipedia - Aide:Systèmes de projection - Décomposition de la
	 *      projection</a> for the algorithm
	 */
	public Position transformToPosition(GeoCoordinates coordinates) throws IllegalArgumentException {
		if (coordinates.getLongitude() < northWestMostPoint.getLongitude()
				|| coordinates.getLongitude() > southEastMostPoint.getLongitude()
				|| coordinates.getLatitude() > northWestMostPoint.getLatitude()
				|| coordinates.getLatitude() < southEastMostPoint.getLatitude()) {
			throw new IllegalArgumentException("The coordinate is out of bound");
		}

		double right = southEastMostPoint.getLongitude();
		double left = northWestMostPoint.getLongitude();
		double top = northWestMostPoint.getLatitude();
		double bottom = southEastMostPoint.getLatitude();

		double x = targetWidth * (coordinates.getLongitude() - left) / (right - left);
		double y = targetHeight * (top - coordinates.getLatitude()) / (top - bottom);

		return new Position((float) x, (float) y);
	}

	public Position transformToDragAndZoomPosition(GeoCoordinates coordinates, Position translation, double scaleFactor) throws IllegalArgumentException {
		Position position = transformToPosition(coordinates);

		position.setX(position.getX() + translation.getX());
		position.setY(position.getY() + translation.getY());

		position.setX(position.getX() * new Float(scaleFactor));
		position.setY(position.getY() * new Float(scaleFactor));

		return position;
	}

	public Position transformToDragAndZoomPosition(Position position, Position translation, double scaleFactor) throws IllegalArgumentException {
		position.setX(position.getX() + translation.getX());
		position.setY(position.getY() + translation.getY());

		position.setX(position.getX() * new Float(scaleFactor));
		position.setY(position.getY() * new Float(scaleFactor));

		return position;
	}

}
