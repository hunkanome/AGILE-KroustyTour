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

	private GeoCoordinates northWestMostPoint;

	private GeoCoordinates southEastMostPoint;

	private Float targetWidth;

	private Float targetHeight;

	/**
	 * Creates a CoordinateTransformer configured with the border of the map
	 * (North-West most and South-East most points) and with the size of the image
	 * target.
	 * 
	 * @param northWestMostPoint
	 * @param southEastMostPoint
	 * @param targetWidth
	 * @param targetHeight
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
	 * @param coord - the coordinates in the spherical coordinate system
	 * @return the position in the Cartesian system
	 * @see <a href=
	 *      "https://fr.wikipedia.org/wiki/Aide:Syst%C3%A8mes_de_projection#De_la_formule_math%C3%A9matique_%C3%A0_celle_du_mod%C3%A8le">
	 *      Wikipedia - Aide:Systèmes de projection - Décomposition de la
	 *      projection</a> for the algorithm
	 */
	public Position transformToPosition(GeoCoordinates coord) {
		double right = southEastMostPoint.getLongitude();
		double left = northWestMostPoint.getLongitude();
		double top = northWestMostPoint.getLatitude();
		double bottom = southEastMostPoint.getLatitude();

		double x = targetWidth * (coord.getLongitude() - left) / (right - left);
		double y = targetHeight * (top - coord.getLatitude()) / (top - bottom);

		return new Position((float) x, (float) y);
	}

}
