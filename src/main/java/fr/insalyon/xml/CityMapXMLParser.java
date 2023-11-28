package fr.insalyon.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import fr.insalyon.model.*;

public class CityMapXMLParser {
    public static CityMap parseFile(String filename) {
        // Create Map
        CityMap map = new CityMap();
        long warehouseAddress = 0L;
        Intersection intersection;
        Intersection originIntersection;
        Intersection destinationIntersection;
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
            e.printStackTrace();
            map = null;
        }
        return map;
    }
}