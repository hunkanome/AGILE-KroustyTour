package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

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
		
		Position actualPosition = transformer.transformToPosition(coord);
		Position expectedPosition = new Position(50f, 50f);
		
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	void testTransformCoordinateCorner() {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		GeoCoordinates coord = new GeoCoordinates(10f, 0f);
		
		Position actualPosition = transformer.transformToPosition(coord);
		Position expectedPosition = new Position(0f, 0f);
		
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	void testTransformCoordinateBorder() {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		GeoCoordinates coord = new GeoCoordinates(0f, 5f);
		
		Position actualPosition = transformer.transformToPosition(coord);
		Position expectedPosition = new Position(50f, 100f);
		
		assertEquals(expectedPosition, actualPosition);
	}

	@Test
	void testPointOutOfBound() {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		GeoCoordinates coord = new GeoCoordinates(11f, 0f);
		Exception e = assertThrows(IllegalArgumentException.class, () -> transformer.transformToPosition(coord));

		String expectedMessage = "The coordinate is out of bound";
		String actualMessage = e.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testTransformPointWithScale() {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		Position originPosition = new Position(5f, 5f);
		
		Position translation = new Position(0f, 0f);
		double scale = 1f;
		Position actualPosition = transformer.transformToDragAndZoom(originPosition, translation, scale);
		Position expectedPosition = new Position(5f, 5f);
		
		assertEquals(expectedPosition, actualPosition);
	}
	
	@ParameterizedTest
	@CsvFileSource(resources = "/pointTransformation.csv", numLinesToSkip = 1)
	void testTransformPointWithScaleAndTranslation(float x, float y, double scale, float xTranslation, float yTranslation, float xExpected, float yExpected) {
		GeoCoordinates nwPoint = new GeoCoordinates(10f, 0f);
		GeoCoordinates sePoint = new GeoCoordinates(0f, 10f);
		CoordinateTransformer transformer = new CoordinateTransformer(nwPoint, sePoint, 100f, 100f);
		Position originPosition = new Position(x, y);
		
		Position translation = new Position(xTranslation, yTranslation);
		Position actualPosition = transformer.transformToDragAndZoomPosition(originPosition, translation, scale);
		Position expectedPosition = new Position(xExpected, yExpected);
		
		assertEquals(expectedPosition, actualPosition);
	}
	
}
