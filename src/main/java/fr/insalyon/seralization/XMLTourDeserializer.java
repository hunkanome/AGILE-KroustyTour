package fr.insalyon.seralization;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import fr.insalyon.algorithm.AStar;
import fr.insalyon.algorithm.ShortestPathAlgorithm;
import fr.insalyon.cityMapXML.BadlyFormedXMLException;
import fr.insalyon.cityMapXML.XMLParserException;
import fr.insalyon.model.Courier;
import fr.insalyon.model.Delivery;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.TimeWindow;
import fr.insalyon.model.Tour;

public class XMLTourDeserializer extends TourDeserializer {

	private static final ShortestPathAlgorithm PATH_CALCULATOR = new AStar();
	
	@Override
	public TourDeserializer deserialize() throws Exception {
		try {
			DocumentBuilder builder = this.getDocumentBuilder();
			Document document = builder.parse(in);
			Element root = document.getDocumentElement();
			if (!root.getTagName().equals("tours")) {
				throw new BadlyFormedXMLException("The root of a tours XML document must be a `tours` element");
			}
			int cityMapHash = Integer.parseInt(root.getAttribute("cityMapHash"));
			
			int expectedHash = this.cityMap.hashCode();
			if (expectedHash != cityMapHash) {
				throw new XMLParserException("The map of the tours XML file is not the same as the currently loaded map");
			}
			
			this.parseTours(root);
			
		} catch (SAXParseException | BadlyFormedXMLException e) {
			throw new BadlyFormedXMLException("XML file is badly formed : " + e.getMessage());
		} catch (Exception e) {
			throw new XMLParserException("Error while parsing XML file : " + e.getMessage());
		}
		return this;
	}
	
	private void parseTours(Element root) {
		NodeList tourNodes = root.getElementsByTagName("tour");
		
		for (int i = 0; i < tourNodes.getLength(); i++) {
			Element tourElement = (Element) tourNodes.item(i);
			int courierId = Integer.parseInt(tourElement.getAttribute("courierId"));
			Courier courier = new Courier(courierId);
			Tour tour = new Tour(courier);
			
			NodeList deliveryNodes = tourElement.getElementsByTagName("delivery");
			for (int j = 0; j < deliveryNodes.getLength(); j++) {
				Element deliveryElement = (Element) deliveryNodes.item(j);
				Delivery delivery = this.parseDelivery(deliveryElement);
				tour.addDelivery(delivery, cityMap, PATH_CALCULATOR);
			}
			this.tours.add(tour);
		}
	}
	
	private Delivery parseDelivery(Element deliveryElement) {
		long intersectionId = Long.parseLong(deliveryElement.getAttribute("intersectionId"));
		Intersection intersection = cityMap.getIntersectionById(intersectionId);
		
		int timeWindowHour = Integer.parseInt(deliveryElement.getAttribute("timeWindow"));
		TimeWindow timeWindow = TimeWindow.getTimeWindow(timeWindowHour);
		
		return new Delivery(intersection, timeWindow);
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

}
