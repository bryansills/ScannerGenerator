import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class NFAState {

  public static class Builder {

    private Set<Character> transition;
    private List<NFAState> nextStates;
    private boolean accept;
    private String name;

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

    public Builder setNextStates(List<NFAState> nextStates) {
      this.nextStates = nextStates;
      return this;
    }

    public Builder addNextState(NFAState s) {
      nextStates.add(s);
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public NFAState build() {
      return new NFAState(this);
    }

  }

  private Set<Character> transition;
  private List<NFAState> nextStates;
  private boolean accept;
  private String name;

  private NFAState(Builder b) {
    this.accept = b.accept;
    this.transition = b.transition;
    this.nextStates = b.nextStates;
    this.name = name;
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

    public String accepts(String str) {
        //if we have gone through the entire string and the current state is an accept
        //state, then the NFA accepts the string
        if (this.isAccept() && (str == null || str.length() == 0)) {
            if (name != null) {
                return name;
            } else {
                return "";
            }
        }

        String nfaName = null;
        Character nextChar = null;
        List<NFAState> nStates;
        if (!(str == null) && str.length() >= 1) {
            //else, get the next character in the string
            nextChar = str.charAt(0);

            nStates = next(nextChar);

            //if there are states that accept the next character, recursively call this method
            //on the str, minus the first character
            for (NFAState state : nStates) {
                String tempName = state.accepts(str.substring(1));
                if (tempName != null) {
                    if (name != null) {
                        nfaName = name;
                    } else {
                        nfaName = tempName;
                    }
                }
            }
        }

        //if there are no states that accept the next character, try using an empty string
        //transition with the full string
        nextChar = null;
        nStates = next(nextChar);

        for (NFAState state : nStates) {
            String tempName = state.accepts(str);
            if (tempName != null) {
                if (name != null) {
                    nfaName = name;
                } else {
                    nfaName = tempName;
                }
            }
        }

        //string might not be accepted by the NFA. go up the recursive stack
        return nfaName;
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

  public String getName() {
    return name;
  }

  public NFAState setName(String name) {
    this.name = name;
    return this;
  }

  public NFAState copy() {
    return NFAState.builder()
        .setAccept(accept)
        .setTransition(transition).build();
  }
}
