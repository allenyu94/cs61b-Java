package enigma;

/** Class that represents a complete enigma machine.
 *  @author Allen Yu
 */
class Machine {

    /** The Machine's Rotors. Initially, the rotor settings are all 'A'. */
    private Rotor[] _rotors;

    /** Returns the Machine's Rotors. */
    Rotor[] getRotors() {
        return _rotors;
    }

    /** Constructor for the Machine. */
    public Machine() {
        _rotors = new Rotor[5];
    }

    /** Set my rotors to (from left to right) ROTORS.  Initially, the rotor
     *  settings are all 'A'. */
    void replaceRotors(Rotor[] rotors) {
        for (int i = 0; i < rotors.length; i += 1) {
            _rotors[i] = rotors[i];
        }
    }

    /** Set my rotors according to SETTING, which must be a string of four
     *  upper-case letters. The first letter refers to the leftmost
     *  rotor setting.  */
    void setRotors(String setting) {
        for (int i = 0; i < 4; i += 1) {
            _rotors[i + 1].set(setting.charAt(i));
        }
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] message = msg.toCharArray();
        char[] newmsg = new char[msg.length()];
        int newout = 0;
        for (int i = 0; i < msg.length(); i += 1) {
            if (Rotor.toIndex(message[i]) >= Rotor.ALPHABET_SIZE
                    || Rotor.toIndex(message[i]) < 0) {
                System.exit(1);
            }
            if (_rotors[4].atNotch() && _rotors[3].atNotch()
                    && _rotors[2].atNotch()) {
                _rotors[3].advance();
                _rotors[2].advance();
            } else if (_rotors[3].atNotch() && _rotors[2].atNotch()) {
                _rotors[3].advance();
                _rotors[2].advance();
            } else if (_rotors[4].atNotch() && _rotors[2].atNotch()) {
                _rotors[3].advance();
                _rotors[2].advance();
            } else if (_rotors[4].atNotch() && _rotors[3].atNotch()) {
                _rotors[3].advance();
                _rotors[2].advance();
            } else if (_rotors[4].atNotch()) {
                _rotors[3].advance();
            } else if (_rotors[3].atNotch()) {
                _rotors[3].advance();
                _rotors[2].advance();
            }
            _rotors[4].advance();
            newout = _rotors[4].encryptForward(Rotor.toIndex(message[i]),
                    _rotors[4].getSetting());
            for (int k = 3; k > -1; k -= 1) {
                newout = _rotors[k].encryptForward(newout,
                        _rotors[k].getSetting());
            }
            for (int j = 1; j < 5; j += 1) {
                newout = _rotors[j].encryptBackward(newout,
                        _rotors[j].getSetting());
            }
            newmsg[i] = Rotor.toLetter(newout);
        }
        String answer = new String(newmsg);
        return answer;
    }
}
