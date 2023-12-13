package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.insalyon.city_map_xml.BadlyFormedXMLException;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Courier;
import fr.insalyon.model.Tour;
import fr.insalyon.seralization.XMLTourDeserializer;

class XMLTourDeserializerTest {

	@Test
	void testEmptyTourList() throws Exception {
		XMLTourDeserializer deserializer = new XMLTourDeserializer();
		CityMap cityMap = new MockCityMap();
		deserializer.setCityMap(cityMap);
		String inputXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"/>";
		ByteArrayInputStream in = new ByteArrayInputStream(inputXml.getBytes());
		deserializer.setInputFile(in);
		deserializer.deserialize();

		List<Tour> actual = deserializer.getTours();
		List<Tour> expected = new ArrayList<>();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testBadlyFormedFile() {
		XMLTourDeserializer deserializer = new XMLTourDeserializer();
		CityMap cityMap = new MockCityMap();
		deserializer.setCityMap(cityMap);
		String inputXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test/>";
		ByteArrayInputStream in = new ByteArrayInputStream(inputXml.getBytes());
		deserializer.setInputFile(in);
		Exception exception = assertThrows(BadlyFormedXMLException.class, deserializer::deserialize);
		
		String actualMessage = exception.getMessage();
		String expectedMessage = "XML file is badly formed : The root of a tours XML document must be a `tours` element";
		
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	void testEmptyTour() throws Exception {
		XMLTourDeserializer deserializer = new XMLTourDeserializer();
		CityMap cityMap = new MockCityMap();
		deserializer.setCityMap(cityMap);
		String inputXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"><tour courierId=\"1\"/></tours>";
		ByteArrayInputStream in = new ByteArrayInputStream(inputXml.getBytes());
		deserializer.setInputFile(in);
		deserializer.deserialize();

		List<Tour> actual = deserializer.getTours();
		List<Tour> expected = new ArrayList<>();
		Tour tour = new Tour(new Courier(1));
		expected.add(tour);
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testEmptyTours() throws Exception {
		XMLTourDeserializer deserializer = new XMLTourDeserializer();
		CityMap cityMap = new MockCityMap();
		deserializer.setCityMap(cityMap);
		String inputXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"><tour courierId=\"1\"/><tour courierId=\"2\"/></tours>";
		ByteArrayInputStream in = new ByteArrayInputStream(inputXml.getBytes());
		deserializer.setInputFile(in);
		deserializer.deserialize();

		List<Tour> actual = deserializer.getTours();
		List<Tour> expected = new ArrayList<>();
		Tour tour = new Tour(new Courier(1));
		expected.add(tour);
		Tour tour2 = new Tour(new Courier(2));
		expected.add(tour2);
		
		assertEquals(expected, actual);
	}
	
	// TODO faire avec des tours remplis

}
