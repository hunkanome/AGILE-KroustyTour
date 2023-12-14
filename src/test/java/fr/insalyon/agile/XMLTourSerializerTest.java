package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import fr.insalyon.agile.mock.MockCityMap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Courier;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Segment;
import fr.insalyon.model.TimeWindow;
import fr.insalyon.model.Tour;
import fr.insalyon.seralization.XMLTourSerializer;

class XMLTourSerializerTest {
	
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
		XMLTourSerializer serializer = new XMLTourSerializer();
		List<Tour> tours = new ArrayList<>();
		serializer.setTours(tours);
		CityMap cityMap = new MockCityMap();
		serializer.setCityMap(cityMap);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.setFile(out);
		serializer.serialize();

		String actual = out.toString().trim();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"/>";

		assertEquals(expected, actual);
	}

	@Test
	void testEmptyTour() throws Exception {
		XMLTourSerializer serializer = new XMLTourSerializer();
		List<Tour> tours = new ArrayList<>();
		Tour tour = new Tour(new Courier(1));
		tours.add(tour);
		serializer.setTours(tours);
		CityMap cityMap = new MockCityMap();
		serializer.setCityMap(cityMap);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.setFile(out);
		serializer.serialize();

		String actual = out.toString().trim();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"><tour courierId=\"1\"/></tours>";

		assertEquals(expected, actual);
	}

	@Test
	void testOneTourWithOneDelivery() throws Exception {
		XMLTourSerializer serializer = new XMLTourSerializer();
		List<Tour> tours = new ArrayList<>();
		Tour tour = new Tour(new Courier(1));
		Delivery delivery = new Delivery(intersection, TimeWindow.getTimeWindow(8));
		tour.addDelivery(delivery, cityMap, new AStar());
		tours.add(tour);
		serializer.setTours(tours);
		serializer.setCityMap(cityMap);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.setFile(out);
		serializer.serialize();

		String actual = out.toString().trim();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"><tour courierId=\"1\"><delivery intersectionId=\"0\" timeWindow=\"8\"/></tour></tours>";

		assertEquals(expected, actual);
	}

	@Test
	void testOneTourWithManyDeliveries() throws Exception {
		XMLTourSerializer serializer = new XMLTourSerializer();
		List<Tour> tours = new ArrayList<>();
		Tour tour = new Tour(new Courier(1));
		Delivery delivery = new Delivery(intersection, TimeWindow.getTimeWindow(8));
		Delivery delivery1 = new Delivery(intersection2, TimeWindow.getTimeWindow(10));
		tour.addDelivery(delivery, cityMap, new AStar());
		tour.addDelivery(delivery1, cityMap, new AStar());
		tours.add(tour);
		serializer.setTours(tours);
		serializer.setCityMap(cityMap);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.setFile(out);
		serializer.serialize();

		String actual = out.toString().trim();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"><tour courierId=\"1\"><delivery intersectionId=\"0\" timeWindow=\"8\"/><delivery intersectionId=\"2\" timeWindow=\"10\"/></tour></tours>";

		assertEquals(expected, actual);
	}
	
	@Test
	void testManyToursWithManyDeliveries() throws Exception {
		XMLTourSerializer serializer = new XMLTourSerializer();
		List<Tour> tours = new ArrayList<>();
		Tour tour = new Tour(new Courier(1));
		Delivery delivery = new Delivery(intersection, TimeWindow.getTimeWindow(8));
		tour.addDelivery(delivery, cityMap, new AStar());
		tours.add(tour);
		
		Tour tour1 = new Tour(new Courier(2));
		Delivery delivery1 = new Delivery(intersection2, TimeWindow.getTimeWindow(10));
		tour1.addDelivery(delivery1, cityMap, new AStar());
		tours.add(tour1);
		
		serializer.setTours(tours);
		serializer.setCityMap(cityMap);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		serializer.setFile(out);
		serializer.serialize();

		String actual = out.toString().trim();
		String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><tours cityMapHash=\"1\"><tour courierId=\"1\"><delivery intersectionId=\"0\" timeWindow=\"8\"/></tour><tour courierId=\"2\"><delivery intersectionId=\"2\" timeWindow=\"10\"/></tour></tours>";

		assertEquals(expected, actual);
	}

}
