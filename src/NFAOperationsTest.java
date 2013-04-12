import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class NFAOperationsTest {

    @Test
    public void basicConcat() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(left, right);

        assert(result.accepts("ab"));
        assert(result.accepts("ac"));
        assert(result.accepts("aa"));
        assert(result.accepts("bb"));
        assertFalse(result.accepts("a"));
        assertFalse(result.accepts("aaa"));
    }

    @Test
    public void threeConcat() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA middle = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(NFAOperations.concat(left, middle), right);

        assert(result.accepts("aaa"));
        assert(result.accepts("abc"));
        assert(result.accepts("cba"));
        assertFalse(result.accepts("xxx"));
        assertFalse(result.accepts("axx"));
        assertFalse(result.accepts("xxa"));
        assertFalse(result.accepts("aa"));
        assertFalse(result.accepts("aaaa"));
    }

    @Test
    public void threeConcatWithMiddleEmpty() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA middle = new NFA(null);
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(NFAOperations.concat(left, middle), right);

        assert(result.accepts("aa"));
        assert(result.accepts("ac"));
        assert(result.accepts("ca"));
        assertFalse(result.accepts("xx"));
        assertFalse(result.accepts("ax"));
        assertFalse(result.accepts("xa"));
        assertFalse(result.accepts("a"));
        assertFalse(result.accepts("aaa"));
    }

    @Test
    public void basicUnion() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));

        NFA result = NFAOperations.union(left, right);

        assert(result.accepts("a"));
        assert(result.accepts("d"));
        assertFalse(result.accepts("x"));
        assertFalse(result.accepts("aa"));
        assertFalse(result.accepts("ae"));

    }
    @Test
    public void basicPlus() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.plus(left);

        assert(result.accepts("a"));
        assert(result.accepts("aa"));
        assert(result.accepts("ab"));
        assert(result.accepts("ba"));
        assert(result.accepts("bccc"));
        assertFalse(result.accepts(null));
        assertFalse(result.accepts("x"));
        assertFalse(result.accepts("ax"));
        assertFalse(result.accepts("xc"));
        assertFalse(result.accepts("cxa"));
    }

}
