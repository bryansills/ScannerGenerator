import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DFA {
	private DFAState start;
	private DFAState curr;
	private List<NFAState> nfaStates;
	private List<Set<Character>> transitionsSeen;
	private NFAState focus;
	
	public DFA(Set<Character> chars) {
		
		DFAState acceptState = new DFAState();
		
		
		//this.start = new DFAState(null, states);
	}
	
	public DFA(NFA nfa) {
		focus = nfa.getStartState();
		nfaStates = nfa.getAllStates();
		transitionsSeen = new ArrayList<Set<Character>>();
		List<NFAState> visited = new ArrayList<NFAState>();
		curr = new DFAState();
		
		while(!visited.equals(nfaStates)) {
			if(start == null) {
				start = new DFAState(0);
				curr = start;
			}
			
			explore(focus);
		}
	}
	
	/**
	 * A recursive helper method for the constructor
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
			if(nfaState.getTransition() == null) {
				curr.addToIdList(nfaStates.indexOf(nfaState)); // test this hard
			}
			else {
				if(!transitionsSeen.contains(nfaState.getTransition())) {
					curr.getNextStates().add(new DFAState(false, nfaState.getTransition()));
					transitionsSeen.add(nfaState.getTransition());
					curr = curr.next(transitionsSeen.get(transitionsSeen.size() - 1));
					curr.getIdList().add(nfaStates.indexOf(nfaState));
				}
				else {
					for(DFAState state : this.getAllStates()) {
						if(state.next(nfaState.getTransition()).getTransition() == nfaState.getTransition()) {
							curr = state.next(nfaState.getTransition());
							curr.addToIdList(nfaStates.indexOf(focus));
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
	
	public List<DFAState> getAllStates() {
		List<DFAState> states = new ArrayList<DFAState>();
		
		return states;
	}

}
