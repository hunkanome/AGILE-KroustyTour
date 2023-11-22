package fr.insalyon.algorithm;

import fr.insalyon.model.Intersection;
import fr.insalyon.model.Map;
import fr.insalyon.model.Path;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class MapMatrix {

    protected float[][] arrayDistances;
    protected Path[][] arrayPaths;

    MapMatrix(List<Intersection> deliveries) {
        Iterator<Intersection> i1 = deliveries.iterator();
        Iterator<Intersection> i2 = deliveries.iterator();

        int i = 0, j = 0, length;
        this.arrayDistances = new float[deliveries.size()][deliveries.size()];

        while (i1.hasNext()) {
            Intersection startIntersection = i1.next();
            while (i2.hasNext()) {
                Intersection endIntersection = i2.next();

                // Djikstra
                arrayPaths[i][j] = djikstra(null, startIntersection, endIntersection);

                j++;
            }
            i++;
        }
    }

    private Path djikstra(Map map, Intersection startNode, Intersection endNode) {
        Path path = new Path(startNode, endNode);

        PriorityQueue<Intersection> greyNodes = new PriorityQueue<>();

        float[] distancesArray = new float[map.getIntersections().size()];
        Arrays.fill(distancesArray, Float.MAX_VALUE);
        distancesArray[map.getIntersections().indexOf(startNode)] = 0;

        return path;
    }
}
