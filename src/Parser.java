import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Reads a grammar and pulls out a list of nonterminals. The nonterminals keep 
 * hold a list of rules that they are associated with.
 * 
 * @author dgreenhalgh
 */
public class Parser {
	
	Set<NonTerminal> nonTerminals = new HashSet<NonTerminal>();
	List<Token> tokens = new ArrayList<Token>();
	
	public Parser() {
		File inFile = new File("grammar.txt");
		
		try {
			Scanner in = new Scanner(inFile);
			
			while(in.hasNextLine()) {
				String s = in.nextLine();

				NonTerminal nt = tokenizeLeftHandSide(s);
				tokenizeRules(nt);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
		
		int i = 0;
		boolean contains = false;
		List<NonTerminal> ntList = new ArrayList<NonTerminal>(nonTerminals);
		
		if(nonTerminals.size() > 0) {
			while(i < ntList.size() && !contains) {
				if(ntList.get(i).getText().equals(nt.getText())) {
					contains = true;
					for(Rule r : nt.getRules())
						ntList.get(i).addRule(r);
				}
				
				i++;				
			}
			
			if(!contains)
				nonTerminals.add(nt);
		}
		else {
			nonTerminals.add(nt);
		}
		
		return nt;
	}
	
	/**
	 * Tokenizes a nonterminal's rules and adds those tokens to a list
	 */
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
	
	public static void main(String[] args) {
		Parser p = new Parser();
	}
}
