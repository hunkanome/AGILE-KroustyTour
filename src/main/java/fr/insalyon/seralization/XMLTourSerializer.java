package fr.insalyon.seralization;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.insalyon.model.Delivery;
import fr.insalyon.model.Tour;

public class XMLTourSerializer extends TourSerializer {

	private Document document;

	@Override
	public TourSerializer serialize() throws Exception {
		document = getDocumentBuilder().newDocument();
		Element root = document.createElement("tours");
		document.appendChild(root);

		for (Tour tour : this.tours) {
			Element tourElement = serializeTour(tour);
			root.appendChild(tourElement);
		}

		int cityMapHash = this.cityMap.hashCode();
		root.setAttribute("cityMapHash", Integer.toString(cityMapHash));

		Transformer transformer = this.getTransformer();
		transformer.transform(new DOMSource(document), new StreamResult(this.out));

		return this;
	}

	private Element serializeTour(Tour tour) {
		Element tourElement = document.createElement("tour");
		String courierId = Integer.toString(tour.getCourier().getId());
		tourElement.setAttribute("courierId", courierId);

		for (Delivery delivery : tour.getDeliveriesList()) {
			Element deliveryElement = serializeDelivery(delivery);
			tourElement.appendChild(deliveryElement);
		}

		return tourElement;
	}

	private Element serializeDelivery(Delivery delivery) {
		Element deliveryElement = document.createElement("delivery");
		String intersectionId = Long.toString(delivery.getLocation().getId());
		deliveryElement.setAttribute("intersectionId", intersectionId);
		String timeWindow = Integer.toString(delivery.getTimeWindow().getStartHour());
		deliveryElement.setAttribute("timeWindow", timeWindow);

		return deliveryElement;
	}

	/**
	 * Create a DocumentBuilder for parsing the XML Document and configure it to
	 * prevent XXE attacks.
	 * 
	 * @return DocumentBuilder
	 * @throws ParserConfigurationException if a feature of the
	 *                                      DocumentBuilderFactory cannot be set
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
	
	private Transformer getTransformer() throws TransformerConfigurationException {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
		transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
		
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty("encoding", "UTF-8");
		transformer.setOutputProperty("indent", "yes");
		
		return transformer;
	}

}
