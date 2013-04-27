import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Parser {
	
	Set<Terminal> terminals = new HashSet<Terminal>();
	Set<NonTerminal> nonTerminals = new HashSet<NonTerminal>();
	Set<Identifier> identifiers = new HashSet<Identifier>();
	
	List<String> terminalsAndIds = new ArrayList<String>();
	
	public Parser() {
		File inFile = new File("grammar.txt");
		
		try {
			Scanner in = new Scanner(inFile);
			
			while(in.hasNextLine()) {
				String s = in.nextLine();
				
				/* Pull out nonterminals on left-hand side */
				NonTerminal nt = tokenizeLeftHandSide(s);
				
				/* Tokenize terminals and IDs on right-hand side, 
				 * disregarding nonterminals */
				tokenizeRightHandSide(nt);
				
				while(!terminalsAndIds.isEmpty()) {
					String sId = terminalsAndIds.get(0);
					if(sId.substring(0,1).matches("[A-Z]")) {
						if(identifiers.size() > 0) {
							List<Identifier> idList = new ArrayList<Identifier>(identifiers);
							int i = 0;
							boolean contains = false;
							while(i < idList.size()) {
								if(idList.get(i).getText().equals(sId))
									contains = true;
								
								i++;
							}
							
							if(!contains)
								identifiers.add(new Identifier(sId));
							
							terminalsAndIds.remove(0);
						}
						else {
							identifiers.add(new Identifier(sId));
							terminalsAndIds.remove(0);
						}
					}
					else {
						List<Terminal> tList = new ArrayList<Terminal>(terminals);
						int i = 0;
						boolean contains = false;
						while(i < tList.size()) {
							if(tList.get(i).getText().equals(sId))
								contains = true;
								
							i++;
						}
						
						if(!contains)
							terminals.add(new Terminal(sId));
						
						terminalsAndIds.remove(0);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(NonTerminal nont : nonTerminals)
			System.out.println(nont);
	}
	
	/**
	 * Tokenize nonterminals on left-hand side
	 * 
	 * @param s Next line in grammar
	 * @return nt The nonterminal on line s
	 */
	public NonTerminal tokenizeLeftHandSide(String s) {
		NonTerminal nt = new NonTerminal();
		
		String[] ruleDivided = s.split("::=");
		nt.setText(ruleDivided[0].trim());
		nt.setContents(ruleDivided[1].split("\\|"));
		
		nonTerminals.add(nt);
		
		return nt;
	}
	
	public void tokenizeRightHandSide(NonTerminal nt) {
		for(String content : nt.getContents()) {
			String[] contentsTrimmed = content.split(" ");
			for(String termOrId : contentsTrimmed) {
				if(!termOrId.contains("<") && (termOrId.length() > 0)) {
					terminalsAndIds.add(termOrId);
				}
			}
		}
	}
	
	/**
	 * Iterates through nonTerminals and returns the start nonterminal.
	 * Returns null if no start nonterminal is found.
	 */
	public NonTerminal getStartNonTerminal() {
		for(NonTerminal nt : nonTerminals) {
			if(nt.isStart())
				return nt;
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		Parser p = new Parser();
	}
}
