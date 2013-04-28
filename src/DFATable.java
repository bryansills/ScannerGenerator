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
	List<DFAState> visited;
	
	
	public DFATable(DFA aDfa) {
		dfa = aDfa;
		currState = new ArrayList<DFAState>();
		input = new ArrayList<Set<Character>>();
		nextState = new ArrayList<DFAState>();
		isStartState = new ArrayList<Boolean>();
		isAcceptState = new ArrayList<Boolean>();
		visited = new ArrayList<DFAState>();
		
		populateDfaTable(this);
	}
	
	public DFATable(List<DFAState> currState,
	List<Set<Character>> input,
	List<DFAState> nextState,
	List<Boolean> isStartState,
	List<Boolean> isAcceptState){
		this.currState = currState;
		this.input = input;
		this.nextState = nextState;
		this.isStartState = isStartState;
		this.isAcceptState = isAcceptState;
	}
	
	public List getCurrState(){
		return currState;
	}
	public List getInput(){
		return input;
	}
	public List getNextState(){
		return nextState;
	}
	public List getStartState(){
		return isStartState;
	}
	public List getAcceptState(){
		return isAcceptState;
	}
	
	public void populateDfaTable(DFATable dfaTable) {
		DFAState curr = dfa.getStartState();
		visited.add(dfa.getStartState());
		
		for(DFAState next : curr.getNextStates()) {
			currState.add(curr);
			input.add(next.getTransition());
			nextState.add(next);
			isStartState.add(curr.getIsStart());
			isAcceptState.add(curr.getAccept());
			
			explore(next);
		}
	}
	
	public void explore(DFAState state) {
		visited.add(state);
		
		if(state.getNextStates().isEmpty()) {
			currState.add(state);
			input.add(null);
			nextState.add(null);
			isStartState.add(state.getIsStart());
			isAcceptState.add(state.getAccept());
		}
		else {
			for(DFAState next : state.getNextStates()) {
				currState.add(state);
				input.add(next.getTransition());
				nextState.add(next);
				isStartState.add(state.getIsStart());
				isAcceptState.add(state.getAccept());
				
				explore(next);
			}
		}
		
		/*
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
		*/
	}

    public boolean accepts(String t) {
        List<Integer> indexes = indexesOf(isStartState, Boolean.TRUE);
        DFAState nState;
        Character currChar;
        int ind = -1;

        for (int ii = 0; ii < t.length(); ii++) {
            currChar = t.charAt(ii);

            for (Integer index : indexes) {
                if (input.get(index).contains(currChar)) {
                    ind = index;
                }
            }

            nState = nextState.get(ind);

            indexes = indexesOf(currState, nState);
        }

        return isAcceptState.get(ind);
    }

    private <T> List<Integer> indexesOf(List<T> source, T target)
    {
        final List<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < source.size(); i++) {
            if (source.get(i).equals(target)) { indexes.add(i); }
        }
        return indexes;
    }
}
