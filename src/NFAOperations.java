public class NFAOperations {

	public static NFA concat(NFA nfa_a, NFA nfa_b) {
	  	// set accept states isAccept to false 
	  	// and set next states to start state of b
	  	List<NFAState> acceptStates = nfa_a.findAcceptStates();

	  	for(NFAState acceptState : acceptStates) {
	  		acceptState.addNext(nfa_b.getStartState());
	  		acceptState.setAccept(false);
	  	}
	  	// set accept state of a next to start state of b

	  	return a;
  	}

  	public static NFA union(NFA nfa_a, NFA nfa_b) {

  	}

  	public static NFA star(NFA nfa_a) {

  	}

  	public static NFA plus(NFA nfa_a) {
  		return union(nfa_a, star(nfa_a));
  	}

}