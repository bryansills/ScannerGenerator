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


public class MyDFA {
  private Map<MyDFAState, Map<Character, MyDFAState>> table;
  private MyDFAState start;
  public MyDFA() {
    table = new HashMap<MyDFAState, Map<Character, MyDFAState>>();
  }
  // don't forget to merge and have accept state thing
  // performs epsilon closure
  public static MyDFAState nfaStateToDfaState(NFAState state) {
    MyDFAState start = new MyDFAState();
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
  public static void printTable(MyDFA d) {
    for (MyDFAState row : d.table.keySet()) {
      for (Entry<Character, MyDFAState> col : d.table.get(row).entrySet()) {
        System.out.println(row.getId() + ":" + row + " - " + col.getKey() +  " > " + col.getValue().getId() + ":" + col.getValue());
      }
    }
  }
  public static MyDFA nfaToDfa(NFA nfa) {
    Queue<MyDFAState> queue = new LinkedList<MyDFAState>();
    Set<MyDFAState> visited = new HashSet<MyDFAState>();
    Set<Integer> test = new HashSet<Integer>();
    MyDFAState start = nfaStateToDfaState(nfa.getStartState());
    queue.add(start);
    Map<MyDFAState, Map<Character, MyDFAState>> table =
        new HashMap<MyDFAState, Map<Character, MyDFAState>>();

    Map<Integer, MyDFAState> stateMap = new HashMap<Integer, MyDFAState>();

    while (!queue.isEmpty()) {
      MyDFAState cur = queue.remove();
      if (visited.contains(cur)) {
        continue;
      }
      visited.add(cur);
      System.out.println(visited);
      System.out.println(cur);

      Map<Character, MyDFAState> transitionMap = new HashMap<Character, MyDFAState>();
      List<NFAState> innerStates = new ArrayList<NFAState>(cur.getInnerStates());
      for (NFAState succ : innerStates) {
        for (NFAState after : succ.getNextStates()) {
          boolean seen = false;
          if (test.contains(after.getId())) {
            seen = true;
          }
          test.add(after.getId());
          if (after.getTransition() != null) {
            MyDFAState dfaAfter = nfaStateToDfaState(after);
            for (NFAState ns : dfaAfter.getInnerStates()) {
              if (stateMap.containsKey(ns.getId())) {
                MyDFAState existing = stateMap.get(ns.getId());
                existing.add(dfaAfter);
                dfaAfter = existing;
                break;
              } else {
                stateMap.put(ns.getId(), dfaAfter);
              }
            }
            //System.out.println(after.getTransition() + " " + after.getId());
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
            // add all
          }
        }
      }
      table.put(cur, transitionMap);
      MyDFA asdf = new MyDFA();
      asdf.setTable(table);
      //printTable(asdf);
      //System.out.println("\n\n");
    }

    MyDFA d = new MyDFA();
    d.setStart(start);
    d.setTable(table);
    printTable(d);




    return d;
  }
  public boolean accepts(String s) {

    return validate(s.toCharArray());
  }
  public boolean validate(char[] string) {
      MyDFAState curr = start;
      for (int i = 0; i < string.length; i++) {
          //System.out.println(curr.getId() + " " + curr);
          Character c = string[i];
          Map<Character, MyDFAState> column = table.get(curr);
          //System.out.println("Char " + c);
          if (column.containsKey(c)) {
              curr = column.get(c);
          }
          else {
              //System.out.println(new String(string) + " fails");
              return false;
          }
      }
      //System.out.print("here goes nothing: ");
      /*for (NFAState in : curr.getInnerStates()) {
        if (in.getName() != null) {
          System.out.println(in.getName());
        }
      }*/
      /*NFAState thing = curr.getAcceptingState();
      if (thing != null && thing.isAccept()) {
        System.out.println("Accepted " + new String(string) + " as a " + thing.getName());
      } else {
        System.out.println("Did not accept " + new String(string));
      }*/
      for (NFAState in : curr.getInnerStates()) {
        if (in.isAccept()) return true;
      }

      return false;
  }
  public Map<MyDFAState, Map<Character, MyDFAState>> getTable() {
    return table;
  }
  public void setTable(Map<MyDFAState, Map<Character, MyDFAState>> table) {
    this.table = table;
  }
  public MyDFAState getStart() {
    return start;
  }
  public void setStart(MyDFAState start) {
    this.start = start;
  }


}
