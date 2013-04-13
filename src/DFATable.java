import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class DFATable {
	DFA dfa;
	List<DFAState> currState;
	List<Set<Character>> input;
	List<DFAState> nextState;
	List<Boolean> isStartState;
	List<Boolean> isAcceptState;
	
	
	public DFATable(DFA aDfa) {
		dfa = aDfa;
		currState = new ArrayList<DFAState>();
		input = new ArrayList<Set<Character>>();
		nextState = new ArrayList<DFAState>();
		isStartState = new ArrayList<Boolean>();
		isAcceptState = new ArrayList<Boolean>();
		
		populateDfaTable(this);
	}
	
	public void populateDfaTable(DFATable dfaTable) {
		explore(dfa.getStartState());
	}
	
	public void explore(DFAState currentState) {
		if(currentState.getNextStates() == null) {
			return;
		}
		
		while(currentState.getNextStates() != null) {
			for(DFAState dfaState : currentState.getNextStates()) {
				currState.add(currentState);
				nextState.add(dfaState);
				input.add(dfaState.getTransition());
				
				if(currentState.getAccept()) {
					isAcceptState.add(true);
				}
				else {
					isAcceptState.add(false);
				}
				
				if(currentState.getIsStart()) {
					isStartState.add(true);
				}
				else {
					isAcceptState.add(false);
				}
			}
			
			explore(currentState);
		}
	}

}
