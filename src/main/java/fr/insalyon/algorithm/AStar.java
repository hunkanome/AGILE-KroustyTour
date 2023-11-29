package fr.insalyon.algorithm;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar implements ShortestPathAlgorithm {
    @Override
    public Path shortestPath(CityMap map, Intersection startNode, Intersection endNode) {
        // map.getIntersections() node index is its id
        float[] distances = new float[map.getIntersections().size()];
        int[] predecessors = new int[map.getIntersections().size()];
        PriorityQueue<Integer> greyNodes = new PriorityQueue<>(Comparator.comparing(index -> distances[index]));

        // init distances
        Arrays.fill(distances, Float.MAX_VALUE);
        distances[startNode.getIndex()] = 0;
        // init predecessors
        Arrays.fill(predecessors, -1);
        predecessors[startNode.getIndex()] = NO_PREDECESSORS;
        // init greyNodes
        greyNodes.add(startNode.getIndex());

        while (!greyNodes.isEmpty()) {
            int originNodeIndex = greyNodes.peek();
            Intersection originNode = map.getIntersections().get(originNodeIndex);

            for (Segment segment : originNode.getOutwardSegments()) {
                Intersection destinationNode = segment.getDestination();
                int destinationNodeIndex = destinationNode.getIndex();

                if (greyNodes.contains(destinationNodeIndex)) {
                    // the intersection is a grey node
                    if (distances[destinationNodeIndex] > distances[originNodeIndex] + segment.getLength() + distance(destinationNode, endNode)) {
                        distances[destinationNodeIndex] = distances[originNodeIndex] + segment.getLength() + distance(destinationNode, endNode);
                        predecessors[destinationNodeIndex] = originNodeIndex;
                    }
                }
                else if (predecessors[destinationNodeIndex] == -1) {
                    // the intersection is a white node
                    distances[destinationNodeIndex] = distances[originNodeIndex] + segment.getLength() + distance(destinationNode, endNode);
                    predecessors[destinationNodeIndex] = originNodeIndex;

                    greyNodes.add(destinationNodeIndex);
                }
            }
            // we pop the node from the priority queue
            greyNodes.poll();
        }

        // we get the list of intersections of the shortest path
        ArrayList<Intersection> intersectionsPath = new ArrayList<>();
        int currentNodeIndex = endNode.getIndex();
        while (predecessors[currentNodeIndex] != -1 && predecessors[currentNodeIndex] != -2) {
            intersectionsPath.addFirst(map.getIntersections().get(currentNodeIndex));
            currentNodeIndex = predecessors[currentNodeIndex];
        }
        intersectionsPath.addFirst(startNode);

        // we construct the shortest path made of segments
        Path shortestPath = new Path(startNode, endNode);
        for (int i = 0; i < intersectionsPath.size()-1; i++) {
            for (Segment segment : intersectionsPath.get(i).getOutwardSegments()) {
                if (segment.getDestination() == intersectionsPath.get(i+1)) {
                    shortestPath.appendSegment(segment);
                }
            }
        }

        return shortestPath;
    }

    public float distance(Intersection currentNode, Intersection endNode) {
        //https://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude
        float lat1 = currentNode.getLatitude(); float lon1 = currentNode.getLongitude();
        float lat2 = endNode.getLatitude(); float lon2 = endNode.getLongitude();

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
