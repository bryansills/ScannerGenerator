import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;


public class DFA {
  private Map<DFAState, Map<Character, DFAState>> table;
  private DFAState start;
  public DFA() {
    table = new HashMap<DFAState, Map<Character, DFAState>>();
  }
  // performs epsilon closure
  public static DFAState nfaStateToDfaState(NFAState state) {
    DFAState start = new DFAState();
    start.add(state);

    Stack<NFAState> stack = new Stack<NFAState>();
    Set<NFAState> visited = new HashSet<NFAState>();
    List<NFAState> epsilons = new ArrayList<NFAState>();
    stack.add(state);
    visited.add(state);
    while (!stack.isEmpty()) {
      NFAState cur = stack.pop();
      List<NFAState> succ = cur.getNextStates();
      for (NFAState next : succ) {
        if (next.getTransition() == null) {
          stack.add(next);
          visited.add(next);
        }
      }
    }
    for (NFAState n : visited) {
      start.add(n);
    }
    return start;
  }
  public static void printTable(DFA d) {
    for (DFAState row : d.table.keySet()) {
      for (Entry<Character, DFAState> col : d.table.get(row).entrySet()) {
        System.out.println(row.getId() + ":" + row + " - " + col.getKey() +  " > " + col.getValue().getId() + ":" + col.getValue());
      }
    }
  }
  public static DFA nfaToDfa(NFA nfa) {
    Queue<DFAState> queue = new LinkedList<DFAState>();
    Set<DFAState> visited = new HashSet<DFAState>();
    Set<Integer> test = new HashSet<Integer>();
    DFAState start = nfaStateToDfaState(nfa.getStartState());
    queue.add(start);
    Map<DFAState, Map<Character, DFAState>> table =
        new HashMap<DFAState, Map<Character, DFAState>>();

    Map<Integer, DFAState> stateMap = new HashMap<Integer, DFAState>();

    while (!queue.isEmpty()) {
      DFAState cur = queue.remove();
      if (visited.contains(cur)) {
        continue;
      }
      visited.add(cur);

      Map<Character, DFAState> transitionMap = new HashMap<Character, DFAState>();
      List<NFAState> innerStates = new ArrayList<NFAState>(cur.getInnerStates());
      for (NFAState succ : innerStates) {
        for (NFAState after : succ.getNextStates()) {
          boolean seen = false;
          if (test.contains(after.getId())) {
            seen = true;
          }
          test.add(after.getId());
          if (after.getTransition() != null) {
            DFAState dfaAfter = nfaStateToDfaState(after);
            for (NFAState ns : dfaAfter.getInnerStates()) {
              if (stateMap.containsKey(ns.getId())) {
                DFAState existing = stateMap.get(ns.getId());
                existing.add(dfaAfter);
                dfaAfter = existing;
                break;
              } else {
                stateMap.put(ns.getId(), dfaAfter);
              }
            }
            for (Character c : after.getTransition()) {
              if (transitionMap.containsKey(c)) {
                transitionMap.get(c).add(dfaAfter);
              } else {
                transitionMap.put(c, dfaAfter);
              }
            }
            if (!seen) {
              queue.add(dfaAfter);
            }
          }
        }
      }
      table.put(cur, transitionMap);
    }

    DFA result = new DFA();
    result.setStart(start);
    result.setTable(table);

    return result;
  }

  public boolean accepts(String s) {
    DFAState cur = start;
    for (Character c : s.toCharArray()) {
      Map<Character, DFAState> col = table.get(cur);
      if (col.containsKey(c)) {
        cur = col.get(c);
      } else {
        return false;
      }
    }
    for (NFAState in : cur.getInnerStates()) {
      if (in.isAccept()) return true;
    }
    return false;
  }

  public Map<DFAState, Map<Character, DFAState>> getTable() {
    return table;
  }
  public void setTable(Map<DFAState, Map<Character, DFAState>> table) {
    this.table = table;
  }
  public DFAState getStart() {
    return start;
  }
  public void setStart(DFAState start) {
    this.start = start;
  }
}
