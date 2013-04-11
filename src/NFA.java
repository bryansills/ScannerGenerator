import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;


/**
 * Heavily unfinished
 * @author jarv
 *
 */
public class NFA {
  private NFAState start;

  /**
   * Creates a primitive nfa based on a char class
   * @param chars the accepted characters from a char class
   */
  public NFA(Set<Character> chars) {
    NFAState acceptState = new NFAState();
    acceptState.setTransition(chars); // TODO(magicjarvis): use builder pattern. this is bad.
    List<NFAState> states = new ArrayList<NFAState>();
    states.add(acceptState);
    this.start = new NFAState(null, states); // create the start state
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

  public NFAState getStartState() {
  	return start;
  }

  public void setStartState(NFAState start) {
  	this.start = start;
  }
  
  public List<NFAState> getAllStates() {
	  List<NFAState> allStates = new ArrayList<NFAState>();
	  
	  Set<NFAState> discovered = new HashSet<NFAState>();
	  Stack<NFAState> nextStatesToExplore = new Stack<NFAState>();
	  NFAState temp;
	  
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
}
