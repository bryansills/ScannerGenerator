import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Parser {
	
	Set<NonTerminal> nonTerminals = new HashSet<NonTerminal>();
	List<Token> tokens = new ArrayList<Token>();
	
	public Parser() {
		File inFile = new File("grammar.txt");
		
		try {
			Scanner in = new Scanner(inFile);
			
			while(in.hasNextLine()) {
				String s = in.nextLine();
				
				//TODO: set isStart
				
				/* Pull out nonterminals on left-hand side */
				NonTerminal nt = tokenizeLeftHandSide(s);
				
				/* Tokenize terminals and IDs on right-hand side, 
				 * disregarding nonterminals */
				tokenizeRules(nt);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(NonTerminal nont : nonTerminals)
			System.out.println(nont);
		
		for(Token toke : tokens)
			System.out.println(toke);
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
		for(String ruleStr : ruleDivided[1].split("\\|")) {
			nt.addRule(new Rule(ruleStr.split(" ")));
		}
		
		nonTerminals.add(nt);
		
		return nt;
	}
	
	public void tokenizeRules(NonTerminal nt) {
		for(Rule r : nt.getRules()) {
			for(Symbol sym : r.getRule()) {
				if(!sym.getText().contains("<") && (sym.getText().length() > 0)) {
					int i = 0;
					boolean contains = false;
					
					while(i < tokens.size()) {
						if(tokens.get(i).getText().equals(sym.getText()))
							contains = true;
						
						i++;
					}
					
					if(!contains)
						tokens.add(new Token(sym.getText()));
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
