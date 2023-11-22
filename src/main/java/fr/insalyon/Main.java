package fr.insalyon;

import fr.insalyon.xml.XMLParser;
import fr.insalyon.model.*;

public class Main {
    public static void main(String[] args) {
        Map map = XMLParser.ParseFile("data/xml/smallMap.xml");
        System.out.println(map);
    }
}