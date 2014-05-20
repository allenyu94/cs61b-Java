package trip;

import graph.Distancer;

/** Heuristic that calculates the "as-the-crow-files" distance
 *  between two locations.
 *  @author AllenYu
 */
public class StraightLineHeur implements Distancer<Location> {

    @Override
    public double dist(Location v0, Location v1) {
        double xDiff = v0.getXCoor() - v1.getXCoor();
        double yDiff = v0.getYCoor() - v1.getYCoor();
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
}
