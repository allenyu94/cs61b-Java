package jump61;

import static org.junit.Assert.*;
import static jump61.Color.*;
import org.junit.Test;

/** A unit test of Square.
 * @author AllenYu
 */

public class SquareTest {

    @Test
    public void test1() {
        Square square = new Square();
        assertEquals("Wrong default color.", square.color(), WHITE);
        assertEquals("Wrong default spots.", square.spots(), 0);
    }

    @Test
    public void test2() {
        Square square = new Square(2, BLUE);
        assertEquals("Wrong constructor input.", square.spots(), 2);
        assertEquals("Wrong constructor input.", square.color(), BLUE);
    }

    @Test
    public void test3() {
        Square square = new Square();
        square.setcolor(RED);
        assertEquals("problem with set color", square.color(), RED);
        square.setSpots(3);
        assertEquals("problem with set spots.", square.spots(), 3);
    }

    @Test
    public void test4() {
        Square square = new Square(1, RED);
        square.clear();
        assertEquals("problem with clear.", square.color(), WHITE);
        assertEquals("problem with clear.", square.spots(), 0);
    }
}
