package enigma;

/** Class that represents a rotor in the enigma machine.
 *  @author Allen Yu
 */
class Rotor {

    /* Instance variables. */

    /** The rotor type. */
    private String _type;

    /** My current setting (index 0..25, with 0 indicating that 'A'
     *  is showing). */
    private int _setting;

    /* Constructors. */

    /** Constructs a Rotor and sets the type of Rotor to type P.
     * and default sets the setting to 'A' position.*/
    public Rotor(String p) {
        _type = p;
        _setting = 0;
    }

    /* Methods involving rotor's setting. */

    /** Set the Rotor's setting to POSN.  */
    void set(int posn) {
        assert 0 <= posn && posn < ALPHABET_SIZE;
        _setting = posn;
    }

    /** Return the Rotor's setting. */
    int getSetting() {
        return _setting;
    }

    /* Methods involving rotor's type */

    /** Return the Rotor's type. */
    String getType() {
        return _type;
    }

    /* Methods involving alphabet. */

    /** Size of alphabet used for plaintext and ciphertext. */
    static final int ALPHABET_SIZE = 26;

    /** Assuming that P is an integer in the range 0..25, returns the
     *  corresponding upper-case letter in the range A..Z. */
    static char toLetter(int p) {
        return (char) (p + 'A');
    }

    /** Assuming that C is an upper-case letter in the range A-Z, return the
     *  corresponding index in the range 0..25. Inverse of toLetter. */
    static int toIndex(char c) {
        return (c - 'A');
    }

    /** Returns the modulated value if the original value X is greater than 26
     * or less than 0. */
    static int mod(int x) {
        if (x < 0 || x >= ALPHABET_SIZE) {
            return ((x + ALPHABET_SIZE) % ALPHABET_SIZE);
        } else {
            return x;
        }
    }

    /* Methods involving rotor's advancement. */

    /** Returns true iff this rotor has a ratchet and can advance. */
    boolean advances() {
        return true;
    }

    /** Advance me one position. */
    void advance() {
        if (_setting == ALPHABET_SIZE - 1) {
            _setting = 0;
        } else {
            _setting += 1;
        }
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        String[][] perm = PermutationData.ROTOR_SPECS;
        for (int i = 0; i < perm.length; i += 1) {
            if (_type.equals(perm[i][0])) {
                char[] nch = perm[i][3].toCharArray();
                if (nch.length > 1) {
                    int[] answer = {toIndex(nch[0]), toIndex(nch[1])};
                    return (_setting == answer[0] || _setting == answer[1]);
                } else {
                    int answer = toIndex(nch[0]);
                    return (_setting == answer);
                }
            }
        }
        return false;
    }

    /* Methods involving rotor's inverse capabilites. */

    /** Returns true iff this Rotor has a left-to-right inverse. */
    boolean hasInverse() {
        return true;
    }

    /* Methods involving rotor's conversion factors. */

    /** Return the conversion of P (an integer in the range 0..25)
     *  according to my permutation. */
    int convertForward(int p) {
        String[][] perm = PermutationData.ROTOR_SPECS;
        for (int i = 0; i < perm.length; i += 1) {
            if (_type.equals(perm[i][0])) {
                String str = perm[i][1];
                char[] perms = str.toCharArray();
                return Rotor.toIndex(perms[p]);
            }
        }
        return -1;
    }

    /** Return the conversion of E (an integer in the range 0..25)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        String[][] perm = PermutationData.ROTOR_SPECS;
        for (int i = 0; i < perm.length; i += 1) {
            if (_type.equals(perm[i][0])) {
                String str = perm[i][2];
                char[] perms = str.toCharArray();
                return Rotor.toIndex(perms[e]);
            }
        }
        return -1;
    }

    /** Returns the final output of the rotor given an INPUT for the
     *  rotor's current SETTING. This process is for the forward
     *  encryption process. */
    int encryptForward(int input, int setting) {
        input = mod(input);
        int first = mod(setting + input);
        int nxt = convertForward(first);
        int output = mod(nxt - setting);
        return output;
    }

     /** Returns the final output of the rotor given an INPUT for the
      *  rotor's current SETTING. This process is for the backward
      *  encryption process. */
    int encryptBackward(int input, int setting) {
        input = mod(input);
        int first = mod(setting + input);
        int nxt = convertBackward(first);
        int output = mod(nxt - setting);
        return output;
    }
}
