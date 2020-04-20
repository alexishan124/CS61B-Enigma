package enigma;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Alexis Han
 */
class Rotor {

    /**
     * Accesses the setting for this rotor.
     */
    private int _setting;
    /**
     * Size of this alphabet.
     */
    private int _size;

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _size = perm.alphabet().size();
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return _name.contains("V") || _name.contains("I");
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return !rotates() && _name.length() == 1;
    }

    /** Return my current setting. */
    int setting() {
        return _setting; }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        int position = _permutation.alphabet().toInt(cposn);
        _setting = position;
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int enterRotor = p + _setting % _size;
        int permuting = _permutation.permute(enterRotor);
        int leaveRotor = (permuting - _setting) % _size;
        if (leaveRotor < 0) {
            leaveRotor = leaveRotor + _size;
        }
        return leaveRotor;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int enterRotor = e + _setting % _size;
        int inverting = _permutation.invert(enterRotor);
        int leaveRotor = (inverting - _setting) % _size;
        if (leaveRotor < 0) {
            leaveRotor = leaveRotor + _size;
        }
        return leaveRotor;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
        if ((rotates())) {
            _setting = (_setting + 1) % _permutation.size();
        }
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

}
