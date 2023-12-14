package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import fr.insalyon.agile.mock.MockCityMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.city_map_xml.BadlyFormedXMLException;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Courier;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Segment;
import fr.insalyon.model.TimeWindow;
import fr.insalyon.model.Tour;
import fr.insalyon.seralization.XMLTourDeserializer;

class XMLTourDeserializerTest {
	
	private static CityMap cityMap;
	private static Intersection intersection;
	private static Intersection intersection1;
	private static Intersection intersection2;
	
	@BeforeAll
	static void createCityMap() {
		intersection = new Intersection(0, new GeoCoordinates(1f, 1f), 0);
		intersection1 = new Intersection(1, new GeoCoordinates(1f, 2f), 1);
		intersection2 = new Intersection(2, new GeoCoordinates(2f, 2f), 2);
		Segment segment = new Segment(intersection, intersection1, "test", 10);
		intersection.addOutwardSegment(segment);
		Segment segment2 = new Segment(intersection, intersection2, "test2", 10);
		intersection.addOutwardSegment(segment2);
		
		Segment segment1 = new Segment(intersection1, intersection, "test", 10);
		intersection1.addOutwardSegment(segment1);
		Segment segment3 = new Segment(intersection1, intersection2, "test1", 10);
		intersection1.addOutwardSegment(segment3);
		
		Segment segment4 = new Segment(intersection2, intersection, "test2", 10);
		intersection2.addOutwardSegment(segment4);
		Segment segment5 = new Segment(intersection2, intersection1, "test1", 10);
		intersection2.addOutwardSegment(segment5);
		
		cityMap = new MockCityMap();
		cityMap.setWarehouse(intersection1);
		cityMap.addIntersection(intersection);
		cityMap.addIntersection(intersection1);
		cityMap.addIntersection(intersection2);
	}

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
	
	@Test
	void testOneTourWithOneDelivery() throws Exception {
		XMLTourDeserializer deserializer = new XMLTourDeserializer();
		deserializer.setCityMap(cityMap);
		String inputXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"><tour courierId=\"1\"><delivery intersectionId=\"0\" timeWindow=\"8\"/></tour></tours>";
		ByteArrayInputStream in = new ByteArrayInputStream(inputXml.getBytes());
		deserializer.setInputFile(in);
		deserializer.deserialize();

		List<Tour> actual = deserializer.getTours();
		List<Tour> expected = new ArrayList<>();
		Tour tour = new Tour(new Courier(1));
		Delivery delivery = new Delivery(intersection, TimeWindow.getTimeWindow(8));
		tour.addDelivery(delivery, cityMap, new AStar());
		expected.add(tour);
		
		assertEquals(expected.size(), actual.size());
		
		Tour expectedTour = expected.get(0);
		Tour actualTour = actual.get(0);
		assertEquals(expectedTour.getCourier().getId(), actualTour.getCourier().getId());
		assertEquals(expectedTour.getDeliveriesList().get(0).getLocation(), actualTour.getDeliveriesList().get(0).getLocation());
	}

}
