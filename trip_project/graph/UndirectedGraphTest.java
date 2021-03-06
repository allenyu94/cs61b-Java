package graph;

import static org.junit.Assert.*;

import org.junit.Test;

public class UndirectedGraphTest {

    @Test
    public void test() {
        UndirectedGraph<String, String> uG =
                new UndirectedGraph<String, String>();
        Graph<String, String>.Vertex v1 = uG.add("vertex 1");
        Graph<String, String>.Vertex v2 = uG.add("vertex 2");
        Graph<String, String>.Vertex v3 = uG.add("vertex 3");
        Graph<String, String>.Vertex v4 = uG.add("vertex 4");

        uG.add(v2, v3, "edge 1");
        uG.add(v3, v4, "edge 2");

        assertEquals("wrong number of undirected edges for vertex 3",
                uG.degree(v3), 2);

        Iteration<Graph<String, String>.Edge> e = uG.edges(v3);
        assertEquals("this is not a succeeding vertex of vertex 3",
                e.next().getV(v3), v2);
        assertEquals("this is not a succeeding vertex of vertex 3",
                e.next().getV(v3), v4);
        assertEquals("wrong number of successor vertices for vertex 3",
                uG.degree(v3), 2);
        assertEquals("wrong number of successors for vertex 1",
                uG.degree(v1), 0);
        assertEquals("wrong number of successors for vertex 2",
                uG.degree(v2), 1);
        assertEquals("wrong number of edges for vertex 3",
                getSize(uG.edges(v3)), 2);
        assertEquals("wrong number of edges for vertex 4",
                getSize(uG.edges(v4)), 1);
        assertEquals("cannot find edge 1", uG.contains(v3, v4, "edge 2"),
                true);
        assertEquals("graph should contain this edge",
                uG.contains(v3, v2), true);
        assertEquals("graph should contain this edge",
                uG.contains(v4, v3, "edge 2"), true);
    }

    /** Returns the size of the iteration ITER. That has label EDGE. */
    private int getSize(Iteration<Graph<String, String>.Edge> iter) {
        int count = 0;
        for (Graph<String, String>.Edge e: iter) {
            count += 1;
        }
        return count;
    }
}
