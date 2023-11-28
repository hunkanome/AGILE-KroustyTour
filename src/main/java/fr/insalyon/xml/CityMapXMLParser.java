package fr.insalyon.xml;

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

import fr.insalyon.model.*;

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
	 * @throws XMLParserException      if an other error occurs during the parsing
	 *                                 (IOException, ...)
	 */
	public CityMap parse() throws BadlyFormedXMLException, XMLParserException {
		CityMap map = new CityMap();
		try {
			DocumentBuilder builder = this.getDocumentBuilder();
			Document document = builder.parse(input);
			Element root = document.getDocumentElement();

			this.parseIntersections(root, map);
			this.parseSegments(root, map);
			this.parseWarehouse(root, map);
		} catch (SAXParseException e) {
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
	 * @throws ParserConfigurationException
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
			List<Intersection> intersections = new ArrayList<>();
			for (int i = 0; i < intersectionNodes.getLength(); i++) {
				Element intersectionElement = (Element) intersectionNodes.item(i);
				String id = intersectionElement.getAttribute("id");
				String latitude = intersectionElement.getAttribute("latitude");
				String longitude = intersectionElement.getAttribute("longitude");
				Intersection intersection = new Intersection(Long.parseLong(id), Float.parseFloat(latitude),
						Float.parseFloat(longitude), i);
				intersections.add(intersection);
			}
			map.setIntersections(intersections);

		} catch (NumberFormatException | NullPointerException e) {
			// If the number parsing fail (not a number, not present, ...)
			throw new BadlyFormedXMLException("Error while parsing intersections");
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
				Segment segment = new Segment(originIntersection, destinationIntersection, name,
						Float.parseFloat(length));
				originIntersection.addOutwardSegment(segment);
			}
		} catch (NumberFormatException | NullPointerException e) {
			// If the number parsing fail (not a number, not present, ...)
			throw new BadlyFormedXMLException("Error while parsing segments");
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
			} catch (NumberFormatException e) {
				// If the number parsing fail (not a number, not present, ...)
				throw new BadlyFormedXMLException("Error while parsing warehouse");
			}
		} else {
			throw new BadlyFormedXMLException("A map must contain exactly sone warehouse");
		}
	}
}
