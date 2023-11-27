package fr.insalyon;

import fr.insalyon.xml.XMLParser;
import fr.insalyon.model.*;

public class Main {
    public static void main(String[] args) {
        CityMap map = XMLParser.parseFile("data/xml/testMapError.xml");
        System.out.println(map);
    }
}