package fr.insalyon.algorithm;

import fr.insalyon.model.Intersection;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;

import java.util.*;

public class CityMapMatrix {

    private final int NO_PREDECESSORS = -2;
    protected Path[][] arrayPaths;

    public CityMapMatrix(CityMap map, List<Intersection> deliveries) {
        this.arrayPaths = new Path[deliveries.size()][deliveries.size()];

        int i = 0;
        Iterator<Intersection> it1 = deliveries.iterator();
        while (it1.hasNext()) {
            Intersection startIntersection = it1.next();

            int j = 0;
            Iterator<Intersection> it2 = deliveries.iterator();
            while (it2.hasNext()) {
                Intersection endIntersection = it2.next();

                if (startIntersection != endIntersection) {
                    // Dijkstra
                    this.arrayPaths[i][j] = dijkstra(map, startIntersection, endIntersection);
                    this.arrayPaths[j][i] = dijkstra(map, endIntersection, startIntersection);
                } else {
                    this.arrayPaths[i][j] = new Path(startIntersection, startIntersection, new ArrayList<>());
                }
                j++;
            }
            i++;
        }
    }

    public Path[][] getArrayPaths() { return arrayPaths; }

    public void setArrayPaths(Path[][] arrayPaths) { this.arrayPaths = arrayPaths; }

    private Path dijkstra(CityMap map, Intersection startNode, Intersection endNode) {
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

        // shortest path
        Path path = new Path(startNode, endNode);

        // we get the list of intersections of the shortest path
        ArrayList<Intersection> intersectionsPath = new ArrayList<>();

        int currentNodeIndex = endNode.getIndex();
        while (predecessors[currentNodeIndex] != -1 && predecessors[currentNodeIndex] != -2) {
            intersectionsPath.addFirst(map.getIntersections().get(currentNodeIndex));
            currentNodeIndex = predecessors[currentNodeIndex];
        }
        intersectionsPath.addFirst(startNode);

        // we construct the path made of segments
        for (int i = 0; i<intersectionsPath.size()-1; i++) {
            for (Segment segment : intersectionsPath.get(i).getOutwardSegments()) {
                if (segment.getDestination() == intersectionsPath.get(i+1)) {
                    path.appendSegment(segment);
                }
            }
        }

        return path;
    }
}
