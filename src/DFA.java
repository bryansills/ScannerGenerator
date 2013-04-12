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
	
	private NFAState forkHack;
	
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
		//System.out.println(nfaStates.size());
		transitionsSeen = new ArrayList<Set<Character>>();
		visited = new ArrayList<NFAState>();
		//forkHack = NFAState.builder().build();
		//curr = DFAState.builder().build();  // necessary?
		
		start = DFAState.builder()
				.setFirstId(nfaStates.indexOf(focus))
				.setIsStart(true)
				.build();
		this.setStartState(start);
		curr = start;
		
		//visited.add(focus);
		//System.out.println(visited.get(0));
		
		/* Execute until you've visited every NFAState */
		while(visited.size() <= nfaStates.size()) {		
			explore(focus);
		}
		//System.out.println(nfa.getAllStates().size() + " " + visited.size());
	}
	
	/**
	 * A recursive helper method for nfaToDfa
	 * 
	 * @param focus
	 * @return
	 */
	public void explore(NFAState focus) {
		visited.add(focus);
		
		if(focus.getNextStates().isEmpty()) {
			//System.out.println("whaaat");
			//System.out.println(this.getAllStates().size());
			return;
		}
		
		List<NFAState> nextStates = focus.getNextStates();
		/*if(nextStates.size() > 1) {
			forkHack = focus;
		}*/

		for(NFAState nfaState: nextStates) {
			
			/* 
			 * Check each adjacent state and see if its transition is epsilon.
			 * If it is, add the id of the NFAState to the DFAState. If the
			 * If the NFA state is accept, transfer that to the new DFA>
			 */
			if(nfaState.getTransition() == null) {
				curr.addToIdList(nfaStates.indexOf(nfaState)); // test this hard
				
				if(nfaState.isAccept()) {
					curr.setAccept(true);
				}
				if(nfaState.getIsStart()) {
					curr.setIsStart(true);
				}
			}
			else {
				/*
				 * If you haven't seen that transition yet, add it to the DFA
				 */
				if(!transitionsSeen.contains(nfaState.getTransition())) {
					curr.addNext(DFAState.builder()
							.setAccept(false)
							.setTransition(nfaState.getTransition())
							.build());
					transitionsSeen.add(nfaState.getTransition());
					curr = curr.next(nfaState.getTransition());

					curr.addToIdList(nfaStates.indexOf(nfaState));
					
					if(nfaState.isAccept()) {
						curr.setAccept(true);
					}
					if(nfaState.getIsStart()) {
						curr.setIsStart(true);
					}
				}
				else {
					/*
					 * If you've seen a transition before, but it wasn't the last
					 * transition modified in your DFA
					 */
					for(DFAState state : this.getAllStates()) {
						if(state.getTransition() == nfaState.getTransition()) {
							curr = state;
							curr.addToIdList(nfaStates.indexOf(focus));
							
							if(nfaState.isAccept()) {
								curr.setAccept(true);
							}
							if(nfaState.getIsStart()) {
								curr.setIsStart(true);
							}
						}
					}
				}
			}
			for(NFAState nextState : focus.getNextStates()) {
				/*if(!curr.getIdList().contains(nfaStates.indexOf(focus))) {
					curr.addToIdList(nfaStates.indexOf(focus));
				}*/
				
				focus = nextState;
				
				/*for(NFAState state : visited) {
					System.out.print(visited.get(visited.indexOf(state)));
				}
				System.out.println();*/
				
				explore(focus);
			}
		}
		//focus = forkHack;
		return;
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
		
		//System.out.println(allStates.size());
		return allStates;
	}

}
