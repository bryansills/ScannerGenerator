import java.util.List;
import java.util.Set;

public class DFAState {
	private List<DFAState> nextStates;
	private Set<Character> transition;
	private boolean accept;
	
	public DFAState(Set<Character> aTransition, boolean aAccept) {
		transition = aTransition;
		accept = aAccept;
	}
	
	public DFAState next(Character c) {
		DFAState nextState;
		
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
	
	

}
