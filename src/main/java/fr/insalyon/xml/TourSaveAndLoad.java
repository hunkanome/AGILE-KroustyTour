package fr.insalyon.xml;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import fr.insalyon.model.*;
import fr.insalyon.geometry.*;
import javafx.collections.ObservableList;


public class TourSaveAndLoad {
    public static String askForSaveOutput(ObservableList<Tour> tours) {
        StringBuilder output = new StringBuilder();

        output.append("<output>\n");
        for(Tour tour : tours) {
            output.append("<tour>\n");
            String courierText = serializeCourier(tour.getCourier());
            String mapText = serializeCityMap(tour.getCityMap());
            String pathText = serializePath(tour.getPathList());


            String deliveriesText = serializeDelivery(tour.getDeliveriesList());

            output.append("<map>").append(mapText).append("</map>\n");
            output.append("<courier>").append(courierText).append("</courier>\n");
            output.append("<paths>").append(pathText).append("</paths>");
            output.append("<deliveries>").append(deliveriesText).append("</deliveries>");
            output.append("</tour>\n");
        }

        output.append("</output>\n");

        return output.toString();
    }

    private static String serializeCourier(Courier courier) {

        return "<id>" + courier.getId() + "</id>\n" +
                "\n<availability>" + courier.isAvailable() + "</availability>\n";
    }

    private static String serializeDelivery(Collection<Delivery> deliveries) {
        StringBuilder deliveriesString = new StringBuilder();

        for(Delivery delivery : deliveries) {
            deliveriesString.append("<delivery>");
            //deliveriesString.append("<courier>").append(serializeCourier(delivery.getCourier())).append("</courier>"); // TODO : MODIFIER AVEC LES CHANGEMENTS //////////////////////////////////
            deliveriesString.append("<location>").append(intersectionParsing(delivery.getLocation())).append("</location>");
            deliveriesString.append("<timeWindow>").append(delivery.getTimeWindow().getStartHour()).append("</timeWindow>");
            deliveriesString.append("</delivery>");
        }
        return deliveriesString.toString();
    }

    private static String serializePath(List<Path> paths) {
        StringBuilder pathString = new StringBuilder();

        for(Path path : paths) {
            pathString.append("<path>");
            pathString.append("<start>");
            Intersection start = path.getStart();
            pathString.append("<id>").append(start.getId()).append("</id>");

            GeoCoordinates coordinates = path.getStart().getCoordinates();
            pathString.append(coordinatesParsing(coordinates));
            // outwards
            Set<Segment> outward = start.getOutwardSegments();
            pathString.append(listOfSegmentParsing(outward, true));

            pathString.append("<index>").append(start.getIndex()).append("</index>");
            pathString.append("</start>\n");

            pathString.append("<end>");
            Intersection end = path.getEnd();
            pathString.append("<id>").append(start.getId()).append("</id>");

            GeoCoordinates coordinatesEnd = path.getEnd().getCoordinates();
            pathString.append(coordinatesParsing(coordinatesEnd));

            Set<Segment> outwardEnd = end.getOutwardSegments();
            pathString.append(listOfSegmentParsing(outwardEnd, true));

            pathString.append("<index>").append(end.getIndex()).append("</index>");
            pathString.append("</end>\n");

            pathString.append(listOfSegmentParsing(path.getSegments(), true));

            pathString.append("<length>").append(path.getLength()).append("</length>\n");
            pathString.append("</path>\n");
        }

        return pathString.toString();
    }

    private static String serializeCityMap(CityMap map) {
        // To compare with the actual map
        StringBuilder cityMapIdentifier = new StringBuilder();

        cityMapIdentifier.append("<warehouse>").append(map.getWarehouse().getCoordinates().getLatitude()).append("</warehouse>\n");

        for (Intersection inter : map.getIntersections()) {
            GeoCoordinates coordinates = inter.getCoordinates();
            cityMapIdentifier.append("<intersection>").append(coordinates.getLatitude()).append("</intersection>\n");
        }

        return cityMapIdentifier.toString();
    }

    private static String listOfSegmentParsing(Collection<Segment> segments, boolean firstIntersection) {

        StringBuilder output = new StringBuilder();
        if(firstIntersection){

            output.append("<segmentList>");
            for (Segment segment : segments) {
                output.append("<segment>");
                output.append("<origin>").append(intersectionParsing(segment.getOrigin())).append("</origin>"); // TODO : utiliser la méthode intersectionParsing pour avoir la destination et l'origin
                output.append("<destination>").append(intersectionParsing(segment.getDestination())).append("</destination>");
                output.append("<name>").append(segment.getName()).append("</name>");
                output.append("<length>").append(segment.getLength()).append("</length>");
                output.append("</segment>");
            }
            output.append("</segmentList>");

        }
        return output.toString();
    }

    private static String intersectionParsing(Intersection intersection) {
        StringBuilder output = new StringBuilder();

        output.append("<id>").append(intersection.getId()).append("</id>\n");

        GeoCoordinates coordinates = intersection.getCoordinates();
        output.append("<coordinates>");
        output.append("<latitude>").append(coordinates.getLatitude()).append("</latitude>");
        output.append("<longitude>").append(coordinates.getLongitude()).append("</longitude>");
        output.append("</coordinates>");

        Set<Segment> outwardSegments = intersection.getOutwardSegments();
        output.append(listOfSegmentParsing(outwardSegments,false));

        output.append("<index>").append(intersection.getIndex()).append("</index>");

        return output.toString();
    }

    private static String coordinatesParsing(GeoCoordinates coordinates) {
        StringBuilder output = new StringBuilder();

        output.append("<coordinates>");
        output.append("<latitude>").append(coordinates.getLatitude()).append("</latitude>");
        output.append("<longitude>").append(coordinates.getLongitude()).append("</longitude>");
        output.append("</coordinates>");

        return output.toString();
    }

}