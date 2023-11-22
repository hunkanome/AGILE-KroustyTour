package fr.insalyon.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XMLParser {
    public static void ParseFile(String filename) {
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
                        String address = element.getAttribute("address");
                        System.out.println("Warehouse Address: " + address);
                    } else if (element.getTagName().equals("intersection")) {
                        // Parse intersection information
                        String id = element.getAttribute("id");
                        String latitude = element.getAttribute("latitude");
                        String longitude = element.getAttribute("longitude");
                        System.out.println("Intersection ID: " + id);
                        System.out.println("Latitude: " + latitude);
                        System.out.println("Longitude: " + longitude);
                        System.out.println();
                    } else if (element.getTagName().equals("segment")) {
                        // <segment destination="195276" length="33.907093" name="Rue des Tuiliers" origin="21993015"/>
                        // Parse segment information
                        String origin = element.getAttribute("origin");
                        String destination = element.getAttribute("destination");
                        String length = element.getAttribute("length");
                        String name = element.getAttribute("name");
                        System.out.println("Segment origin: " + origin);
                        System.out.println("Segment destination: " + destination);
                        System.out.println("Segment length: " + length);
                        System.out.println("Segment name: " + name);
                        System.out.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}