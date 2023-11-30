package fr.insalyon.algorithm;

import fr.insalyon.model.Intersection;

public class AStar extends Dijkstra {
    @Override
    public float heuristic(Intersection currentNode, Intersection endNode) {
        //https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
        float lat1 = currentNode.getLatitude();
        float lon1 = currentNode.getLongitude();
        float lat2 = endNode.getLatitude();
        float lon2 = endNode.getLongitude();

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0;

        return (float)Math.sqrt(Math.pow(distance, 2) + Math.pow(height, 2));
    }
}
