import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class DFATable {
	
	List<DFAState> currState;
	List<Set<Character>> input;
	List<DFAState> nextState;
	List<Integer> isStartState;
	List<Integer> isAcceptState;
	
	public DFATable(DFA dfa) {
		currState = new ArrayList<DFAState>();
		input = new ArrayList<Set<Character>>();
		nextState = new ArrayList<DFAState>();
		isStartState = new ArrayList<Integer>();
		isAcceptState = new ArrayList<Integer>();
	}

}
