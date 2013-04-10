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

}
