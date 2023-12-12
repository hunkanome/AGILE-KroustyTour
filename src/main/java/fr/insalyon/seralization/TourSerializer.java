package fr.insalyon.seralization;

import java.io.OutputStream;
import java.util.List;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Tour;

/**
 * The TourSerializer interface defines methods for serializing tours.
 */
public abstract class TourSerializer {

	protected List<Tour> tours;

	protected OutputStream out;

	protected CityMap cityMap;

	/**
	 * Sets the list of tours to be serialized.
	 * 
	 * @param tours - the list of tours
	 * @return this serializer
	 */
	public TourSerializer setTours(List<Tour> tours) {
		this.tours = tours;
		return this;
	}

	/**
	 * Sets the cityMap for the serialization process
	 * 
	 * @param cityMap - the city map
	 * @return this serializer
	 */
	public TourSerializer setCityMap(CityMap cityMap) {
		this.cityMap = cityMap;
		return this;
	}

	/**
	 * Sets the output stream for writing the serialized data.
	 * 
	 * @param out - the output stream
	 * @return this serializer
	 */
	public TourSerializer setFile(OutputStream out) {
		this.out = out;
		return this;
	}

	/**
	 * Serializes the tours and writes the data to the output stream.
	 * 
	 * @return this serializer
	 * @throws Exception if an error occurs during the serialization
	 */
	public abstract TourSerializer serialize() throws Exception;

}
