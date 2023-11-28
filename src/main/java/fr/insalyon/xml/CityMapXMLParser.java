package fr.insalyon.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.InputStream;

import fr.insalyon.model.*;


public class CityMapXMLParser {
	
	private final InputStream input;
	
	/**
	 * Create a parser for the given inputStream. The stream can be from a file, a socket, a string, ...<br/>
	 * Use the parse() method to parse the XML and get the CityMap
	 * 
	 * @param input the input stream to read the XML file
	 */
    public CityMapXMLParser(InputStream input) {
		this.input = input;
	}

	/**
	 * Parse the XML document and construct the CityMap.<br/>
	 * If the XML document is badly formed, throw an Exception
	 * TODO determine which exception to throw
	 * 
	 * @return cityMap the map stored in the XML document
	 */
	public CityMap parse() {
        // Create Map
        CityMap map = new CityMap();
        long warehouseAddress = 0L;
        Intersection intersection;
        Intersection originIntersection;
        Intersection destinationIntersection;
        Segment segment;

        try {
        	DocumentBuilder builder = this.getDocumentBuilder();
            Document document = builder.parse(input);
            Element root = document.getDocumentElement();

            // Get all child nodes under the root element
            NodeList nodeList = root.getChildNodes();

            int indexIntersection = 0;

            // Iterate through the nodes
            for (int i = 0; i < nodeList.getLength() ; i++) {
                Node node = nodeList.item(i);

                // Check if the node is an element node
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Check the node name
                    switch (element.getTagName()) {
                        case "warehouse" :
                            // Parse warehouse information
                            warehouseAddress = Long.parseLong(element.getAttribute("address"));
                            break;
                        case "intersection" :
                            // Parse intersection information
                            String id = element.getAttribute("id");
                            String latitude = element.getAttribute("latitude");
                            String longitude = element.getAttribute("longitude");
                            intersection = new Intersection(Long.parseLong(id), Float.parseFloat(latitude), Float.parseFloat(longitude), indexIntersection);
                            indexIntersection++;
                            map.addIntersection(intersection);
                            break;
                        case "segment" :
                            // Parse segment information
                            String origin = element.getAttribute("origin");
                            String destination = element.getAttribute("destination");
                            String length = element.getAttribute("length");
                            String name = element.getAttribute("name");
                            originIntersection = map.getIntersectionById(Long.parseLong(origin));
                            destinationIntersection = map.getIntersectionById(Long.parseLong(destination));
                            segment = new Segment(originIntersection, destinationIntersection, name, Float.parseFloat(length));
                            originIntersection.addOutwardSegment(segment);
                            break;
                        default :
                            break;
                    }
                }
            }
            // find the warehouse in the intersections and put it in the map
            for (Intersection intersection1 : map.getIntersections()) {
                if (intersection1.getId().equals(warehouseAddress)) {
                    map.setWarehouse(intersection1);
                }
            }
        } catch (Exception e) {
        	// TODO handle exception
            e.printStackTrace();
            map = null;
        }
        return map;
    }
	
	/**
	 * Create a DocumentBuilder for parsing the XML Document and configure it to prevent XXE attacks.
	 * @return DocumentBuilder
	 * @throws ParserConfigurationException
	 * @see <a>https://rules.sonarsource.com/java/RSPEC-2755/</a>
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
