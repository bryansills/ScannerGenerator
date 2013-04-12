import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class NFAtoDFATest {

	public NFA buildExampleNFA() {
		NFA nfa = new NFA();
		Set<Character> chars = new HashSet<Character>(
				Arrays.asList(new Character[] { 'a' }));
		Set<Character> charsb = new HashSet<Character>(
				Arrays.asList(new Character[] { 'b' }));

		NFAState one = NFAState.builder().setAccept(false).setTransition(null)
				.build();

		NFAState two = NFAState.builder().setAccept(false).setTransition(null)
				.build();

		NFAState three = NFAState.builder().setAccept(false)
				.setTransition(chars).build();

		NFAState four = NFAState.builder().setAccept(false).setTransition(null)
				.build();

		NFAState five = NFAState.builder().setAccept(false)
				.setTransition(charsb).build();

		NFAState six = NFAState.builder().setAccept(true).setTransition(null)
				.build();

		NFAState seven = NFAState.builder().setAccept(false)
				.setTransition(null).build();

		NFAState eight = NFAState.builder().setAccept(false)
				.setTransition(chars).build();

		one.addNext(two);
		one.addNext(seven);

		two.addNext(three);

		three.addNext(four);

		four.addNext(five);

		five.addNext(six);

		seven.addNext(eight);

		eight.addNext(six);

		nfa.setStartState(one);
		
		return nfa;
	}
	public DFA buildExampleDFA(){
		DFA dfa = new DFA();
		Set<Character> chars = new HashSet<Character>(
				Arrays.asList(new Character[] { 'a' }));
		Set<Character> charsb = new HashSet<Character>(
				Arrays.asList(new Character[] { 'b' }));
		DFAState A = new DFAState(false, null);
		DFAState B = new DFAState(true, chars);
		DFAState C = new DFAState(true, charsb);
		
		A.addToIdList(1);
		A.addToIdList(2);
		A.addToIdList(7);
		
		B.addToIdList(3);
		B.addToIdList(4);
		B.addToIdList(6);
		B.addToIdList(8);
		
		C.addToIdList(5);
		C.addToIdList(6);
		A.addNext(B);
		B.addNext(C);
		
		dfa.setStartState(A);
		
		return dfa;
	}
	
	@Test
	public void ConvertedNFAShouldEqualDFA(){
		DFA dfa1 = buildExampleDFA();
		NFA nfa = buildExampleNFA();
		DFA dfa2 = new DFA(nfa);
		
		DFAState curr1 = dfa1.getStartState();
		DFAState curr2 = dfa2.getStartState();
		System.out.println("first" + curr2.getIdList().size());
		System.out.println("stuff" + curr2.next('a').getIdList().size());
		
		int states1 = dfa1.getAllStates().size();
		int states2 = dfa2.getAllStates().size();
		
		//assertNotNull(dfa1.getStartState());
		//assertEquals(curr1,curr2);
		
		assertEquals(states1,states2);
		//assertEquals(curr1.getIdList().get(0), curr2.getIdList().get(0));
		//assertEquals(curr1.getIdList().size(),curr2.getIdList().size());
		//assertEquals(curr1.next('a').getIdList().size(), curr2.next('a').getIdList().size());
	}
}
