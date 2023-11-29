package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import fr.insalyon.geometry.CoordinateTransformer;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.geometry.Position;

class CoordinateTransformerTest {

	@Test
	void testTransformCoordinateMiddle() {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		GeoCoordinates coord = new GeoCoordinates(5f, 5f);
		Position position = transformer.transformToPosition(coord);
		assertEquals(50f, position.getX().floatValue());
		assertEquals(50f, position.getY().floatValue());
	}

	@Test
	void testTransformCoordinateCorner() {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		GeoCoordinates coord = new GeoCoordinates(10f, 0f);
		Position position = transformer.transformToPosition(coord);
		assertEquals(0f, position.getX().floatValue());
		assertEquals(0f, position.getY().floatValue());
	}

	@Test
	void testTransformCoordinateBorder() {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		GeoCoordinates coord = new GeoCoordinates(0f, 5f);
		Position position = transformer.transformToPosition(coord);
		assertEquals(50f, position.getX().floatValue());
		assertEquals(100f, position.getY().floatValue());
	}

	@Test
	void testPointOutOfBound() {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		GeoCoordinates coord = new GeoCoordinates(11f, 0f);
		Exception e = assertThrows(IllegalArgumentException.class, () -> {
			transformer.transformToPosition(coord);
		});

		String expectedMessage = "The coordinate is out of bound";
		String actualMessage = e.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
	
}
