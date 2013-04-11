import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NFAOperationsTest {

    @Test
    public void basicConcat() {
//        NFAState leftAccept = new NFAState()
//                .setAccept(true)
//                .setTransition(new HashSet<Character>(Arrays.asList(new Character[]{'a'})));
//        NFAState leftStart = new NFAState().addNext(leftAccept);
        NFA left = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

//        NFAState rightAccept = new NFAState()
//                .setAccept(true)
//                .setTransition(new HashSet<Character>(Arrays.asList(new Character[]{'b'})));
//        NFAState rightStart = new NFAState().setTransition(null).addNext(rightAccept);
        NFA right = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

        NFA result = NFAOperations.concat(left, right);

        assertEquals(result.accepts("ab"), true);
    }
}
