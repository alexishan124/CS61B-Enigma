package enigma;
import java.util.ArrayList;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Alexis Han
 */
class Permutation {
    /**
     * Stores the characters within this permutation.
     */
    private ArrayList<ArrayList> permutationchar = new ArrayList<>();
    /**
     * Stores the indices of the characters within this permutation.
     */
    private ArrayList<ArrayList> permutationindex = new ArrayList<>();
    /**
     * Accesses the cycles within this permutation.
     */
    private String _cycles;

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        String tempCycle = "";
        cycles = cycles.trim();
        for (int i = 0; i < cycles.length(); i += 1) {
            if (cycles.charAt(i) == '(') {
                continue;
            } else if (cycles.charAt(i) == ')') {
                addCycle(tempCycle);
                tempCycle = "";
            } else {
                tempCycle += cycles.charAt(i);
            }
        }
        ArrayList<Character> item = new ArrayList<>();
        ArrayList<Integer> intItem = new ArrayList<>();
        for (int i = 0; i < _alphabet.size(); i += 1) {
            for (ArrayList j: permutationchar) {
                if (!j.contains(_alphabet.toChar(i))) {
                    item.add(_alphabet.toChar(i));
                    intItem.add(i);
                }
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        ArrayList<Character> charItem = new ArrayList<>();
        ArrayList<Integer> intItem = new ArrayList<>();
        cycle = cycle.trim();
        for (int i = 0; i < cycle.length(); i += 1) {
            int index = _alphabet.toInt(cycle.charAt(i));
            charItem.add(cycle.charAt(i));
            intItem.add(index);
        }
        permutationchar.add(charItem);
        permutationindex.add(intItem);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        p = wrap(p);
        int output = p;
        for (ArrayList i: permutationindex) {
            if (i.contains(p)) {
                if ((Integer) i.indexOf(p) == (i.size() - 1)) {
                    return (Integer) i.get(0);
                } else {
                    return (Integer) i.get((Integer) i.indexOf(p) + 1);
                }
            } else if ((!i.contains(p)
                    && _alphabet.contains(_alphabet.toChar(p)))) {
                output = p;
            } else if (i.size() == 0 && permutationindex.size() == 1) {
                output = p;
            } else {
                output = p;
            }
        }
        return output;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        int output = c;
        for (ArrayList i: permutationindex) {
            if (i.contains(c)) {
                if ((Integer) i.indexOf(c) == 0) {
                    return (Integer) i.get(i.size() - 1);
                } else {
                    return (Integer) i.get((Integer) i.indexOf(c) - 1);
                }
            } else if (!i.contains(c)
                    && _alphabet.contains(_alphabet.toChar(c))) {
                output = c;
            } else if (i.size() == 0 && permutationindex.size() == 1) {
                output = c;
            } else {
                output = c;
            }
        }
        return output;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int toAlphabet = _alphabet.toInt(p);
        int index = wrap(toAlphabet);
        Character item = _alphabet.toChar(permute(index));
        return item;
    }


    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int toAlphabet = _alphabet.toInt(c);
        int index = wrap(toAlphabet);
        return _alphabet.toChar(invert(index));
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < _alphabet.size(); i += 1) {
            if (permute(i) == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;


}
