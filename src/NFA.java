import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


/**
 * Heavily unfinished
 * @author jarv
 *
 */
public class NFA {
  private NFAState start;

  public NFA() {
    this.start = NFAState.builder()
    	.setIsStart(true)
        .build();
  }

  /**
   * Creates a primitive nfa based on a char class
   * @param chars the accepted characters from a char class
   */
  public NFA(Set<Character> chars) {
    NFAState acceptState = NFAState.builder()
        .setAccept(true)
        .setTransition(chars)
        .build();
    this.start = NFAState.builder()
        .setTransition(null) // empty transition.
        .addNextState(acceptState)
        .build();
  }


  /**
   * Returns a list of accept states in a given NFA.
   */
  public List<NFAState> findAcceptStates() {
  	List<NFAState> acceptStates = new ArrayList<NFAState>();

  	Set<NFAState> discovered = new HashSet<NFAState>();
  	Stack<NFAState> nextStatesToExplore = new Stack<NFAState>();
  	NFAState temp;

  	nextStatesToExplore.push(getStartState());

  	while(!nextStatesToExplore.empty()) {
  		temp = nextStatesToExplore.pop();

  		if(temp.isAccept()) {
  			acceptStates.add(temp);
  		}

  		for(NFAState nextState : temp.getNextStates()) {
  			if((!nextStatesToExplore.contains(nextState))
  				&& (!discovered.contains(nextState))) {
  				nextStatesToExplore.push(nextState);
  			}
  		}

  		discovered.add(temp);
  	}

  	return acceptStates;
  }
  public NFA copy() {
    NFA copy = new NFA();
    Map<NFAState, NFAState> oldToNew = new HashMap<NFAState, NFAState>();
    List<NFAState> oldStates = getAllStates();

    //create a map linking all the old states to the new copies
    for (NFAState oState : oldStates) {
        NFAState nState = oState.copy();
        oldToNew.put(oState, nState);
    }

    //below is a modified dfs
    Set<NFAState> discovered = new HashSet<NFAState>();
    Stack<NFAState> nextStatesToExplore = new Stack<NFAState>();
    NFAState old, newState;

    nextStatesToExplore.push(start);

    //traverse the old nfa
    while(!nextStatesToExplore.empty()) {
        old = nextStatesToExplore.pop();
        newState = oldToNew.get(old);

        for(NFAState nextState : old.getNextStates()) {
            //add all the next new states based on the old next states
            newState.addNext(oldToNew.get(nextState));

            if((!nextStatesToExplore.contains(nextState))
                    && (!discovered.contains(nextState))) {
                nextStatesToExplore.push(nextState);
            }
        }
        discovered.add(old);
    }

    copy.setStartState(oldToNew.get(start));
    return copy;
  }

  public List<NFAState> getAllStates() {
    List<NFAState> allStates = new ArrayList<NFAState>();

    Set<NFAState> discovered = new HashSet<NFAState>();
    Stack<NFAState> nextStatesToExplore = new Stack<NFAState>();
    NFAState temp;

    nextStatesToExplore.push(start);

    while(!nextStatesToExplore.empty()) {
        temp = nextStatesToExplore.pop();
        allStates.add(temp);

        for(NFAState nextState : temp.getNextStates()) {
            if((!nextStatesToExplore.contains(nextState))
                    && (!discovered.contains(nextState))) {
                nextStatesToExplore.push(nextState);
            }
        }

        discovered.add(temp);
    }

    return allStates;
  }
  public boolean accepts(String str) {
      return start.accepts(str);
  }

  public NFAState getStartState() {
  	return start;
  }

  public NFA setStartState(NFAState start) {
  	this.start = start;
    return this;
  }
}
