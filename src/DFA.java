import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class DFA {
	private DFAState start;
	private DFAState curr;
	private List<NFAState> nfaStates;
	private List<Set<Character>> transitionsSeen;
	private NFAState focus;
	
	public DFA(Set<Character> chars) {}
	
	/**
	 * Constructor that builds a DFA out of an NFA.
	 * @param nfa
	 */
	public DFA(NFA nfa) {
		nfaToDfa(nfa);
	}
	
	/**
	 * Takes in an NFA and converts it to a DFA
	 * @param nfa
	 */
	public void nfaToDfa(NFA nfa) {
		focus = nfa.getStartState();
		nfaStates = nfa.getAllStates();
		transitionsSeen = new ArrayList<Set<Character>>();
		List<NFAState> visited = new ArrayList<NFAState>();
		curr = new DFAState();
		
		/* Execute until you've visited every NFAState */
		while(!visited.equals(nfaStates)) {
			if(start == null) {
				start = new DFAState(0);
				curr = start;
			}
			
			explore(focus);
		}
	}
	
	/**
	 * A recursive helper method for nfaToDfa
	 * 
	 * @param focus
	 * @return
	 */
	public void explore(NFAState focus) {
		if(focus.getNextStates() == null) {
			return;
		}
		
		List<NFAState> nextStates = focus.getNextStates();
		
		for(NFAState nfaState: nextStates) {
			/* Check each adjecent state and see if its transition is epsilon.
			 * If it is, add the id of the NFAState to the DFAState. If the
			 * If the NFA state is accept, transfer that to the new DFA>
			 */
			if(nfaState.getTransition() == null) {
				curr.addToIdList(nfaStates.indexOf(nfaState)); // test this hard
				
				if(nfaState.isAccept()) {
					curr.setAccept(true);
				}
			}
			else {
				/*
				 * If you haven't seen that transition yet, add it to the DFA
				 */
				if(!transitionsSeen.contains(nfaState.getTransition())) {
					curr.getNextStates().add(new DFAState(false, nfaState.getTransition()));
					transitionsSeen.add(nfaState.getTransition());
					curr = curr.next(transitionsSeen.get(transitionsSeen.size() - 1));
					curr.getIdList().add(nfaStates.indexOf(nfaState));
					
					if(nfaState.isAccept()) {
						curr.setAccept(true);
					}
				}
				else {
					/*
					 * If you've seen a transition before, but it wasn't the last
					 * transition modified in your DFA
					 */
					for(DFAState state : this.getAllStates()) {
						if(state.next(nfaState.getTransition()).getTransition() == nfaState.getTransition()) {
							curr = state.next(nfaState.getTransition());
							curr.addToIdList(nfaStates.indexOf(focus));
							
							if(focus.isAccept()) {
								curr.setAccept(true);
							}
						}
					}
					
					for(NFAState nextState : focus.getNextStates()) {
						focus = nextState;
						explore(focus);
					}
				}
			}
		}
	}
	
	/**
	 * @return A list of all of the DFAStates in the DFA
	 */
	public List<DFAState> getAllStates() {
		List<DFAState> allStates = new ArrayList<DFAState>();
		
		Set<DFAState> discovered = new HashSet<DFAState>();
		Stack<DFAState> nextStatesToExplore = new Stack<DFAState>();
		DFAState temp;
		
		while(!nextStatesToExplore.empty()) {
			temp = nextStatesToExplore.pop();
			allStates.add(temp);
			
			for(DFAState nextState : temp.getNextStates()) {
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
