import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashSet;

import junit.framework.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class NFAOperationsTest {

    @Test
    public void basicConcat() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(left, right);

        assertNotNull(result.accepts("ab"));
        assertNotNull(result.accepts("ac"));
        assertNotNull(result.accepts("aa"));
        assertNotNull(result.accepts("bb"));
        assertNull(result.accepts("a"));
        assertNull(result.accepts("aaa"));
    }

    @Test
    public void threeConcat() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA middle = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(NFAOperations.concat(left, middle), right);

        assertNotNull(result.accepts("aaa"));
        assertNotNull(result.accepts("abc"));
        assertNotNull(result.accepts("cba"));
        assertNull(result.accepts("xxx"));
        assertNull(result.accepts("axx"));
        assertNull(result.accepts("xxa"));
        assertNull(result.accepts("aa"));
        assertNull(result.accepts("aaaa"));
    }

    @Test
    public void threeConcatWithMiddleEmpty() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA middle = new NFA(null);
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(NFAOperations.concat(left, middle), right);

        assertNotNull(result.accepts("aa"));
        assertNotNull(result.accepts("ac"));
        assertNotNull(result.accepts("ca"));
        assertNull(result.accepts("xx"));
        assertNull(result.accepts("ax"));
        assertNull(result.accepts("xa"));
        assertNull(result.accepts("a"));
        assertNull(result.accepts("aaa"));
    }

    @Test
    public void basicUnion() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));

        NFA result = NFAOperations.union(left, right);

        assertNotNull(result.accepts("a"));
        assertNotNull(result.accepts("d"));
        assertNull(result.accepts("x"));
        assertNull(result.accepts(null));
        assertNull(result.accepts("aa"));
        assertNull(result.accepts("ae"));

    }

    @Test
    public void threeUnion() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA middle = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'g', 'h', 'i'})));

        NFA result = NFAOperations.union(NFAOperations.union(left, middle), right);

        assertNotNull(result.accepts("a"));
        assertNotNull(result.accepts("d"));
        assertNotNull(result.accepts("h"));
        assertNull(result.accepts("x"));
        assertNull(result.accepts(null));
        assertNull(result.accepts("aa"));
        assertNull(result.accepts("ga"));
    }

    @Test
    public void unionWithOverlappingCharacterSets() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA leftSecond = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'h', 'i'})));

        NFA result = NFAOperations.union(NFAOperations.concat(left, leftSecond), right);

        assertNotNull(result.accepts("ad"));
        assertNotNull(result.accepts("be"));
        assertNotNull(result.accepts("a"));
        assertNotNull(result.accepts("h"));
        assertNull(result.accepts("b"));
        assertNull(result.accepts("bh"));
        assertNull(result.accepts("aa"));
        assertNull(result.accepts("bbc"));
        assertNull(result.accepts("bx"));
        assertNull(result.accepts("xe"));
        assertNull(result.accepts("e"));
        assertNull(result.accepts("fb"));
    }

    @Test
    public void basicStar() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.star(left);

        assertNotNull(result.accepts("a"));
        assertNotNull(result.accepts("aa"));
        assertNotNull(result.accepts("aaa"));
        assertNotNull(result.accepts("abc"));
        assertNotNull(result.accepts("acb"));
        assertNotNull(result.accepts("bca"));
        assertNotNull(result.accepts(null));
        assertNull(result.accepts("x"));
        assertNull(result.accepts("ax"));
        assertNull(result.accepts("xa"));
        assertNull(result.accepts("axa"));
        assertNull(result.accepts("axb"));
        assertNull(result.accepts("xbx"));
    }

    @Test
    public void starAndConcat() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));

        NFA result = NFAOperations.star(NFAOperations.concat(left, right));

        assertNotNull(result.accepts("ad"));
        assertNotNull(result.accepts("be"));
        assertNotNull(result.accepts("adad"));
        assertNotNull(result.accepts("bfbf"));
        assertNotNull(result.accepts("adbf"));
        assertNotNull(result.accepts("bdcdbf"));
        assertNotNull(result.accepts(null));
        assertNull(result.accepts("da"));
        assertNull(result.accepts("ade"));
        assertNull(result.accepts("aebb"));
        assertNull(result.accepts("xd"));
        assertNull(result.accepts("cx"));
        assertNull(result.accepts("bfx"));
        assertNull(result.accepts("xda"));
        assertNull(result.accepts("cxa"));
        assertNull(result.accepts("aecx"));
        assertNull(result.accepts("bdxf"));
        assertNull(result.accepts("cxaf"));
        assertNull(result.accepts("xdbf"));
    }

    @Test
    public void starAndUnion() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'d', 'e', 'f'})));

        NFA result = NFAOperations.star(NFAOperations.union(left, right));

        assertNotNull(result.accepts("a"));
        assertNotNull(result.accepts("e"));
        assertNotNull(result.accepts("bd"));
        assertNotNull(result.accepts("fa"));
        assertNotNull(result.accepts("aaa"));
        assertNotNull(result.accepts("dbb"));
        assertNotNull(result.accepts("efa"));
        assertNotNull(result.accepts(null));
        assertNull(result.accepts("x"));
        assertNull(result.accepts("xa"));
        assertNull(result.accepts("ex"));
        assertNull(result.accepts("axe"));
    }

    @Test
    public void basicPlus() {
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.plus(left);

        assertNotNull(result.accepts("a"));
        assertNotNull(result.accepts("aa"));
        assertNotNull(result.accepts("ab"));
        assertNotNull(result.accepts("ba"));
        assertNotNull(result.accepts("bccc"));
        assertNull(result.accepts(null));
        assertNull(result.accepts("x"));
        assertNull(result.accepts("ax"));
        assertNull(result.accepts("xc"));
        assertNull(result.accepts("cxa"));
    }
}
