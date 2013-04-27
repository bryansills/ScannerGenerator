import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MyDFAState {
  private Set<NFAState> innerStates;
  private Set<Character> transition;
  private boolean accept;
  private int id;
  private static int Count = 0;

  public MyDFAState() {
    id = Count++;
    innerStates = new HashSet<>();
    transition = new HashSet<>();
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
  public void add(MyDFAState d) {
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
  public boolean equals(MyDFAState other) {
    return other.id == id;
    /*boolean eq = true;
    for (NFAState inner : other.innerStates) {
      if (!innerStates.contains(inner)) {
        System.out.println("wtf");
        eq = false;
      }
    }
    return eq;*/
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
