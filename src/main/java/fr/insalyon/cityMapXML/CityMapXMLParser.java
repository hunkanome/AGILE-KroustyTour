package fr.insalyon.cityMapXML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fr.insalyon.geometry.GeoCoordinates;
import fr.insalyon.model.*;

/**
 * Parse an XML file containing a CityMap and construct the corresponding CityMap
 * object.<br/>
 * The XML file must be formatted as follows :
 * 
 * <pre>
 * {@code
 * <map>
 * <intersection id="1" latitude="45.770365" longitude="4.874439"/>
 * <intersection id="2" latitude="45.770365" longitude="4.874439"/>
 * <intersection id="3" latitude="45.770365" longitude="4.874439"/>
 * <segment origin="1" destination="2" length="100" name="rue de la paix"/>
 * <segment origin="2" destination="3" length="200" name="rue de la joie"/>
 * <warehouse address="1"/>
 * </map>
 * }
 * </pre>
 * 
 * It must contain at least one intersection and exactly one warehouse.<br/>
 * The order of the intersections and segments is not important.<br/>
 */
public class CityMapXMLParser {

	private final InputStream input;

	/**
	 * Create a parser for the given inputStream. The stream can be from a file, a
	 * socket, a string, ...<br/>
	 * Use the parse() method to parse the XML and get the CityMap
	 * 
	 * @param input the input stream to read the XML file. The stream must be valid,
	 *              open and readable
	 */
	public CityMapXMLParser(InputStream input) {
		this.input = input;
	}

	/**
	 * Parse the XML document and construct the CityMap
	 * 
	 * @return cityMap the map stored in the XML document
	 * @throws BadlyFormedXMLException if the XML document is not correctly formed
	 *                                 (unknown tag, incoherent data, ...)
	 * @throws XMLParserException      if any other type of error occurs during the parsing
	 *                                 (IOException, ...)
	 */
	public CityMap parse() throws BadlyFormedXMLException, XMLParserException {
		CityMap map = new CityMap();
		try {
			DocumentBuilder builder = this.getDocumentBuilder();
			Document document = builder.parse(input);
			Element root = document.getDocumentElement();
			if (!root.getTagName().equals("map")) {
				throw new BadlyFormedXMLException("The root of a map XML document must be a map element");
			}

			this.parseIntersections(root, map);
			this.parseSegments(root, map);
			this.parseWarehouse(root, map);
		} catch (SAXParseException | BadlyFormedXMLException e) {
			throw new BadlyFormedXMLException("XML file is badly formed : " + e.getMessage());
		} catch (Exception e) {
			throw new XMLParserException("Error while parsing XML file : " + e.getMessage());
		}
		return map;
	}

	/**
	 * Create a DocumentBuilder for parsing the XML Document and configure it to
	 * prevent XXE attacks.
	 * 
	 * @return DocumentBuilder
	 * @throws ParserConfigurationException if a feature of the DocumentBuilderFactory cannot be set
	 * @see <a href=
	 *      "https://rules.sonarsource.com/java/RSPEC-2755/">https://rules.sonarsource.com/java/RSPEC-2755/</a>
	 */
	private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

		return factory.newDocumentBuilder();
	}

	private void parseIntersections(Element root, CityMap map) throws BadlyFormedXMLException {
		try {
			NodeList intersectionNodes = root.getElementsByTagName("intersection");
			if (intersectionNodes.getLength() < 1) {
				throw new BadlyFormedXMLException("A map XML document must contain at least one Intersection");
			}

			List<Intersection> intersections = new ArrayList<>();
			for (int i = 0; i < intersectionNodes.getLength(); i++) {
				Element intersectionElement = (Element) intersectionNodes.item(i);
				String id = intersectionElement.getAttribute("id");
				String latitude = intersectionElement.getAttribute("latitude");
				String longitude = intersectionElement.getAttribute("longitude");
				GeoCoordinates coords = new GeoCoordinates(Float.parseFloat(latitude), Float.parseFloat(longitude));
				Intersection intersection = new Intersection(Long.parseLong(id), coords, i);
				intersections.add(intersection);
			}
			map.setIntersections(intersections);

		} catch (NumberFormatException e) {
			// If the number parsing fail (not a number, not present, ...)
			throw new BadlyFormedXMLException("Malformed intersection element");
		}
	}

	private void parseSegments(Element root, CityMap map) throws BadlyFormedXMLException {
		try {
			NodeList segmentNodes = root.getElementsByTagName("segment");
			for (int i = 0; i < segmentNodes.getLength(); i++) {
				Element segmentElement = (Element) segmentNodes.item(i);
				String origin = segmentElement.getAttribute("origin");
				String destination = segmentElement.getAttribute("destination");
				String length = segmentElement.getAttribute("length");
				String name = segmentElement.getAttribute("name");

				Intersection originIntersection = map.getIntersectionById(Long.parseLong(origin));
				Intersection destinationIntersection = map.getIntersectionById(Long.parseLong(destination));
				if (originIntersection == destinationIntersection) {
					throw new BadlyFormedXMLException("Segment's origin and destination must be different");
				}

				Segment segment = new Segment(originIntersection, destinationIntersection, name,
						Float.parseFloat(length));
				originIntersection.addOutwardSegment(segment);
			}
		} catch (NumberFormatException | IndexOutOfBoundsException e) {
			// If the number parsing fail (not a number, not present, ...)
			throw new BadlyFormedXMLException("Malformed segment element");
		}
	}

	private void parseWarehouse(Element root, CityMap map) throws BadlyFormedXMLException {
		NodeList warehouseNodes = root.getElementsByTagName("warehouse");
		if (warehouseNodes.getLength() == 1) {
			try {
				Element warehouseElement = (Element) warehouseNodes.item(0);
				String address = warehouseElement.getAttribute("address");
				Intersection intersection = map.getIntersectionById(Long.parseLong(address));

				map.setWarehouse(intersection);
			} catch (NumberFormatException | IndexOutOfBoundsException e) {
				// If the number parsing fail (not a number, not present, ...)
				throw new BadlyFormedXMLException("Malformed warehouse element");
			}
		} else {
			throw new BadlyFormedXMLException("A map XML document must contain exactly one warehouse");
		}
	}
}
