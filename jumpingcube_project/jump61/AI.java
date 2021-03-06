package jump61;

import java.util.ArrayList;

import static jump61.Color.*;

/** An automated Player.
 *  @author Allen Yu
 */
class AI extends Player {

    /** A new player of GAME initially playing COLOR that chooses
     *  moves automatically.
     */
    AI(Game game, Color color) {
        super(game, color);
    }

    @Override
    void makeMove() {
        Game game = getGame();
        Board r = getBoard();
        Board b = new MutableBoard(r);
        ArrayList<Integer> moves = new ArrayList<Integer>();
        int depth = 4;
        minmax(getColor(), b, depth, Integer.MAX_VALUE, moves);
        int pos = 0;
        if (!moves.isEmpty()) {
            pos = moves.get(game.randInt(moves.size()));
        } else {
            for (int i = 1; i < b.size() + 1; i++) {
                for (int c = 1; c < b.size() + 1; c++) {
                    if (b.isLegal(getColor(), i, c)) {
                        pos = b.sqNum(i, c);
                        break;
                    }
                }
            }
        }
        game.makeMove(pos);
        game.message("%s moves %d %d.%n", getColorString(getColor()),
                b.row(pos), b.col(pos));
    }

    /** Return the minimum of CUTOFF and the minmax value of board B
     *  (which must be mutable) for player P to a search depth of D
     *  (where D == 0 denotes evaluating just the next move).
     *  If MOVES is not null and CUTOFF is not exceeded, set MOVES to
     *  a list of all highest-scoring moves for P; clear it if
     *  non-null and CUTOFF is exceeded. the contents of B are
     *  invariant over this call. */
    private int minmax(Color p, Board b, int d, int cutoff,
                       ArrayList<Integer> moves) {
        if (moves == null) {
            moves = new ArrayList<Integer>();
        }
        moves.clear();
        if (d <= 0) {
            return staticEval(p, b);
        } else {
            int maxsofar = Integer.MIN_VALUE + 10;
            for (int i = 0; i < b.size() * b.size(); i++) {
                if (b.isLegal(p, i)) {
                    int value;
                    b.addSpot(p, i);
                    if (b.getWinner() != null) {
                        value = Integer.MAX_VALUE;
                    } else {
                        value = -minmax(p.opposite(), b, d - 1,
                                -maxsofar, null);
                    }
                    b.undo();
                    if (value > cutoff) {
                        moves.clear();
                        return value;
                    }
                    if (value > maxsofar) {
                        moves.clear();
                    }
                    if (value >= maxsofar) {
                        moves.add(i);
                        maxsofar = value;
                    }
                }
            }
            return maxsofar;
        }
    }

    /** Returns heuristic value of board B for player P.
     *  Higher is better for P. */
    private int staticEval(Color p, Board b) {
        int value = 0;
        value += b.numOfColor(p) - b.numOfColor(p.opposite()) * 10;
        value += lessNeighbors(p, b);
        return value;
    }

    /** Returns the heuristic value of board B for player P
     * for when the board still has empty corners. */
    private int lessNeighbors(Color p, Board b) {
        int currvalue = 0;
        int[] corners = findCorners(b);
        for (int x: corners) {
            if (b.getSquare(x).color() == p) {
                currvalue += 10;
            }
        }
        for (int y: findSides(b)) {
            if (b.getSquare(y).color() == p) {
                currvalue += 5;
            }
        }
        return currvalue;
    }

    /** Returns all the corners of the given board B.*/
    private int[] findCorners(Board b) {
        int[] corners = new int[4];
        corners[0] = 0;
        corners[1] = b.size() - 1;
        corners[2] = b.size() * b.size() - b.size();
        corners[3] = b.size() * b.size() - 1;
        return corners;
    }

    /** Returns all the sides of the given board B. */
    private int[] findSides(Board b) {
        int numSides;
        if (b.size() == 2) {
            numSides = 0;
        } else {
            numSides = (int) Math.pow(2.0, b.size() - 1);
        }
        int iter = 0;
        int [] sides = new int[numSides];
        for (int i = 0; i < b.size(); i++) {
            for (int k = 0; k < b.size(); k++) {
                if (b.neighbors(i, k) == 3) {
                    sides[iter] = b.sqNum(i, k);
                }
            }
        }
        return sides;
    }

    /** The board of this game. */
    private Board _board;
}

