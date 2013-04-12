import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NFAOperationsTest {

    @Test
    public void basicConcat() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(left, right);

        assertTrue(result.accepts("ab"));
        assertTrue(result.accepts("ac"));
        assertTrue(result.accepts("aa"));
        assertTrue(result.accepts("bb"));
        assertFalse(result.accepts("a"));
        assertFalse(result.accepts("aaa"));
    }

    @Test
    public void threeConcat() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA middle = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(NFAOperations.concat(left, middle), right);

        assertTrue(result.accepts("aaa"));
        assertTrue(result.accepts("abc"));
        assertTrue(result.accepts("cba"));
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

        assertTrue(result.accepts("aa"));
        assertTrue(result.accepts("ac"));
        assertTrue(result.accepts("ca"));
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

        assertTrue(result.accepts("a"));
        assertTrue(result.accepts("d"));
        assertFalse(result.accepts("x"));
        assertFalse(result.accepts(null));
        assertFalse(result.accepts("aa"));
        assertFalse(result.accepts("ae"));

    }

    @Test
    public void threeUnion() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA middle = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'g', 'h', 'i'})));

        NFA result = NFAOperations.union(NFAOperations.union(left, middle), right);

        assertTrue(result.accepts("a"));
        assertTrue(result.accepts("d"));
        assertTrue(result.accepts("h"));
        assertFalse(result.accepts("x"));
        assertFalse(result.accepts(null));
        assertFalse(result.accepts("aa"));
        assertFalse(result.accepts("ga"));
    }

    @Test
    public void unionWithOverlappingCharacterSets() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA leftSecond = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'h', 'i'})));

        NFA result = NFAOperations.union(NFAOperations.concat(left, leftSecond), right);

        assertTrue(result.accepts("ad"));
        assertTrue(result.accepts("be"));
        assertTrue(result.accepts("a"));
        assertTrue(result.accepts("h"));
        assertFalse(result.accepts("b"));
        assertFalse(result.accepts("bh"));
        assertFalse(result.accepts("aa"));
        assertFalse(result.accepts("bbc"));
        assertFalse(result.accepts("bx"));
        assertFalse(result.accepts("xe"));
        assertFalse(result.accepts("e"));
        assertFalse(result.accepts("fb"));
    }

    @Test
    public void basicStar() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.star(left);

        assertTrue(result.accepts("a"));
        assertTrue(result.accepts("aa"));
        assertTrue(result.accepts("aaa"));
        assertTrue(result.accepts("abc"));
        assertTrue(result.accepts("acb"));
        assertTrue(result.accepts("bca"));
        assertTrue(result.accepts(null));
        assertFalse(result.accepts("x"));
        assertFalse(result.accepts("ax"));
        assertFalse(result.accepts("xa"));
        assertFalse(result.accepts("axa"));
        assertFalse(result.accepts("axb"));
        assertFalse(result.accepts("xbx"));
    }

    @Test
    public void starAndConcat() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));

        NFA result = NFAOperations.star(NFAOperations.concat(left, right));

        assertTrue(result.accepts("ad"));
        assertTrue(result.accepts("be"));
        assertTrue(result.accepts("adad"));
        assertTrue(result.accepts("bfbf"));
        assertTrue(result.accepts("adbf"));
        assertTrue(result.accepts("bdcdbf"));
        assertTrue(result.accepts(null));
        assertFalse(result.accepts("da"));
        assertFalse(result.accepts("ade"));
        assertFalse(result.accepts("aebb"));
        assertFalse(result.accepts("xd"));
        assertFalse(result.accepts("cx"));
        assertFalse(result.accepts("bfx"));
        assertFalse(result.accepts("xda"));
        assertFalse(result.accepts("cxa"));
        assertFalse(result.accepts("aecx"));
        assertFalse(result.accepts("bdxf"));
        assertFalse(result.accepts("cxaf"));
        assertFalse(result.accepts("xdbf"));
    }

    @Test
    public void starAndUnion() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));

        NFA result = NFAOperations.star(NFAOperations.union(left, right));

        assertTrue(result.accepts("a"));
        assertTrue(result.accepts("e"));
        assertTrue(result.accepts("bd"));
        assertTrue(result.accepts("fa"));
        assertTrue(result.accepts("aaa"));
        assertTrue(result.accepts("dbb"));
        assertTrue(result.accepts("efa"));
        assertTrue(result.accepts(null));
        assertFalse(result.accepts("x"));
        assertFalse(result.accepts("xa"));
        assertFalse(result.accepts("ex"));
        assertFalse(result.accepts("axe"));
    }

    @Test
    public void basicPlus() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.plus(left);

        assertTrue(result.accepts("a"));
        assertTrue(result.accepts("aa"));
        assertTrue(result.accepts("ab"));
        assertTrue(result.accepts("ba"));
        assertTrue(result.accepts("bccc"));
        assertFalse(result.accepts(null));
        assertFalse(result.accepts("x"));
        assertFalse(result.accepts("ax"));
        assertFalse(result.accepts("xc"));
        assertFalse(result.accepts("cxa"));
    }
}
