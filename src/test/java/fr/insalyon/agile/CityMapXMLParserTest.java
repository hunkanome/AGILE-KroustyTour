package fr.insalyon.agile;

import fr.insalyon.city_map_xml.BadlyFormedXMLException;
import fr.insalyon.city_map_xml.CityMapXMLParser;
import fr.insalyon.city_map_xml.XMLParserException;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Segment;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class CityMapXMLParserTest {

	@Test
	void testParseValidMap() throws BadlyFormedXMLException, XMLParserException {
		String testXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><map>"
				+ "<warehouse address=\"0\"/>"
				+ "<intersection id=\"0\" latitude=\"45\" longitude=\"4\"/>"
				+ "<intersection id=\"1\" latitude=\"45\" longitude=\"8\"/>"
				+ "<segment destination=\"1\" length=\"79.02355\" name=\"Rue Antoine Charial\" origin=\"0\"/>"
				+ "<segment destination=\"0\" length=\"69.480034\" name=\"Rue Antoine Charial\" origin=\"1\"/>"
				+ "</map>";
		InputStream input = new ByteArrayInputStream(testXml.getBytes());
		CityMapXMLParser parser = new CityMapXMLParser(input);
		CityMap map = parser.parse();

		CityMap expectedResult = new CityMap();
		Intersection intersection0 = new Intersection(0L, new GeoCoordinates(45f, 4f), 0);
		Intersection intersection1 = new Intersection(1L, new GeoCoordinates(45f, 8f), 1);
		expectedResult.setWarehouse(intersection0);
		Segment s1 = new Segment(intersection0, intersection1, "Rue Antoine Charial", 79.02355f);
		Segment s2 = new Segment(intersection1, intersection0, "Rue Antoine Charial", 69.480034f);
		intersection0.addOutwardSegment(s1);
		intersection1.addOutwardSegment(s2);
		expectedResult.addIntersection(intersection0);
		expectedResult.addIntersection(intersection1);

		assertEquals(expectedResult, map);
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/badlyFormedMapXMLDocuments.csv", numLinesToSkip = 1)
	void testBadlyFormedMapXMLDocument(String document, String expectedMessage) {
		InputStream input = new ByteArrayInputStream(document.getBytes());
		CityMapXMLParser parser = new CityMapXMLParser(input);
		Exception exception = assertThrows(BadlyFormedXMLException.class, parser::parse);

		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

}
