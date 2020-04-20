package enigma;

import java.util.ArrayList;


/** Class that represents a rotating rotor in the enigma machine.
 *  @author Alexis Han
 */
class MovingRotor extends Rotor {

    /**
     * Accesses the permutation for this rotor.
     */
    private Permutation permutation;
    /**
     * Size of this alphabet.
     */
    private int size;
    /**
     * Setting of the rotor.
     */
    private int _setting;
    /**
     * A list of notches.
     */
    private ArrayList<Integer> notchesList = new ArrayList<>();

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        permutation = perm;
        size = perm.alphabet().size();
        _setting = 0;
        for (int i = 0; i < notches.length(); i += 1) {
            notchesList.add(perm.alphabet().toInt(notches.charAt(i)));
        }
    }

    @Override
    void advance() {
        _setting = (_setting + 1) % permutation.size();
    }

    @Override
    int convertForward(int p) {
        int enterRotor = p + _setting % size;
        int permuting = permutation.permute(enterRotor);
        int leaveRotor = (permuting - _setting) % size;
        if (leaveRotor < 0) {
            leaveRotor = leaveRotor + size;
        }
        return leaveRotor;
    }

    @Override
    int convertBackward(int c) {
        int enterRotor = c + _setting % size;
        int inverting = permutation.invert(enterRotor);
        int leaveRotor = (inverting - _setting) % size;
        if (leaveRotor < 0) {
            leaveRotor = leaveRotor + size;
        }
        return leaveRotor;
    }

    @Override
    boolean atNotch() {
        return notchesList.contains(_setting);
    }

    @Override
    void set(int posn) {
        _setting = posn;
    }

    @Override
    boolean rotates() {
        return true;
    }

}
