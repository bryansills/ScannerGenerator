import java.util.Set;


/**
 * Heavily unfinished
 * @author jarv
 *
 */
public class NFA {
  private NFAState start;
  private Set<Character> chars;

  /**
   * Creates a primitive nfa based on a char class
   * @param chars the accepted characters from a char class
   */
  public NFA(Set<Character> chars) {
  	this.chars = chars;
  }

  /**
   * Returns a list of accept states in a given NFA.
   */
  public List<NFAState> findAcceptStates(NFA nfa) {
  	List<NFAState> acceptStates = [];

  	Set<NFAState> discovered = new HashSet<NFAState>();
  	Stack<NFAState> nextStatesToExplore = new Stack<NFAState>();
  	NFAState temp;

  	nextStatesToExplore.push(nfa.getStartState());

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

  public Set<Character> getChars() {
  	return chars;
  }

  public void setChars(Set<Character> chars) {
  	this.chars = chars;
  }
}
