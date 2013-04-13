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

        if (isAcceptState.get(ind)) {
            return true;
        } else {
            return false;
        }
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
