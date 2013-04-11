import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NFAState {
  private Set<Character> transition;
  private List<NFAState> nextStates;
  private boolean accept;

  public NFAState(boolean accept, Set<Character> transition, List<NFAState> nextStates) {
    this.accept = accept;
    this.transition = transition;
    this.nextStates = nextStates;

  }

  private NFAState(boolean accept, Set<Character> transition) {
    this(accept, transition, new ArrayList<NFAState>());
  }

  /**
   * If you make a state without a transition. that defaults to an accept state.
   */
  public NFAState() {
    this(true, new HashSet<Character>());
  }

  /**
   * Creates a state and sets its transition cost (based on char class)
   * null means that it accepts empty
   * @param transition
   */
  public NFAState(Set<Character> transition) {
    this(false, transition);
  }

  public NFAState(Set<Character> transition, List<NFAState> nextStates) {
    this(false, transition, nextStates);
  }

  /**
   * The states we enter after accepting a character
   * @param c
   * @return list of states we go to from c
   */
  public List<NFAState> next(Character c) {
    List<NFAState> states = new ArrayList<NFAState>();
    for (NFAState n : nextStates) {
      if (n.acceptsChar(c)) {
        states.add(n);
      }
    }
    return states;
  }


  public boolean accepts(String str) {
    // TODO(bryansills):FINISH THIS
    if (str.length() == 0 && this.isAccept()) {
      return true;
    }
    Character next = str.charAt(0);

    return false;
  }

  /**
   * Get the characters this state will accept
   * @return
   */
  public Set<Character> getTransition() {
    return transition;
  }

  public NFAState setTransition(Set<Character> transition) {
      this.transition = transition;
      return this;
  }

  public boolean isAccept() {
    return accept;
  }

  /**
   * Tells you whether or not this state accepts a particular character.
   * @param c
   * @return
   */
  public boolean acceptsChar(Character c) {
    return transition == null && c == null
        || transition != null && transition.contains(c);
  }

  /**
   * Is this an accept state or not?
   * @param b
   */
  public NFAState setAccept(boolean b) {
    accept = b;
    return this;
  }

  /**
   * Adds a state that the current state points to.
   * @param state
   */
  public NFAState addNext(NFAState state) {
    nextStates.add(state);
    return this;
  }

  public List<NFAState> getNextStates() {
    return nextStates;
  }
}
