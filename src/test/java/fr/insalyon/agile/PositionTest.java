package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.geometry.Position;

class PositionTest {

	@ParameterizedTest
	@CsvFileSource(resources = "/pointSubstract.csv", numLinesToSkip = 1)
	void testPositionSubstract(float x1, float y1, float x2, float y2, float xExpected, float yExpected) {
		Position p1 = new Position(x1, y1);
		Position p2 = new Position(x2, y2);
		
		p1.substract(p2);
		
		Position expectedPosition = new Position(xExpected, yExpected);
		assertEquals(expectedPosition, p1);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/pointDivide.csv", numLinesToSkip = 1)
	void testPositionDivide(float x1, float y1, double scale, float xExpected, float yExpected) {
		Position p1 = new Position(x1, y1);
		
		p1.divide(scale);
		
		Position expectedPosition = new Position(xExpected, yExpected);
		assertEquals(expectedPosition, p1);
	}
	@Test
	public void testEquals() {
		Position position1 = new Position(1.0f, 2.0f);
		Position position2 = new Position(1.0f, 2.0f);
        assertEquals(position1, position1);
        assertEquals(position1, position2);
	}
	@Test
	public void testNotEquals() {
		Position position1 = new Position(1.0f, 3.0f);
		Position position2 = new Position(1.0f, 2.0f);
        assertNotEquals(position1, position2);
        assertNotEquals(position1, null);
		assertNotEquals(position1, new GeoCoordinates(1.0f, 2.0f));
	}

	@Test
	public void toStringTest() {
		Position position = new Position(1.0f, 2.0f);
		assertEquals("Position [x=1.0, y=2.0]", position.toString());
	}

	//test for distanceTo
	@Test
	public void testDistanceTo() {
		Position position1 = new Position(1.0f, 2.0f);
		Position position2 = new Position(1.0f, 2.0f);
		Float expected = (float) 0;
		assertEquals(position1.distanceTo(position2), expected);
		position2 = new Position(4.0f, 2.0f);
		expected = (float) 3;
		assertEquals(position1.distanceTo(position2), expected);
		position2 = new Position(1.0f, 4.0f);
		expected = (float) 2;
		assertEquals(position1.distanceTo(position2), expected);
	}
}
