package trip;

import graph.Graph;
import graph.Weighted;

/** Represents a road on the map. Used as an ELabel in the graph _map in
 *  Main.java.
 *  @author Allen Yu
 */
public class Distance implements Weighted {

    /** Creates a Road called NAME with weight WEIGHT starting at vertex V0 and
     *  directed in DIRECTION. */
    public Distance(String name, double weight,
            Graph<Location, Distance>.Vertex V0,
            String direction) {
        _name = name;
        _weight = weight;
        _start = V0;
        _direction = direction;
    }

    @Override
    public double weight() {
        return _weight;
    }

    /** Returns the starting vertex of this road. */
    public Graph<Location, Distance>.Vertex start() {
        return _start;
    }

    /** Returns the direction of this road assuming it is starting from its
     *  _startVertex. */
    public String direction() {
        return _direction;
    }

    /** Returns the name of this road. */
    public String name() {
        return _name;
    }

    /** Returns the direction of this road starting from the provided vertex
     *  START. */
    public String direction(Graph<Location, Distance>.Vertex start) {
        if (start == _start) {
            return _direction;
        } else {
            return opposite(_direction);
        }
    }

    /** Returns the opposite direction to X. */
    private static String opposite(String x) {
        if (x.equals("north")) {
            return "south";
        } else if (x.equals("south")) {
            return "north";
        } else if (x.equals("east")) {
            return "west";
        } else {
            return "east";
        }
    }

    /** Double representing the length of this road. */
    private double _weight;
    /** Name of this road. */
    private String _name;
    /** Starting vertex for this road. */
    private Graph<Location, Distance>.Vertex _start;
    /** Direction for this road. */
    private String _direction;
}
