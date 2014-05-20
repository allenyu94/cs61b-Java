package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may make changes that don't affect the API as seen
 * from outside the graph package:
 *   + You may make methods in Graph abstract, if you want different
 *     implementations in DirectedGraph and UndirectedGraph.
 *   + You may add bodies to abstract methods, modify existing bodies,
 *     or override inherited methods.
 *   + You may change parameter names, or add 'final' modifiers to parameters.
 *   + You may private and package private members.
 *   + You may add additional non-public classes to the graph package.
 */

/** Represents a general graph whose vertices are labeled with a type
 *  VLABEL and whose edges are labeled with a type ELABEL. The
 *  vertices are represented by the inner type Vertex and edges by
 *  inner type Edge.  A graph may be directed or undirected.  For
 *  an undirected graph, outgoing and incoming edges are the same.
 *  Graphs may have self edges and may have multiple edges between vertices.
 *
 *  The vertices and edges of the graph, the edges incident on a
 *  vertex, and the neighbors of a vertex are all accessible by
 *  iterators.  Changing the graph's structure by adding or deleting
 *  edges or vertices invalidates these iterators (subsequent use of
 *  them is undefined.)
 *  @author Allen Yu
 */
public abstract class Graph<VLabel, ELabel> {

    /** Represents one of my vertices. */
    public class Vertex {

        /** A new vertex with LABEL as the value of getLabel(). */
        Vertex(VLabel label) {
            _label = label;
        }

        /** Returns the label on this vertex. */
        public VLabel getLabel() {
            return _label;
        }

        @Override
        public String toString() {
            return String.valueOf(_label);
        }

        /** The label on this vertex. */
        private final VLabel _label;

    }

    /** Represents one of my edges. */
    public class Edge {

        /** An edge (V0,V1) with label LABEL.  It is a directed edge (from
         *  V0 to V1) in a directed graph. */
        Edge(Vertex v0, Vertex v1, ELabel label) {
            _label = label;
            _v0 = v0;
            _v1 = v1;
        }

        /** Returns the label on this edge. */
        public ELabel getLabel() {
            return _label;
        }

        /** Return the vertex this edge exits. For an undirected edge, this is
         *  one of the incident vertices. */
        public Vertex getV0() {
            return _v0;
        }

        /** Return the vertex this edge enters. For an undirected edge, this is
         *  the incident vertices other than getV1(). */
        public Vertex getV1() {
            return _v1;
        }

        /** Returns the vertex at the other end of me from V.  */
        public final Vertex getV(Vertex v) {
            if (v == _v0) {
                return _v1;
            } else if (v == _v1) {
                return _v0;
            } else {
                throw new
                    IllegalArgumentException("vertex not incident to edge");
            }
        }

        @Override
        public String toString() {
            return String.format("(%s,%s):%s", _v0, _v1, _label);
        }

        /** Endpoints of this edge.  In directed edges, this edge exits _V0
         *  and enters _V1. */
        private final Vertex _v0, _v1;

        /** The label on this edge. */
        private final ELabel _label;

    }

    /*=====  Methods and variables of Graph =====*/

    /** Returns the number of vertices in me. */
    public int vertexSize() {
        return vertices.size();
    }

    /** Returns the number of edges in me. */
    public int edgeSize() {
        return edges.size();
    }

    /** Returns true iff I am a directed graph. */
    public abstract boolean isDirected();

    /** Returns the number of outgoing edges incident to V. Assumes V is one of
     *  my vertices.  */
    public int outDegree(Vertex v) {
        int numOut = 0;
        for (Edge e: edges) {
            if (e.getV0() == v) {
                numOut += 1;
            }
        }
        return numOut;
    }

    /** Returns the number of incoming edges incident to V. Assumes V is one of
     *  my vertices. */
    public int inDegree(Vertex v) {
        int numIn = 0;
        for (Edge e: edges) {
            if (e.getV1() == v) {
                numIn += 1;
            }
        }
        return numIn;
    }

    /** Returns outDegree(V). This is simply a synonym, intended for
     *  use in undirected graphs. */
    public final int degree(Vertex v) {
        return outDegree(v);
    }

    /** Returns true iff there is an edge (U, V) in me with any label. */
    public boolean contains(Vertex u, Vertex v) {
        for (Edge e: edges) {
            if (e.getV0() == u && e.getV1() == v) {
                return true;
            }
        }
        return false;
    }

    /** Returns true iff there is an edge (U, V) in me with label LABEL. */
    public boolean contains(Vertex u, Vertex v,
                            ELabel label) {
        for (Edge e: edges) {
            if (e.getV0() == u && e.getV1() == v
                    && e.getLabel().equals(label)) {
                return true;
            }
        }
        return false;
    }

    /** Returns a new vertex labeled LABEL, and adds it to me with no
     *  incident edges. */
    public Vertex add(VLabel label) {
        Vertex newVtx = new Vertex(label);
        vertices.add(newVtx);
        return newVtx;
    }

    /** Returns an edge incident on FROM and TO, labeled with LABEL
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to,
                    ELabel label) {
        Edge newEdge = new Edge(from, to, label);
        edges.add(newEdge);
        return newEdge;
    }

    /** Returns an edge incident on FROM and TO with a null label
     *  and adds it to this graph. If I am directed, the edge is directed
     *  (leaves FROM and enters TO). */
    public Edge add(Vertex from,
                    Vertex to) {
        Edge newEdge = new Edge(from, to, null);
        edges.add(newEdge);
        return newEdge;
    }

    /** Remove V and all adjacent edges, if present. */
    public void remove(Vertex v) {
        vertices.remove(v);
        if (isDirected()) {
            for (Edge e0: outEdges(v)) {
                edges.remove(e0);
            }
            for (Edge e1: inEdges(v)) {
                edges.remove(e1);
            }
        } else {
            for (Edge e0: outEdges(v)) {
                edges.remove(e0);
            }
        }
    }

    /** Remove E from me, if present.  E must be between my vertices,
     *  or the result is undefined.  */
    public void remove(Edge e) {
        Vertex v0 = e.getV0();
        Vertex v1 = e.getV(v0);
        if (vertices.contains(v0) && vertices.contains(v1)) {
            edges.remove(e);
        }
    }

    /** Remove all edges from V1 to V2 from me, if present.  The result is
     *  undefined if V1 and V2 are not among my vertices.  */
    public void remove(Vertex v1, Vertex v2) {
        if (isDirected()) {
            for (Edge e: edges()) {
                if (e.getV0() == v1 && e.getV1() == v2) {
                    edges.remove(e);
                }
            }
        } else {
            for (Edge e:edges()) {
                if ((e.getV0() == v1 || e.getV1() == v1)
                        && (e.getV0() == v2 || e.getV1() == v2)) {
                    edges.remove(e);
                }
            }
        }
    }

    /** Returns an Iterator over all vertices in arbitrary order. */
    public Iteration<Vertex> vertices() {
        return Iteration.iteration(vertices);
    }

    /** Returns an iterator over all successors of V. */
    public Iteration<Vertex> successors(Vertex v) {
        Iteration<Edge> all = outEdges(v);
        ArrayList<Vertex> vtxlst = new ArrayList<Vertex>();
        for (Edge e: all) {
            vtxlst.add(e.getV1());
        }
        return Iteration.iteration(vtxlst);
    }

    /** Returns an iterator over all predecessors of V. */
    public Iteration<Vertex> predecessors(Vertex v) {
        Iteration<Edge> all = inEdges(v);
        ArrayList<Vertex> vtxlst = new ArrayList<Vertex>();
        for (Edge e: all) {
            vtxlst.add(e.getV0());
        }
        return Iteration.iteration(vtxlst);
    }

    /** Returns successors(V).  This is a synonym typically used on
     *  undirected graphs. */
    public final Iteration<Vertex> neighbors(Vertex v) {
        return successors(v);
    }

    /** Returns an iterator over all edges in me. */
    public Iteration<Edge> edges() {
        return Iteration.iteration(edges);
    }

    /** Returns iterator over all outgoing edges from V. */
    public Iteration<Edge> outEdges(Vertex v) {
        ArrayList<Edge> in = new ArrayList<Edge>();
        for (Edge e: edges) {
            if (e.getV0().equals(v)) {
                in.add(e);
            }
        }
        return Iteration.iteration(in);
    }

    /** Returns iterator over all incoming edges to V. */
    public Iteration<Edge> inEdges(Vertex v) {
        ArrayList<Edge> in = new ArrayList<Edge>();
        for (Edge e: edges) {
            if (e.getV1().equals(v)) {
                in.add(e);
            }
        }
        return Iteration.iteration(in);
    }

    /** Returns outEdges(V). This is a synonym typically used
     *  on undirected graphs. */
    public final Iteration<Edge> edges(Vertex v) {
        return outEdges(v);
    }

    /** Returns the natural ordering on T, as a Comparator.  For
     *  example, if intComp = Graph.<Integer>naturalOrder(), then
     *  intComp.compare(x1, y1) is <0 if x1<y1, ==0 if x1=y1, and >0
     *  otherwise. */
    public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
    {
        return new Comparator<T>() {
            @Override
            public int compare(T x1, T x2) {
                return x1.compareTo(x2);
            }
        };
    }

    /** Returns an edge comparator given the ELabel comparator COMP. */
    private Comparator<Edge> edgeComparator(final Comparator<ELabel> comp) {
        Comparator<Edge> newComp = new Comparator<Edge>() {
            @Override
            public int compare(Edge e1, Edge e2) {
                int value = comp.compare(e1.getLabel(), e2.getLabel());
                if (value != 0) {
                    return value;
                } else {
                    return 1;
                }
            }
        };
        return newComp;
    }

    /** Cause subsequent calls to edges() to visit or deliver
     *  edges in sorted order, according to COMPARATOR. Subsequent
     *  addition of edges may cause the edges to be reordered
     *  arbitrarily.  */
    public void orderEdges(Comparator<ELabel> comparator) {
        Collections.sort(edges, edgeComparator(comparator));
    }

    /** The ArrayList of edges in this graph. */
    private ArrayList<Edge> edges = new ArrayList<Edge>();
    /** The ArrayList of vertices in this graph. */
    private ArrayList<Vertex> vertices = new ArrayList<Vertex>();

}
