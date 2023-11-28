package fr.insalyon.agile;

import fr.insalyon.model.CityMap;
import fr.insalyon.xml.CityMapXMLParser;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ParserTest {
    @Test
    void testParseFile() {
        CityMap map = CityMapXMLParser.parseFile("data/xml/testMap.xml");
        assertEquals("CityMap{warehouse=null, intersections=[Intersection{id=0, latitude=45.0, longitude=4.0, outwardSegments=[Segment{originID=0, destinationID=1, name='Rue Antoine Charial', length=79.02355}], index=0}, Intersection{id=1, latitude=45.0, longitude=8.0, outwardSegments=[Segment{originID=1, destinationID=0, name='Rue Antoine Charial', length=69.480034}], index=1}]}", map.toString());
    }
    @Test
    void testParseFileError() {
        CityMap map = CityMapXMLParser.parseFile("data/xml/testMapError.xml");
        assertNull(map);
    }
}
