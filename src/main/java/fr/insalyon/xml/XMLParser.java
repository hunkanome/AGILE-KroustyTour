package fr.insalyon.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import fr.insalyon.model.*;

public class XMLParser {
    public static Map ParseFile(String filename) {
        // Create Map
        Map map = new Map();
        Long warehouseAddress = 0L;
        Intersection intersection, originIntersection, destinationIntersection;
        Segment segment;
        try {
            // Specify the path to your XML file
            File xmlFile = new File(filename);

            // Create a DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Create a DocumentBuilder
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML file to create a Document
            Document document = builder.parse(xmlFile);

            // Get the root element
            Element root = document.getDocumentElement();

            // Get all child nodes under the root element
            NodeList nodeList = root.getChildNodes();

            // Iterate through the nodes
            for (int i = 0; i < nodeList.getLength() ; i++) {
                Node node = nodeList.item(i);

                // Check if the node is an element node
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    // Check the node name
                    if (element.getTagName().equals("warehouse")) {
                        // Parse warehouse information
                        warehouseAddress = Long.parseLong(element.getAttribute("address"));
                    } else if (element.getTagName().equals("intersection")) {
                        // Parse intersection information
                        String id = element.getAttribute("id");
                        String latitude = element.getAttribute("latitude");
                        String longitude = element.getAttribute("longitude");
                        intersection = new Intersection(Long.parseLong(id), Float.parseFloat(latitude), Float.parseFloat(longitude));

                        map.addIntersection(intersection);
                    } else if (element.getTagName().equals("segment")) {
                        // Parse segment information
                        String origin = element.getAttribute("origin");
                        String destination = element.getAttribute("destination");
                        String length = element.getAttribute("length");
                        String name = element.getAttribute("name");

                        originIntersection = map.getIntersectionById(Long.parseLong(origin));
                        destinationIntersection = map.getIntersectionById(Long.parseLong(destination));

                        segment = new Segment(originIntersection, destinationIntersection, name, Float.parseFloat(length));

                        originIntersection.addOutwardSegment(segment);
                    }
                }
            }
            // find the warehouse in the intersections and put it in the map
            for (Intersection intersection1 : map.getIntersections()) {
                if (intersection1.getId() == warehouseAddress) {
                    map.setWarehouse(intersection1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}