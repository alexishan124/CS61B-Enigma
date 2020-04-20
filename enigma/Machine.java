package enigma;

import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Alexis Han
 */
class Machine {

    /**
     * Stores the number of rotors in Machine.
     */
    private int _numRotors;
    /**
     * Stores the number of pawls in Machine.
     */
    private int _pawls;
    /**
     * Accesses this alphabet.
     */
    private final Alphabet _alphabet;
    /**
     * Stores all the rotors provided into the Machine.
     */
    private Collection<Rotor> _allRotors;
    /**
     * Keeps track of the rotors that are actually being used.
     */
    private ArrayList<Rotor> usedRotors = new ArrayList<>();
    /**
     * the plugboard for the Machine: an instance of Permutation class.
     */
    private Permutation _plugboard;

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        this._numRotors = numRotors;
        this._pawls = pawls;
        this._allRotors = allRotors;
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors; }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /**
     * Returns the rotors actually being used in this machine.
     * @return ArrayList<Rotor>
     */
    public ArrayList<Rotor> getUsedRotors() {
        return usedRotors;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i += 1) {
            for (Rotor j: _allRotors) {
                if (rotors[i].toUpperCase().equals(j.name().toUpperCase())) {
                    j.set(0);
                    usedRotors.add(j);
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() < usedRotors.size() - 1) {
            throw new EnigmaException("Wheel settings too short.");
        }
        if (setting.length() > usedRotors.size() - 1) {
            throw new EnigmaException("Wheel settings too long.");
        }
        for (int i = 1; i < usedRotors.size(); i += 1) {
            usedRotors.get(i).set(_alphabet.toInt((setting.charAt(i - 1))));
        }
    }

    /**
     * Removes all rotors fed into the machine.
     */
    public void removeRotors() {
        usedRotors.clear();
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        this._plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        ArrayList<Rotor> toAdvance = new ArrayList<>();
        for (int i = usedRotors.size() - 1; i >= 0; i -= 1) {
            if (i == usedRotors.size() - 1) {
                toAdvance.add(usedRotors.get(i));
            }
            if (usedRotors.get(i).atNotch()
                    && i != numRotors() - numPawls()) {
                if (!toAdvance.contains(usedRotors.get(i))) {
                    toAdvance.add(usedRotors.get(i));
                }
                toAdvance.add(usedRotors.get(i - 1));
            }
        }
        for (Rotor r: toAdvance) {
            r.advance();
        }
        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        int numForward = numRotors() - 1;
        for (int i = numForward; i >= 0; i -= 1) {
            c = usedRotors.get(i).convertForward(c);
        }
        int numBackward = 1;
        while (numBackward < _numRotors) {
            c = usedRotors.get(numBackward).convertBackward(c);
            numBackward += 1;
        }
        if (_plugboard != null) {
            c = _plugboard.permute(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replaceAll("\\s+", "");
        String message = "";
        for (int i = 0; i < msg.length(); i += 1) {
            message += _alphabet.toChar(convert
                    (_alphabet.toInt(msg.charAt(i))));
        }
        return message;
    }
}
