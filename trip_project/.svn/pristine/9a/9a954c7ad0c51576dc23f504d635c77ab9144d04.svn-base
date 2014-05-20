package trip;

import graph.DirectedGraph;
import graph.Distancer;
import graph.Graph;
import graph.Graphs;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/** Initial class for the 'trip' program.
 *  @author Allen Yu
 */
public final class Main {

    /** Entry point for the CS61B trip program.  ARGS may contain options
     *  and targets:
     *      [ -m MAP ] [ -o OUT ] [ REQUEST ]
     *  where MAP (default Map) contains the map data, OUT (default standard
     *  output) takes the result, and REQUEST (default standard input) contains
     *  the locations along the requested trip.
     */
    public static void main(String... args) {
        String mapFileName;
        String outFileName;
        String requestFileName;

        mapFileName = "Map";
        outFileName = requestFileName = null;

        int a;
        for (a = 0; a < args.length; a += 1) {
            if (args[a].equals("-m")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    mapFileName = args[a];
                }
            } else if (args[a].equals("-o")) {
                a += 1;
                if (a == args.length) {
                    usage();
                } else {
                    outFileName = args[a];
                }
            } else if (args[a].startsWith("-")) {
                usage();
            } else {
                break;
            }
        }

        if (a == args.length - 1) {
            requestFileName = args[a];
        } else if (a > args.length) {
            usage();
        }

        if (requestFileName != null) {
            try {
                System.setIn(new FileInputStream(requestFileName));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s.%n", requestFileName);
                System.exit(1);
            }
        }

        if (outFileName != null) {
            try {
                System.setOut(new PrintStream(new FileOutputStream(outFileName),
                        true));
            } catch  (FileNotFoundException e) {
                System.err.printf("Could not open %s for writing.%n",
                        outFileName);
                System.exit(1);
            }
        }

        trip(mapFileName);
    }

    /** Print a trip for the request on the standard input to the standard
     *  output, using the map data in MAPFILENAME. */
    private static void trip(String mapFileName) {
        Reader mapParser = null;
        try {
            mapParser = new FileReader(mapFileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No file found");
        }
        makeMap(mapParser);

        Scanner reqParser = new Scanner(System.in);
        _requests = new ArrayList<Graph<Location, Distance>.Vertex>();
        while (reqParser.hasNext()) {
            String next = reqParser.next();
            next = trim(next);
            if (!_locs.containsKey(next)) {
                reqParser.close();
                throw new RuntimeException("Destination not established");
            }
            _requests.add(_locs.get(next));
        }
        reqParser.close();
        if (_requests.size() < 2) {
            throw new RuntimeException("Too few locations specified");
        }

        System.out.println("From " + _requests.get(0).getLabel().name() + ":");
        System.out.println();

        _count = 1;
        Distancer<Location> heuristic = new StraightLineHeur();
        for (int i = 0; i < _requests.size() - 1; i++) {
            Graph<Location, Distance>.Vertex v0 = _requests.get(i);
            Graph<Location, Distance>.Vertex v1 = _requests.get(i + 1);
            List<Graph<Location, Distance>.Edge> bestPath =
                    Graphs.shortestPath(_map, v0, v1, heuristic);
            if (v1.getLabel().weight() == Double.POSITIVE_INFINITY) {
                throw new RuntimeException("Destination unreachable");
            }
            outputRoad(bestPath, v1);
        }
    }

    /** Reads the map assigned to Reader MAPREADER and creates a graph based
     *  on the vertices and edges in the map. */
    private static void makeMap(Reader mapReader) {
        Scanner mapParser = new Scanner(mapReader);
        String nextToken;
        while (mapParser.hasNext()) {
            nextToken = mapParser.next();
            if (nextToken.equals("R")) {
                addDist(mapParser);
            } else if (nextToken.equals("L")) {
                addLoc(mapParser);
            } else {
                throw new RuntimeException("Invalid entry token");
            }
        }
        mapParser.close();
    }

    /** Makes a place based on the next five words (in free format) in Scanner
     *  MAPPARSER. Called by makeMap() when a "R" token is found. */
    private static void addLoc(Scanner mapParser) {
        try {
            String name = mapParser.next();
            double x = mapParser.nextDouble();
            double y = mapParser.nextDouble();
            Graph<Location, Distance>.Vertex v =
                    _map.add(new Location(name, Double.MAX_VALUE, x, y));
            _locs.put(name, v);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Invalid location entry");
        }
    }

    /** Makes a road based on the next five words (in free format) in Scanner
     *  MAPPARSER. Called by makeMap() when a "R" token is found. */
    private static void addDist(Scanner mapParser) {
        try {
            String place0 = mapParser.next();
            String roadName = mapParser.next();
            double dist = mapParser.nextDouble();
            if (dist <= 0) {
                throw new RuntimeException("Distance must be positive");
            }
            if (!mapParser.hasNext("NS|EW|WE|SN")) {
                throw new RuntimeException("Missing direction");
            }
            String dir = mapParser.next();
            String place1 = mapParser.next();
            Graph<Location, Distance>.Edge road =
                    _map.add(getVert(place0), getVert(place1),
                    new Distance(roadName, dist, getVert(place0), word(dir)));
            Graph<Location, Distance>.Edge other =
                    _map.add(getVert(place1), getVert(place0),
                    new Distance(roadName, dist, getVert(place0),
                            switchDir(word(dir))));
            _distances.put(roadName, road);
            _distances.put(roadName, other);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Invalid road entry");
        }
    }

    /** Takes it input string S and removes the last character if it is a
     *  comma. Returns the new string. */
    private static String trim(String s) {
        if (s.charAt(s.length() - 1) == ',') {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    /** Prints out the best path to GOAL based on the path provided by BEST. */
    private static void outputRoad(List<Graph<Location, Distance>.Edge> best,
            Graph<Location, Distance>.Vertex goal) {
        String storedDir = "";
        String storedName = "";
        double storedWeight = 0.0;
        Distance curr = null;
        ArrayList<String> bestPath = new ArrayList<String>();
        for (int i = 0; i < best.size(); i++) {
            curr = best.get(i).getLabel();
            String currDir = curr.direction(curr.start());
            if (curr.name().equals(storedName) && currDir.equals(storedDir)) {
                storedWeight += curr.weight();
            } else {
                if (i != 0) {
                    bestPath.add(_count + ". Take " + storedName + " "
                            + storedDir + " for "
                            + String.format("%.1f", storedWeight) + " miles.");
                    _count += 1;
                }
                storedDir = currDir;
                storedWeight = curr.weight();
                storedName = curr.name();
            }
        }
        bestPath.add(_count + ". Take " + storedName + " "
                + storedDir + " for " + String.format("%.1f", storedWeight)
                + " miles to " + goal.getLabel().name() + ".");
        _count += 1;
        for (String s : bestPath) {
            System.out.println(s);
        }
    }

    /** Returns the full word of the direction, given the two character
     *  input X. */
    private static String word(String x) {
        if (x.equals("NS")) {
            return "south";
        } else if (x.equals("SN")) {
            return "north";
        } else if (x.equals("EW")) {
            return "west";
        } else {
            return "east";
        }
    }

    /** Returns the opposite direction to X. */
    private static String switchDir(String x) {
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

    /** Returns the vertex that corresponds to String NAME. The vertex must
     *  already exist in _locs. */
    private static Graph<Location, Distance>.Vertex getVert(String name) {
        if (_locs.containsKey(name)) {
            return _locs.get(name);
        } else {
            throw new RuntimeException("Invalid destination");
        }
    }

    /** Print a brief usage message and exit program abnormally. */
    private static void usage() {
        System.out.println("An error has ocurred");
        System.exit(1);
    }

    /** Tracks which instruction number we currently are on. */
    private static int _count;
    /** Graph used to represent the map. */
    private static DirectedGraph<Location, Distance> _map =
            new DirectedGraph<Location, Distance>();
    /** Hashmap used to connect strings of location names to their
     *  corresponding vertices. */
    private static HashMap<String, Graph<Location, Distance>.Vertex> _locs =
            new HashMap<String, Graph<Location, Distance>.Vertex>();
    /** Hashmap used to connect strings of road names to their
     *  corresponding edges. */
    private static HashMap<String, Graph<Location, Distance>.Edge> _distances =
            new HashMap<String, Graph<Location, Distance>.Edge>();
    /** ArrayList used to store the requests made by input. */
    private static ArrayList<Graph<Location, Distance>.Vertex> _requests;
}
