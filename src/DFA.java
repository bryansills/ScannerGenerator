import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class DFA {
	private DFAState start;
	private DFAState curr;
	private List<NFAState> nfaStates;
	private List<Set<Character>> transitionsSeen;
	private NFAState focus;
	private List<NFAState> visited;
	
	public DFA() {}
	
	public DFA(Set<Character> chars) {}
	
	/**
	 * Constructor that builds a DFA out of an NFA.
	 * @param nfa
	 */
	public DFA(NFA nfa) {
		nfaToDfa(nfa);
	}
	
	public DFAState getStartState() {
		return start;
	}
	
	public void setStartState(DFAState newStart) {
		start = newStart;
	}
	
	/**
	 * Takes in an NFA and converts it to a DFA
	 * @param nfa
	 */
	public void nfaToDfa(NFA nfa) {
		focus = nfa.getStartState();
		nfaStates = nfa.getAllStates();
		transitionsSeen = new ArrayList<Set<Character>>();
		visited = new ArrayList<NFAState>();
		start = DFAState.builder()
				.setFirstId(nfaStates.indexOf(focus))
				.setIsStart(true)
				.build();
		this.setStartState(start);
		visited.add(nfa.getStartState());
		
		for(NFAState state : nfa.getStartState().getNextStates()) {
			curr = start;
			curr.addToIdList(nfaStates.indexOf(state));
			explore(state);
		}
	}
	
	/**
	 * A recursive helper method for nfaToDfa
	 * 
	 * @param focus
	 * @return
	 */
	public void explore(NFAState state) {
		visited.add(state);
		for(NFAState nextState : state.getNextStates()) {
			if(nextState.getTransition() == null) {
				curr.addToIdList(nfaStates.indexOf(state));
				
				if(state.isAccept()) {
					curr.setAccept(true);
				}
				if(state.getIsStart()) {
					curr.setIsStart(true);
				}
			}
			else if(!transitionsSeen.contains(nextState.getTransition())) {
				curr.addNext(DFAState.builder()
						.setAccept(false)
						.setTransition(nextState.getTransition())
						.build());
				
				transitionsSeen.add(nextState.getTransition());
				curr = curr.next(nextState.getTransition());
				curr.addToIdList(nfaStates.indexOf(state));
				
				if(state.isAccept()) {
					curr.setAccept(true);
				}
				if(state.getIsStart()) {
					curr.setIsStart(true);
				}
			}
			else {
				for(DFAState d : this.getAllStates()) {
					if(d.getTransition() == nextState.getTransition()) {
						curr = d;
						curr.addToIdList(nfaStates.indexOf(state));
						
						if(state.isAccept()) {
							curr.setAccept(true);
						}
						if(state.getIsStart()) {
							curr.setIsStart(true);
						}
					}
				}
			}
			explore(nextState);
		}
	}
	
	/**
	 * @return A list of all of the DFAStates in the DFA
	 */
	public List<DFAState> getAllStates() {
		List<DFAState> allStates = new ArrayList<DFAState>();
		
		Set<DFAState> discovered = new HashSet<DFAState>();
		Stack<DFAState> nextStatesToExplore = new Stack<DFAState>();
		DFAState temp = this.getStartState();
		nextStatesToExplore.push(temp);
		
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
