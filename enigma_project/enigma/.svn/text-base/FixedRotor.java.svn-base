package enigma;
/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Allen Yu
 */
class FixedRotor extends Rotor {

    /** Constructs a FixedRotor and sets the type of FixedRotor to type P.
     * (Assuming P is one of the types in rotorType) and default sets the
     * setting to 'A' position.*/
    public FixedRotor(String p) {
        super(p);
    }

    @Override
    boolean advances() {
        return false;
    }

    @Override
    boolean atNotch() {
        return false;
    }

    /** Fixed rotors do not advance. */
    @Override
    void advance() {
    }

}
