import static org.junit.Assert.assertFalse;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Test;

public class NFATest {

  @Test
  public void simpleNFAConstructorShouldProduceValidNFA() {
    NFA digit = new NFA(new HashSet<Character>(
        Arrays.asList(new Character[]{'0', '1', '2'})));

    NFAState start = digit.getStartState();

    assertFalse(start.acceptsChar('0'));
    assert(start.next('0').get(0).isAccept());
  }

  @Test
  public void emptyStringAccept() {
      NFA result = new NFA(null);

      assert(result.accepts(null));
  }

  @Test
  public void simpleAccept() {
      NFA result = new NFA(new HashSet<Character>(Arrays.asList(new Character[]{'a', 'b', 'c'})));

      assert(result.accepts("a"));
      assert(result.accepts("b"));
      assertFalse(result.accepts("d"));
      assertFalse(result.accepts(null));
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

        assert(result.accepts("aaa"));
        assert(result.accepts("abc"));
        assert(result.accepts("cba"));
        assertFalse(result.accepts("aa"));
        assertFalse(result.accepts("aaaa"));
        assertFalse(result.accepts("xxxx"));
        assertFalse(result.accepts("aaax"));

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

        assert(result.accepts("aa"));
        assert(result.accepts("ac"));
        assert(result.accepts("ca"));
        assertFalse(result.accepts("a"));
        assertFalse(result.accepts("aaaa"));
        assertFalse(result.accepts("xxxx"));
        assertFalse(result.accepts("aaax"));

    }
}
