package graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;

import org.junit.Test;

public class TraversalTest {

    @Test
    public void testTraverse() {
        DirectedGraph<String, String> G = new DirectedGraph<String, String>();
        Graph<String, String>.Vertex v1 = G.add("A1");
        Graph<String, String>.Vertex v2 = G.add("B8");
        Graph<String, String>.Vertex v3 = G.add("C3");
        Graph<String, String>.Vertex v4 = G.add("D4");
        Graph<String, String>.Vertex v5 = G.add("E5");
        Graph<String, String>.Vertex v6 = G.add("F6");
        G.add(v1, v2, "edge 1");
        G.add(v1, v3, "edge 2");
        G.add(v1, v4, "edge 3");
        G.add(v4, v5, "edge 4");
        G.add(v4, v6, "edge 5");

        Regular test = new Regular();
        test.traverse(G, v1, giveComp());
        assertEquals("traverse previsit has an error",
                toStr(test.getPreVisits()), "B8C3D4E5F6");
        assertEquals("traverse visit has an error",
                toStr(test.getVisits()), "A1C3D4E5F6B8");
        assertEquals("traverse postvisit has an error",
                toStr(test.getPostVisits()), "");
    }

    @Test
    public void traverseCircle() {
        DirectedGraph<String, String> G = new DirectedGraph<String, String>();
        Graph<String, String>.Vertex v1 = G.add("A1");
        Graph<String, String>.Vertex v2 = G.add("B2");
        Graph<String, String>.Vertex v3 = G.add("C3");
        Graph<String, String>.Vertex v4 = G.add("D4");
        G.add(v1, v2, "edge 1");
        G.add(v1, v3, "edge 2");
        G.add(v3, v4, "edge 3");
        G.add(v2, v4, "edge 4");

        Regular test = new Regular();
        test.traverse(G, v1, giveComp());
        assertEquals("ctraverse previsit has an error",
                toStr(test.getPreVisits()), "B2C3D4D4");
        assertEquals("ctraverse visit has an error",
                toStr(test.getVisits()), "A1B2C3D4");
        assertEquals("ctraverse postvisit has an error",
                toStr(test.getPostVisits()), "");
    }

    @Test
    public void testDepthFirst() {
        DirectedGraph<String, String> G = new DirectedGraph<String, String>();
        Graph<String, String>.Vertex v1 = G.add("A");
        Graph<String, String>.Vertex v2 = G.add("B");
        Graph<String, String>.Vertex v3 = G.add("C");
        Graph<String, String>.Vertex v4 = G.add("D");
        Graph<String, String>.Vertex v5 = G.add("E");
        Graph<String, String>.Vertex v6 = G.add("F");
        G.add(v1, v2, "edge 1");
        G.add(v1, v3, "edge 2");
        G.add(v1, v4, "edge 3");
        G.add(v4, v5, "edge 4");
        G.add(v4, v6, "edge 5");

        Regular test = new Regular();
        test.depthFirstTraverse(G, v1);
        assertEquals("depth previsit has an error",
                toStr(test.getPreVisits()), "BCDEF");
        assertEquals("depth visit has an error",
                toStr(test.getVisits()), "ADFECB");
        assertEquals("depth postvisit has an error",
                toStr(test.getPostVisits()), "FEDCBA");
    }

    @Test
    public void depthCircle() {
        DirectedGraph<String, String> G = new DirectedGraph<String, String>();
        Graph<String, String>.Vertex v1 = G.add("A");
        Graph<String, String>.Vertex v2 = G.add("B");
        Graph<String, String>.Vertex v3 = G.add("C");
        Graph<String, String>.Vertex v4 = G.add("D");
        G.add(v1, v2, "edge 1");
        G.add(v1, v3, "edge 2");
        G.add(v3, v4, "edge 3");
        G.add(v2, v4, "edge 4");

        Regular test = new Regular();
        test.depthFirstTraverse(G, v1);
        assertEquals("cdpeth previsit has an error",
                toStr(test.getPreVisits()), "BCD");
        assertEquals("cdepth visit has an error",
                toStr(test.getVisits()), "ACDB");
        assertEquals("cdepth postvisit has an error",
                toStr(test.getPostVisits()), "DCBA");
    }

    @Test
    public void testBreadthFirst() {
        DirectedGraph<String, String> G = new DirectedGraph<String, String>();
        Graph<String, String>.Vertex v1 = G.add("A");
        Graph<String, String>.Vertex v2 = G.add("B");
        Graph<String, String>.Vertex v3 = G.add("C");
        Graph<String, String>.Vertex v4 = G.add("D");
        Graph<String, String>.Vertex v5 = G.add("E");
        Graph<String, String>.Vertex v6 = G.add("F");

        G.add(v1, v2, "edge 1");
        G.add(v1, v3, "edge 2");
        G.add(v1, v4, "edge 3");
        G.add(v4, v5, "edge 4");
        G.add(v4, v6, "edge 5");

        Regular test = new Regular();
        test.breadthFirstTraverse(G, v1);
        assertEquals("breadth previsit has an error",
                toStr(test.getPreVisits()), "BCDEF");
        assertEquals("breadth visit has an error",
                toStr(test.getVisits()), "ABCDEF");
        assertEquals("breadth postvisit has an error",
                toStr(test.getPostVisits()), "BCEFDA");
    }

    @Test
    public void breadthCircle() {
        DirectedGraph<String, String> G = new DirectedGraph<String, String>();
        Graph<String, String>.Vertex v1 = G.add("A");
        Graph<String, String>.Vertex v2 = G.add("B");
        Graph<String, String>.Vertex v3 = G.add("C");
        Graph<String, String>.Vertex v4 = G.add("D");

        G.add(v1, v2, "edge 1");
        G.add(v1, v3, "edge 2");
        G.add(v3, v4, "edge 3");
        G.add(v2, v4, "edge 4");

        Regular test = new Regular();
        test.breadthFirstTraverse(G, v1);
        assertEquals("cbreadth previsit has an error",
                toStr(test.getPreVisits()), "BCDD");
        assertEquals("cbreadth visit has an error",
                toStr(test.getVisits()), "ABCD");
        assertEquals("cbreadth postvisit has an error",
                toStr(test.getPostVisits()), "DCBA");
    }

    /** Returns a comparator for the general traverse method. */
    private Comparator<String> giveComp() {
        Comparator<String> comp = new Comparator<String>() {
            @Override
            public int compare(String x, String y) {
                int x0 = Integer.valueOf(String.valueOf(x.toCharArray()[1]));
                int x1 = Integer.valueOf(String.valueOf(y.toCharArray()[1]));
                if (x0 > x1) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        return comp;
    }

    /** Returns the string representation of the ArrayList given
    *  an ArrayList A. */
    private String toStr(ArrayList<Graph<String, String>.Vertex> A) {
        String v = "";
        for (Graph<String, String>.Vertex x: A) {
            v += x.getLabel();
        }
        return v;
    }

    /** A class that implements the previsit, visit, and postvisit.
     *  With all these methods, simply adds them to their respective
     *  lists. */
    private class Regular extends Traversal<String, String> {

        public Regular() {
            previsits = new ArrayList<Graph<String, String>.Vertex>();
            visits = new ArrayList<Graph<String, String>.Vertex>();
            postvisits = new ArrayList<Graph<String, String>.Vertex>();
        }

        @Override
        public void preVisit(Graph<String, String>.Edge e,
                Graph<String, String>.Vertex v0) {
            previsits.add(e.getV(v0));
        }

        /** Returns the previsits list. */
        public ArrayList<Graph<String, String>.Vertex> getPreVisits() {
            return previsits;
        }

        @Override
        public void visit(Graph<String, String>.Vertex v) {
            visits.add(v);
        }

        /** Returns the visits list. */
        public ArrayList<Graph<String, String>.Vertex> getVisits() {
            return visits;
        }

        @Override
        public void postVisit(Graph<String, String>.Vertex v) {
            postvisits.add(v);
        }

        /** Returns the postvisits list. */
        public ArrayList<Graph<String, String>.Vertex> getPostVisits() {
            return postvisits;
        }

        /** list of all the previsited vertices. */
        private ArrayList<Graph<String, String>.Vertex> previsits;
        /** list of all the visited vertices. */
        private ArrayList<Graph<String, String>.Vertex> visits;
        /** list of all the postvisited vertices. */
        private ArrayList<Graph<String, String>.Vertex> postvisits;

    }

}
