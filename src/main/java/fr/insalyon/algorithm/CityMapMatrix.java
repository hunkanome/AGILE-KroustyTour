package fr.insalyon.algorithm;

import fr.insalyon.model.Intersection;
import fr.insalyon.model.CityMap;
import fr.insalyon.model.Path;
import fr.insalyon.model.Segment;

import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * Class used to compute and store the matrix of the shortest path between the delivery intersections
 */
public class CityMapMatrix {

    private final int NO_PREDECESSORS = -2;

    private Path[][] arrayPaths;

    private final CityMap map;

    private final List<Intersection> intersections;

    /**
     * Construct a new matrix of shortest path between passed locations
     * @param map the map which intersections and segments are used to compute all shortest paths
     * @param deliveries the deliveries locations
     * @see Intersection
     */
    public CityMapMatrix(CityMap map, List<Intersection> deliveries) {
        this.arrayPaths = new Path[deliveries.size()][deliveries.size()];
        this.map = map;
        this.intersections = deliveries;

        int i = 0;
        for (Intersection startIntersection : deliveries) {
            int j = 0;
            for (Intersection endIntersection : deliveries) {
                if (startIntersection != endIntersection) {
                    // Dijkstra
                    this.arrayPaths[i][j] = dijkstra(startIntersection, endIntersection);
                    this.arrayPaths[j][i] = dijkstra(endIntersection, startIntersection);
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

    /**
     * Computes the shortest path between the two intersection
     * @param startNode Starting intersection
     * @param endNode Arrival intersection
     * @return If there is a path between the two intersections, the Path object is returned
     * otherwise an empty Path with no segments is returned
     * @see Intersection
     * @see Path
     */
    private Path dijkstra(Intersection startNode, Intersection endNode) {
        // map.getIntersections() node index is its id
        float[] distances = new float[this.map.getIntersections().size()];
        int[] predecessors = new int[this.map.getIntersections().size()];
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
            Intersection originNode = this.map.getIntersections().get(originNodeIndex);

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

    /**
     * Add an intersection to the list and calculates the shortest path between this intersection and all the others
     * @param newIntersection The new intersection to add
     * @see Intersection
     */
    public void addIntersection(Intersection newIntersection) {
        int size = this.arrayPaths.length + 1;
        int i;

        this.intersections.add(newIntersection);

        // increase the size of the array
        this.arrayPaths = Arrays.copyOf(this.arrayPaths, size);
        this.arrayPaths[size - 1] = new Path[size];
        for (i = 0; i < size - 1; i++) {
            this.arrayPaths[i] = Arrays.copyOf(this.arrayPaths[i], size);
        }

        // fill the array with the new paths
        for (i = 0; i < size; i++) {
            this.arrayPaths[size - 1][i] = dijkstra(newIntersection, this.intersections.get(i));
            this.arrayPaths[i][size - 1] = dijkstra(this.intersections.get(i), newIntersection);
        }
    }
}
