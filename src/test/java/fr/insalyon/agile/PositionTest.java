package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

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
}
