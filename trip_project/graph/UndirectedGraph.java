package graph;

import graph.Graph.Edge;
import graph.Graph.Vertex;

import java.util.ArrayList;

/* Do not add or remove public or protected members, or modify the signatures of
 * any public methods.  You may add bodies to abstract methods, modify
 * existing bodies, or override inherited methods.  */

/** An undirected graph with vertices labeled with VLABEL and edges
 *  labeled with ELABEL.
 *  @author Allen Yu
 */
public class UndirectedGraph<VLabel, ELabel> extends Graph<VLabel, ELabel> {

    /** An empty graph. */
    public UndirectedGraph() {
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public int outDegree(Vertex v) {
        Iteration<Graph<VLabel, ELabel>.Edge> e = edges(v);
        int count = 0;
        while (e.hasNext()) {
            Graph<VLabel, ELabel>.Edge x = e.next();
            if (x.getV0() == v || x.getV1() == v) {
                count += 1;
            }
        }
        return count;
    }

    @Override
    public int inDegree(Vertex v) {
        return outDegree(v);
    }

    @Override
    public boolean contains(Vertex u, Vertex v) {
        for (Edge e: edges()) {
            if ((e.getV0() == u
                    || e.getV1() == u)
                    && (e.getV1() == v
                    || e.getV0() == v)
                    && u != v) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Vertex u, Vertex v,
                            ELabel label) {
        for (Edge e: edges()) {
            if ((e.getV0() == u
                    || e.getV1() == u)
                    && (e.getV1() == v
                    || e.getV0() == v)
                    && u != v
                    && e.getLabel().equals(label)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iteration<Vertex> successors(Vertex v) {
        Iteration<Edge> allOut = outEdges(v);
        ArrayList<Vertex> vtxlst = new ArrayList<Vertex>();
        for (Edge e: allOut) {
            vtxlst.add(e.getV1());
        }
        return Iteration.iteration(vtxlst);
    }

    @Override
    public Iteration<Vertex> predecessors(Vertex v) {
        return successors(v);
    }

    @Override
    public Iteration<Edge> outEdges(Vertex v) {
        ArrayList<Edge> myEdges = new ArrayList<Edge>();
        for (Edge e0: edges()) {
            if ((e0.getV0() == v || e0.getV1() == v)
                    && !(e0.getV0() == v && e0.getV1() == v)) {
                myEdges.add(e0);
            }
        }
        return Iteration.iteration(myEdges);
    }

    @Override
    public Iteration<Edge> inEdges(Vertex v) {
        return outEdges(v);
    }
}
