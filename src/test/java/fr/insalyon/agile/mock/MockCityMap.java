package fr.insalyon.agile.mock;

import fr.insalyon.model.CityMap;

/**
 * Mock to control easily the hashCode
 */
public class MockCityMap extends CityMap {
	@Override
	public int hashCode() {
		return 1;
	}
}
