package fr.insalyon.algorithm;

import fr.insalyon.model.Intersection;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;

import java.util.*;

public class CityMapMatrix {

    protected Path[][] arrayPaths;

    public CityMapMatrix(CityMap map, List<Intersection> deliveries) {
        Iterator<Intersection> i1 = deliveries.iterator();
        Iterator<Intersection> i2 = deliveries.iterator();

        int i = 0, j = 0;
        this.arrayPaths = new Path[deliveries.size()][deliveries.size()];

        while (i1.hasNext()) {
            Intersection startIntersection = i1.next();
            while (i2.hasNext()) {
                Intersection endIntersection = i2.next();

                if (i1 != i2) {
                    // Dijkstra
                    this.arrayPaths[i][j] = dijkstra(map, startIntersection, endIntersection);
                } else {
                    this.arrayPaths[i][j] = null;
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

        while (!greyNodes.isEmpty()) {
            int originNodeIndex = greyNodes.peek();
            Intersection originNode = map.getIntersections().get(originNodeIndex);

            for (Segment segment : startNode.getOutwardSegments()) {
                Intersection destinationNode = segment.getDestination();
                int destinationNodeIndex = destinationNode.getIndex();

                if (greyNodes.contains(originNodeIndex)) {
                    // the intersection is a grey node
                    if (distances[destinationNodeIndex] > distances[originNodeIndex] + segment.getLength()) {
                        distances[destinationNodeIndex] = distances[originNodeIndex] + segment.getLength();
                        predecessors[destinationNodeIndex] = originNodeIndex;
                    }
                }
                else if (predecessors[originNodeIndex] == -1) {
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
        while (predecessors[currentNodeIndex] != -1) {
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
