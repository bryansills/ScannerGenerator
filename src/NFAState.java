import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class NFAState {

  public static class Builder {

    private Set<Character> transition;
    private List<NFAState> nextStates;
    private boolean accept;
    private boolean isStart;
    private boolean visited;

    public Builder() {
      nextStates = new ArrayList<NFAState>();
    }

    /**
     * null means that it accepts epsilons
     * @param transition
     */
    public Builder setTransition(Set<Character> transition) {
      this.transition = transition;
      return this;
    }

    public Builder setAccept(boolean accept) {
      this.accept = accept;
      return this;
    }
    
    public Builder setIsStart(boolean start) {
    	this.isStart = start;
    	return this;
    }

    public Builder setNextStates(List<NFAState> nextStates) {
      this.nextStates = nextStates;
      return this;
    }

    public Builder addNextState(NFAState s) {
      nextStates.add(s);
      return this;
    }

    private Builder setVisited(boolean visited) {
      this.visited = visited;
      return this;
    }

    public NFAState build() {
      return new NFAState(this);
    }

  }

  private Set<Character> transition;
  private List<NFAState> nextStates;
  private boolean accept;
  private boolean isStart;
  private boolean visited;

  private NFAState(Builder b) {
	this.isStart = b.isStart;
    this.accept = b.accept;
    this.transition = b.transition;
    this.nextStates = b.nextStates;
    this.visited = b.visited;
  }

  public static Builder builder() {
    return new Builder();
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
    //if we have gone through the entire string and the current state is an accept
    //state, then the NFA accepts the string
    if (this.isAccept() && (str == null || str.length() == 0)) {
      return true;
    }

    boolean anyTrue = false;
    Character nextChar = null;
    List<NFAState> nStates;
    if (!(str == null) && str.length() >= 1) {
      //else, get the next character in the string
      nextChar = str.charAt(0);

      nStates = next(nextChar);

      //if there are states that accept the next character, recursively call this method
      //on the str, minus the first character
      for (NFAState state : nStates) {
        anyTrue = anyTrue || state.accepts(str.substring(1));
      }
    }

    //if there are no states that accept the next character, try using an empty string
    //transition with the full string
    nextChar = null;
    nStates = next(nextChar);

    for (NFAState state : nStates) {
      anyTrue = anyTrue || state.accepts(str);
    }

    //string might not be accepted by the NFA. go up the recursive stack
    return anyTrue;
  }

  /**
   * Get the characters this state will accept
   * @return
   */
  public Set<Character> getTransition() {
    return transition;
  }

  public void setTransition(Set<Character> transition) {
      this.transition = transition;
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
   * @return
   */
  public NFAState addNext(NFAState state) {
    nextStates.add(state);
    return this;
  }

  public List<NFAState> getNextStates() {
    return nextStates;
  }
  
  public boolean getIsStart() {
	  return isStart;
  }
  
  public NFAState copy() {
    return NFAState.builder()
        .setAccept(accept)
        .setTransition(transition).build();
  }
}
