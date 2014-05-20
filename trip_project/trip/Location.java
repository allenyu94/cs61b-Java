package trip;

import graph.Weightable;

/** Represents a location on the map. Used as a VLabel in the graph _map in
 *  Main.java.
 *  @author Allen Yu
 */
public class Location implements Weightable {

    /** Creates a Place called NAME with weight WEIGHT and located at the x and
     *  Y coordinates X, Y. */
    public Location(String name, double weight, double x, double y) {
        _name = name;
        _weight = weight;
        _xCoor = x;
        _yCoor = y;
    }

    @Override
    public double weight() {
        return _weight;
    }

    @Override
    public void setWeight(double w) {
        _weight = w;
    }

    /** Returns the x coordinate of this place. */
    public double getXCoor() {
        return _xCoor;
    }

    /** Returns the y coordinate of this place. */
    public double getYCoor() {
        return _yCoor;
    }

    /** Returns the name of this place. */
    public String name() {
        return _name;
    }

    /** Weight of this location. */
    private double _weight;
    /** Name of this location .*/
    private String _name;
    /** X Coordiate of this location. */
    private double _xCoor;
    /** Y Coordiate of this location. */
    private double _yCoor;
}
