package fr.insalyon.agile;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Courier;
import fr.insalyon.model.Tour;
import fr.insalyon.seralization.XMLTourSerializer;

class XMLTourSerializerTest {

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
	
	// TODO : ajouter des tests avec des tours remplis

}
