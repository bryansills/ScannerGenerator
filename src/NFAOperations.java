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
  		Set<Character> newSet = new Set<Character>(nfa_a.getChars());
  		newSet.addAll(nfa_b.getChars());

  		//create new nfa to hold unioned nfas
  		NFA newNFA = new NFA(newSet);
  		List<NFAState> nextStates = Arrays.asList(nfa_a.getStartState(), nfa_b.getStartState());
  		
  		//set transition into a and b to null for empty string
  		nfa_a.getStartState().setTransition(null);
  		nfa_b.getStartState().setTransition(null);
  		newNFA.setStartState(new NFAState(false, new Set<Character>(), nextStates));

  		//create final state
  		NFAState finalState = new NFAState(true, null, null);
  		List<NFAState> acceptStates = nfa_a.findAcceptStates();
  		acceptStates.addAll(nfa_b.findAcceptStates);

  		//point a and b accept states to finalstate
  		for(NFAState acceptState : acceptStates) {
	  		acceptState.addNext(finalState);
	  		acceptState.setAccept(false);
	  	}

	  	return newNFA;
  	}

  	public static NFA star(NFA nfa_a) {

  	}

  	public static NFA plus(NFA nfa_a) {
  		return union(nfa_a, star(nfa_a));
  	}

}