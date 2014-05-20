package enigma;
/** Class that represents a reflector in the enigma.
 *  @author Allen Yu
 */
class Reflector extends FixedRotor {

    /** Constructs a Reflector and sets the type of Reflector to type P.
     * (Assuming P is one of the types in rotorType) and default sets the
     * setting to 'A' position.*/
    public Reflector(String p) {
        super(p);
    }

    @Override
    boolean hasInverse() {
        return false;
    }

    /** Returns a useless value; should never be called. */
    @Override
    int convertBackward(int unused) {
        throw new UnsupportedOperationException();
    }

}
