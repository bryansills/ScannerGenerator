import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;


public class NFAState {

  public static class Builder {

    private Set<Character> transition;
    private List<NFAState> nextStates;
    private boolean accept;
    private boolean visited;
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

    private Builder setVisited(boolean visited) {
      this.visited = visited;
      return this;
    }

    private Builder setName(String name) {
      this.name = name;
      return this;
    }

    public NFAState build() {
      return new NFAState(this);
    }

  }

  private static int Count = 0;
  private Set<Character> transition;
  private List<NFAState> nextStates;
  private boolean accept;
  private boolean visited;
  private int id;
  private String name;

  private NFAState(Builder b) {
    this.id = Count++;
    this.accept = b.accept;
    this.transition = b.transition;
    this.nextStates = b.nextStates;
    this.visited = b.visited;
    this.name = b.name;
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
  public NFAState copy() {
    return NFAState.builder()
        .setAccept(accept)
        .setName(name)
        .setTransition(transition).build();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getId() {
    return id;
  }
  public boolean equals(NFAState other) {
    return id == other.id;
  }
  @Override
  public String toString() {
    String s = "";
    Queue<NFAState> queue = new LinkedList<NFAState>();
    queue.add(this);
    Set<Integer> visited = new HashSet<Integer>();
    visited.add(id);
    while (!queue.isEmpty()) {
      NFAState state = queue.remove();
      if (state.isAccept()) {
        s += "State: (" + state.id + ")";
      } else {
        s += "State: " + state.id;
      }
      List<NFAState> successors = state.getNextStates();
      if (!successors.isEmpty()) {
        s += " -> ";
      }
      for(NFAState next : successors) {
        if (!visited.contains(next.id)) {
          s += next.id + " in " + next.getTransition() + "\n";
          queue.add(next);
          visited.add(next.id);
        } else {
          s += next.id + " in " + next.getTransition() + "\n";
        }
      }
    }

    return s;

  }
}
