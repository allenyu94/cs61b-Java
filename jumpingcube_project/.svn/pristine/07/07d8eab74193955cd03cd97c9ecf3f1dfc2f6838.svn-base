package jump61;

import static jump61.Color.*;

/** A Square class that holds information of the square's current
 *  COLOR, number of SPOTS, and the number of NEIGHBORS.
 *  @author AllenYu
 */
public class Square {

    /** The current color of this square. */
    private Color _color;
    /** The current number of spots on this square. */
    private int _spots;

    /** Constructor of this Square class default the color
     * is white with 0 spot. The number of neighbors NUM is passed
     * into the constructor. */
    public Square() {
        _color = WHITE;
        _spots = 0;
    }

    /** Separate Constructor of this Square that can set the initial
     *  COLOR to RED, BLUE, or WHITE and set SPOTS to a value 0
     *  0 <= SPOTS < number of neighbors.
     */
    public Square(int spots, Color color) {
        _color = color;
        _spots = spots;
    }

    /** Sets the color of this square to NEWCOLOR. */
    public void setcolor(Color newcolor) {
        _color = newcolor;
    }

    /** Returns the color of this Square. */
    public Color color() {
        return _color;
    }

    /** Adds another spot to this Square. */
    public void addspot() {
        _spots += 1;
    }

    /** Sets spots to N amount. */
    public void setSpots(int n) {
        _spots = n;
    }

    /** Returns the number of spots of this Square. */
    public int spots() {
        return _spots;
    }

    /** Clears this current Square to 0 Spots and color WHITE. */
    public void clear() {
        setcolor(WHITE);
        _spots = 0;
    }

    @Override
    public String toString() {
        String str = "";
        if (_color == RED) {
            str = "r";
        } else if (_color == BLUE) {
            str = "b";
        }
        return "" + _spots + str;
    }
}
