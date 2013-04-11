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
				
				if(nfaState.isAccept()) {
					curr.setAccept(true);
				}
			}
			else {
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
