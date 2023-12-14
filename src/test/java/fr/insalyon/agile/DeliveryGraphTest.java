package fr.insalyon.agile;

import fr.insalyon.agile.mock.MockPath;
import fr.insalyon.algorithm.DeliveryGraph;
import static org.junit.jupiter.api.Assertions.assertNull;

import fr.insalyon.model.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;



public class DeliveryGraphTest {

    @Test
    public void testGetShortestPathNull() {
        DeliveryGraph graph = new DeliveryGraph();
        assertNull(graph.getShortestPath(0, 0));
        assertNull(graph.getShortestPath(-1, 0));
        assertNull(graph.getShortestPath(0, -1));
        assertNull(graph.getShortestPath(10, 0));
        assertNull(graph.getShortestPath(0, 10));
    }

    @Test
    public void testGetShortestPath() {
        MockPath[][] paths = new MockPath[2][2];
        paths[0][0] = new MockPath();
        paths[0][1] = new MockPath();
        paths[1][0] = new MockPath();
        paths[1][1] = new MockPath();
        paths[0][0].setLength(1);
        paths[0][1].setLength(2);
        paths[1][0].setLength(3);
        paths[1][1].setLength(4);

        DeliveryGraph graph = new DeliveryGraph(paths);

        assertNull(graph.getShortestPath(0, 0));
        assertNull(graph.getShortestPath(1, 1));
        Assertions.assertEquals(paths[0][1].getLength(), graph.getShortestPath(0, 1).getLength());
        Assertions.assertEquals(paths[1][0].getLength(), graph.getShortestPath(1, 0).getLength());
    }
}
