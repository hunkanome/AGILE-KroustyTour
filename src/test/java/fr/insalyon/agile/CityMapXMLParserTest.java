package fr.insalyon.agile;

import fr.insalyon.model.CityMap;
import fr.insalyon.xml.BadlyFormedXMLException;
import fr.insalyon.xml.CityMapXMLParser;
import fr.insalyon.xml.XMLParserException;

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
				+ "<warehouse address=\"0\"/><intersection id=\"0\" latitude=\"45\" longitude=\"4\"/>"
				+ "<intersection id=\"1\" latitude=\"45\" longitude=\"8\"/>"
				+ "<segment destination=\"1\" length=\"79.02355\" name=\"Rue Antoine Charial\" origin=\"0\"/>"
				+ "<segment destination=\"0\" length=\"69.480034\" name=\"Rue Antoine Charial\" origin=\"1\"/>"
				+ "</map>";
		InputStream input = new ByteArrayInputStream(testXml.getBytes());
		CityMapXMLParser parser = new CityMapXMLParser(input);
		CityMap map = parser.parse();
		assertEquals(
				"CityMap{warehouse=Intersection{id=0, latitude=45.0, longitude=4.0, outwardSegments=[Segment{originID=0, destinationID=1, name='Rue Antoine Charial', length=79.02355}], index=0}, intersections=[Intersection{id=0, latitude=45.0, longitude=4.0, outwardSegments=[Segment{originID=0, destinationID=1, name='Rue Antoine Charial', length=79.02355}], index=0}, Intersection{id=1, latitude=45.0, longitude=8.0, outwardSegments=[Segment{originID=1, destinationID=0, name='Rue Antoine Charial', length=69.480034}], index=1}]}",
				map.toString());
	}

	@ParameterizedTest
	@CsvFileSource(resources = "/badlyFormedMapXMLDocuments.csv", numLinesToSkip = 1)
	void testBadlFormedMapXMLDocument(String document, String expectedMessage) {
		InputStream input = new ByteArrayInputStream(document.getBytes());
		CityMapXMLParser parser = new CityMapXMLParser(input);
		Exception exception = assertThrows(BadlyFormedXMLException.class, () -> {
			parser.parse();
		});

		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

}
