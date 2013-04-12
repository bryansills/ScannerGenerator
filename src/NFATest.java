import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NFATest {

  @Test
  public void simpleNFAConstructorShouldProduceValidNFA() {
    NFA digit = new NFA(new HashSet<Character>(
        Arrays.asList(new Character[]{'0', '1', '2'})));

    NFAState start = digit.getStartState();

    assertFalse(start.acceptsChar('0'));
    assertTrue(start.next('0').get(0).isAccept());
  }

  @Test
  public void emptyStringAccept() {
      NFA result = new NFA(null);

      assertNotNull(result.accepts(null));
  }

  @Test
  public void simpleAccept() {
      NFA result = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

      assertNotNull(result.accepts("a"));
      assertNotNull(result.accepts("b"));
      assertNull(result.accepts("d"));
      assertNull(result.accepts(null));
  }

    @Test
    public void chainedAccept() {

        NFA result = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        result.getStartState().getNextStates().get(0).addNext(NFAState.builder()
                .setAccept(false)
                .setTransition(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})))
                .addNextState(NFAState.builder()
                        .setAccept(true)
                        .setTransition(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})))
                        .build())
                .build());

        assertNotNull(result.accepts("aaa"));
        assertNotNull(result.accepts("abc"));
        assertNotNull(result.accepts("cba"));
        assertNull(result.accepts("aa"));
        assertNull(result.accepts("aaaa"));
        assertNull(result.accepts("xxxx"));
        assertNull(result.accepts("aaax"));

    }

    @Test
    public void chainedAcceptWithEmpty() {

        NFA result = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));
        result.getStartState().getNextStates().get(0)
          .setAccept(false)
          .addNext(NFAState.builder()
              .setAccept(false)
              .setTransition(null)
              .addNextState(NFAState.builder()
                  .setAccept(true)
                  .setTransition(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})))
                  .build())
          .build());

        assertNotNull(result.accepts("aa"));
        assertNotNull(result.accepts("ac"));
        assertNotNull(result.accepts("ca"));
        assertNull(result.accepts("a"));
        assertNull(result.accepts("aaaa"));
        assertNull(result.accepts("xxxx"));
        assertNull(result.accepts("aaax"));

    }
}
