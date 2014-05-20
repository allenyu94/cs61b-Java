package enigma;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import static org.junit.Assert.*;

import org.junit.Test;

/** Enigma simulator.
 *  @author Allen Yu
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified in the input from the standard input.  Print the
     *  results on the standard output. Exits normally if there are
     *  no errors in the input; otherwise with code 1. */
    public static void main(String[] unused) {
        Machine M;
        BufferedReader input =
            new BufferedReader(new InputStreamReader(System.in));

        buildRotors();

        M = null;

        try {
            while (true) {
                String line = input.readLine();
                if (line == null) {
                    break;
                }
                if (isConfigurationLine(line)) {
                    M = new Machine();
                    configure(M, line);
                } else if (M == null) {
                    System.exit(1);
                } else {
                    printMessageLine(M.convert(standardize(line)));
                }
            }
        } catch (IOException excp) {
            System.err.printf("Input error: %s%n", excp.getMessage());
            System.exit(1);
        }
    }

    /** Return true iff LINE is an Enigma configuration line. */
    private static boolean isConfigurationLine(String line) {
        return (line.startsWith("*"));
    }

    /** Configure M according to the specification given on CONFIG,
     *  which must have the format specified in the assignment. */
    private static void configure(Machine M, String config) {
        String[] newconfig = config.substring(2).split(" ");
        if (newconfig.length != 6) {
            System.exit(1);
        }
        if (newconfig[5].length() != 4) {
            System.exit(1);
        }
        char[] settings = newconfig[5].toCharArray();
        for (int i = 0; i < 5; i += 1) {
            for (int k = 0; k < PermutationData.ROTOR_SPECS.length; k += 1) {
                if (newconfig[i].equals(ROTORS[k].getType())) {
                    M.getRotors()[i] = ROTORS[k];
                    for (int j = i - 1; j > 0; j -= 1) {
                        if (ROTORS[k] == M.getRotors()[j]) {
                            System.exit(1);
                        }
                    }
                    if (i > 0) {
                        if ((settings[i - 1] - 'A') >= Rotor.ALPHABET_SIZE
                                || (settings[i - 1] - 'A') < 0) {
                            System.exit(1);
                        }
                        M.getRotors()[i].set(Rotor.toIndex(settings[i - 1]));
                    }
                }
            }
        }
        for (int x = 0; x < 5; x += 1) {
            if (x == 0 && !M.getRotors()[0].getType().equals("B")
                    && !M.getRotors()[x].getType().equals("C")) {
                System.exit(1);
            }
            if (x == 1 && !M.getRotors()[1].getType().equals("BETA")
                    && !M.getRotors()[1].getType().equals("GAMMA")) {
                System.exit(1);
            }
            if (x > 1 && x < 5 && !M.getRotors()[x].getType().equals("I")
                    && !M.getRotors()[x].getType().equals("II")
                    && !M.getRotors()[x].getType().equals("III")
                    && !M.getRotors()[x].getType().equals("IV")
                    && !M.getRotors()[x].getType().equals("V")
                    && !M.getRotors()[x].getType().equals("VI")
                    && !M.getRotors()[x].getType().equals("VII")
                    && !M.getRotors()[x].getType().equals("VIII")) {
                System.exit(1);
            }
        }
        for (int j = 0; j < 5; j += 1) {
            if (M.getRotors()[j] == null) {
                System.exit(1);
            }
        }
    }

    /** Return the result of converting LINE to all upper case,
     *  removing all blanks and tabs.  It is an error if LINE contains
     *  characters other than letters and blanks. */
    private static String standardize(String line) {
        return line.replaceAll(" ", "").toUpperCase();
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private static void printMessageLine(String msg) {
        while (msg.length() > 5) {
            System.out.print(msg.substring(0, 5) + " ");
            msg = msg.substring(5);
        }
        System.out.println(msg);
    }

    /**  Array of the Rotors of the Machine. */
    private static final Rotor[] ROTORS =
            new Rotor[PermutationData.ROTOR_SPECS.length];

    /** Create all the necessary rotors. */
    private static void buildRotors() {
        for (int i = 0; i < PermutationData.ROTOR_SPECS.length; i += 1) {
            if (i == 8) {
                ROTORS[i] = new FixedRotor(PermutationData.ROTOR_SPECS[i][0]);
            } else if (i == 10) {
                ROTORS[i] = new Reflector(PermutationData.ROTOR_SPECS[i][0]);
            } else {
                ROTORS[i] = new Rotor(PermutationData.ROTOR_SPECS[i][0]);
            }
        }
    }

    /* JUnit tests for the class Main. */

    /** Tests isConfigurationLine method. */
    @Test
    public void test1() {
        String configln = "* B BETA III IV I AXLE";
        assert (isConfigurationLine(configln));
    }

    /** Tests configure method and the convert method. */
    @Test
    public void test2() {
        buildRotors();
        String configln = "* B BETA I II III AAAA";
        String line = "Hello World";
        Machine T = new Machine();
        configure(T, configln);
        assertEquals(T.getRotors()[0].getType(), "B");
        assertEquals(T.getRotors()[1].getType(), "BETA");
        assertEquals(T.getRotors()[2].getType(), "I");
        assertEquals(T.getRotors()[3].getType(), "II");
        assertEquals(T.getRotors()[4].getType(), "III");
        assertEquals(T.getRotors()[1].getSetting(), 0);
        assertEquals(T.getRotors()[2].getSetting(), 0);
        assertEquals(T.getRotors()[3].getSetting(), 0);
        assertEquals(T.getRotors()[4].getSetting(), 0);
        assertEquals(T.convert(standardize(line)), "ILBDAAMTAZ");
    }

    /* JUnit tests for the class Rotor. */

    /** Tests getters and setters. */
    @Test
    public void test3() {
        Rotor R = new Rotor("I");
        assertEquals(R.getType(), "I");
        R.set(1);
        assertEquals(R.getSetting(), 1);
    }

    /** Tests convertForward and convertBackward methods. */
    @Test
    public void test4() {
        Rotor R = new Rotor("I");
        assertEquals(R.convertForward(Rotor.toIndex('C')), Rotor.toIndex('M'));
        assertEquals(R.convertBackward(Rotor.toIndex('M')), Rotor.toIndex('C'));
    }
}
