import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DFAState {
  private Set<NFAState> innerStates;
  private Set<Character> transition;
  private boolean accept;
  private int id;
  private static int Count = 0;

  public DFAState() {
    id = Count++;
    innerStates = new HashSet<NFAState>();
    transition = new HashSet<Character>();
  }

  public Set<NFAState> getInnerStates() {
    return innerStates;
  }
  public void setInnerStates(Set<NFAState> innerStates) {
    this.innerStates = innerStates;
  }
  public Set<Character> getTransition() {
    return transition;
  }
  public void setTransition(Set<Character> transition) {
    this.transition = transition;
  }
  public boolean isAccept() {
    return accept;
  }
  public void setAccept(boolean accept) {
    this.accept = accept;
  }
  public void add(NFAState s) {
    innerStates.add(s);
  }
  public void add(DFAState d) {
    innerStates.addAll(d.innerStates);
  }

  @Override
  public String toString() {
    List<Integer> name = new ArrayList<Integer>();
    for (NFAState s : innerStates) {
      name.add(s.getId());
    }
    return name.toString();
  }
  public int getId() {
    return id;
  }
  public boolean equals(DFAState other) {
    return other.id == id;
  }
  public NFAState getAcceptingState() {
    for (NFAState n : innerStates) {
      if (n.isAccept()) {
        return n;
      }
    }
    return null;
  }
}
