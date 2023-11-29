package fr.insalyon.algorithm;

import fr.insalyon.model.CityMap;
import fr.insalyon.model.Intersection;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra implements ShortestPathAlgorithm {
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
                    if (distances[destinationNodeIndex] > distances[originNodeIndex] + segment.getLength()) {
                        distances[destinationNodeIndex] = distances[originNodeIndex] + segment.getLength();
                        predecessors[destinationNodeIndex] = originNodeIndex;
                    }
                }
                else if (predecessors[destinationNodeIndex] == -1) {
                    // the intersection is a white node
                    distances[destinationNodeIndex] = distances[originNodeIndex] + segment.getLength();
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
}
