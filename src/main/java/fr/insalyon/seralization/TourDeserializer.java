package fr.insalyon.seralization;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Tour;

/**
 * The TourDeserializer interface defines methods for deserializing tours.
 */
public abstract class TourDeserializer {
	
	protected List<Tour> tours = new ArrayList<>();
	
	protected InputStream in;
	
	protected CityMap cityMap;

	/**
	 * Gets the list of tours.
	 * 
	 * @return the list of tours
	 */
	public List<Tour> getTours() {
		return tours;
	}

	/**
	 * Sets the input stream for deserialization.
	 * 
	 * @param - in the input file to set
	 * @return this deserializer
	 */
	public TourDeserializer setInputFile(InputStream in) {
		this.in = in;
		return this;
	}

	/**
	 * Sets the cityMap for the serialization process
	 * 
	 * @param cityMap - the city map
	 * @return this serializer
	 */
	public TourDeserializer setCityMap(CityMap cityMap) {
		this.cityMap = cityMap;
		return this;
	}

	/**
	 * Deserializes the data from the input file.
	 *
	 * @return this deserializer
	 * @throws IOException if an I/O error occurs
	 */
	public abstract TourDeserializer deserialize() throws Exception;
}
