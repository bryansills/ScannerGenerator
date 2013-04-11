import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DFAState {
	private List<DFAState> nextStates;
	private Set<Character> transition;
	private boolean accept;
	private List<Integer> idList;
	
	public DFAState() {
		this(true, new HashSet<Character>());
	}
	
	public DFAState(int startId) {
		this.idList.add(startId);
	}
	
	public DFAState(boolean aAccept, Set<Character> aTransition) {
		accept = aAccept;
		transition = aTransition;
	}
	
	public DFAState next(Character c) {
		DFAState nextState = new DFAState();
		
		for(DFAState d : nextStates) {
			if(d.acceptsChar(c)) {
				nextState = d;
			}
		}
		
		return nextState;
	}
	
	public DFAState next(Set<Character> charSet) {
		DFAState nextState = new DFAState();
		
		for(DFAState d : nextStates) {
			if(d.acceptsCharSet(charSet)) {
				nextState = d;
			}
		}
		
		return nextState;
	}
	
	public Set<Character> getTransition() {
		return transition;
	}
	
	public DFAState setTransition(Set<Character> transition) {
		this.transition = transition;
		
		return this;
	}
	
	public boolean acceptsChar(Character c) {
		return transition == null && c == null
			|| transition != null && transition.contains(c);
	}
	
	public boolean acceptsCharSet(Set<Character> charSet) {
		return transition == null && charSet == null
			|| transition != null && transition.contains(charSet);	// test
	}
	
	public void setAccept(boolean isAccept) {
		accept = isAccept;
	}
	
	public void addNext(DFAState state) {
		nextStates.add(state);
	}
	
	public List<DFAState> getNextStates() {
		return nextStates;
	}
	
	public List<Integer> getIdList() {
		return idList;
	}
	
	public void addToIdList(int newId) {
		idList.add(newId);
	}
}
