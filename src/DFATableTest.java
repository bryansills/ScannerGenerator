import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;


public class DFATableTest {
	
	public DFA buildExampleDFA(){
		DFA dfa = new DFA();
		Set<Character> chars = new HashSet<Character>(
				Arrays.asList(new Character[] { 'a' }));
		Set<Character> charsb = new HashSet<Character>(
				Arrays.asList(new Character[] { 'b' }));
		
		DFAState A = DFAState.builder()
				.setAccept(false)
				.setTransition(null)
				.build();
		
		DFAState B = DFAState.builder()
				.setAccept(true)
				.setTransition(chars)
				.build();
	
		DFAState C = DFAState.builder()
				.setAccept(true)
				.setTransition(charsb)
				.build();
		
		A.addToIdList(1);
		A.addToIdList(2);
		A.addToIdList(7);
		
		B.addToIdList(3);
		B.addToIdList(4);
		B.addToIdList(6);
		B.addToIdList(8);
		B.addToIdList(9);
		
		C.addToIdList(5);
		C.addToIdList(6);
		//C.addToIdList(8);
		
		A.addNext(B);
		B.addNext(C);
		
		dfa.setStartState(A);
		
		return dfa;
	}
	
	public DFATable buildTable(DFA dfa){
		List currState = new ArrayList<DFAState>();
		List input = new ArrayList<Set<Character>>();
		List nextState = new ArrayList<DFAState>();
		List isStartState = new ArrayList<Boolean>();
		List isAcceptState = new ArrayList<Boolean>();
		List visited = new ArrayList<DFAState>();
		
		currState.add(dfa.getStartState());
		currState.add(dfa.getStartState().next('a'));
		currState.add(dfa.getStartState().next('a').next('b'));
		
		input.add('a');
		input.add('b');
		input.add(null);
		
		nextState.add(dfa.getStartState().next('a'));
		nextState.add(dfa.getStartState().next('a').next('b'));
		nextState.add(null);
		
		isStartState.add(true);
		isStartState.add(false);
		isStartState.add(false);
		
		isAcceptState.add(false);
		isAcceptState.add(true);
		isAcceptState.add(true);
		
		DFATable table = new DFATable(currState,input,nextState,
				isStartState,isAcceptState);
		return table;

	}
	
	@Test
	public void tableEntriesShouldBeEqual(){
		DFA dfa = buildExampleDFA();
		DFATable myTable = buildTable(dfa);
		DFATable table = new DFATable(dfa);
		assertEquals(myTable.getCurrState().get(2),table.getCurrState().get(2));
		assertEquals(myTable.getInput().get(2),table.getInput().get(2));
		assertEquals(myTable.getNextState().get(2),table.getNextState().get(2));
		assertEquals(myTable.getStartState().get(2),table.getStartState().get(2));
		assertEquals(myTable.getAcceptState().get(2),table.getAcceptState().get(2));
		//assertEquals(myTable.getCurrState().size(),table.getCurrState().size());
	}

}
