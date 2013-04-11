import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DFAState {
	private List<DFAState> nextStates;
	private Set<Character> transition;
	private boolean accept;
	
	public DFAState() {
		this(true, new HashSet<Character>());
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
	
	public void setAccept(boolean isAccept) {
		accept = isAccept;
	}
	
	public void addNext(DFAState state) {
		nextStates.add(state);
	}
	
	public List<DFAState> getNextStates() {
		return nextStates;
	}
}
