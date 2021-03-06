package graph;

import static org.junit.Assert.*;

import java.util.Comparator;
import org.junit.Test;

public class DirectedGraphTest {

    @Test
    public void generalTest() {
        DirectedGraph<String, String> G = new DirectedGraph<String, String>();
        Graph<String, String>.Vertex v1 = G.add("vertex 1");
        Graph<String, String>.Vertex v2 = G.add("vertex 2");
        Graph<String, String>.Vertex v3 = G.add("vertex 3");
        G.add(v1, v2, "edge 1");

        assertEquals("wrong number of vertices in graph", G.vertexSize(), 3);
        assertEquals("wrong number of edges in graph", G.edgeSize(), 1);
        assertEquals("vertex 1 is missing", v1.getLabel(), "vertex 1");

        assertEquals("vertex 1 has wrong edge",
                G.outEdges(v1).next().getLabel(), "edge 1");

        G.remove(v1);
        assertEquals("wrong number of vertices after removal",
                G.vertexSize(), 2);
        assertEquals("wrong number of edges after removal", G.edgeSize(), 0);
        assertEquals("vertex 2 should have no edges",
                G.inEdges(v2).hasNext(), false);
    }

    @Test
    public void orderEdgesTest() {
        DirectedGraph<String, Integer> G = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex v1 = G.add("vertex 1");
        Graph<String, Integer>.Vertex v2 = G.add("vertex 2");
        Graph<String, Integer>.Vertex v3 = G.add("vertex 3");
        Graph<String, Integer>.Vertex v4 = G.add("vertex 4");
        G.add(v1, v2, 1);
        G.add(v2, v3, 2);
        G.add(v3, v4, 0);

        Iteration<Graph<String, Integer>.Edge> firstEdges = G.edges();
        int edge1 = firstEdges.next().getLabel();
        int edge2 = firstEdges.next().getLabel();
        int edge3 = firstEdges.next().getLabel();
        assertEquals("wrong beginning first edge", edge1, 1);
        assertEquals("wrong beginning second edge", edge2, 2);
        assertEquals("wrong beginning third edge", edge3, 0);

        G.orderEdges(compareInts());
        Iteration<Graph<String, Integer>.Edge> edges = G.edges();
        int val1 = edges.next().getLabel();
        int val2 = edges.next().getLabel();
        int val3 = edges.next().getLabel();
        assertEquals("wrong first edge", val1, 0);
        assertEquals("wrong second edge", val2, 1);
        assertEquals("wrong third edge", val3, 2);
    }

    @Test
    public void succeedPreceedTest() {
        DirectedGraph<String, Integer> G = new DirectedGraph<String, Integer>();
        Graph<String, Integer>.Vertex v1 = G.add("vertex 1");
        Graph<String, Integer>.Vertex v2 = G.add("vertex 2");
        Graph<String, Integer>.Vertex v3 = G.add("vertex 3");
        Graph<String, Integer>.Vertex v4 = G.add("vertex 4");
        G.add(v1, v2, 1);
        G.add(v1, v3, 2);
        G.add(v3, v4, 0);

        Iteration<Graph<String, Integer>.Vertex> successors = G.successors(v1);
        assertEquals("wrong succeeding vertex for v1", successors.next(), v2);
        assertEquals("wrong succeeding vertex for v1", successors.next(), v3);

        Iteration<Graph<String, Integer>.Vertex> preceed = G.predecessors(v4);
        assertEquals("wrong predecessor of v4", preceed.next(), v3);
    }

    /** Returns the an integer comparator that compares integers. */
    private Comparator<Integer> compareInts() {
        Comparator<Integer> comp = new Comparator<Integer>() {
            @Override
            public int compare(Integer x, Integer y) {
                if (x > y) {
                    return 1;
                } else if (y > x) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
        return comp;
    }

}
