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

        createFirstSets(nonTerminals);
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
			Rule r = new Rule();
			nt.addRule(r);
			for(int i = 0; i < ruleStr.length(); i++) {
				if((ruleStr.charAt(i) == '<') && (ruleStr.length() > 0)) {
					String ntTemp = "";
					while(ruleStr.charAt(i) != '>') {
						ntTemp += ruleStr.charAt(i);
						i++;
					}
					ntTemp += '>';
					r.addToRule(ntTemp);
				}
			}
			String[] splitSpace = ruleStr.split(" ");
			for(String spaces : splitSpace) {
				if(!spaces.contains("<") && (spaces.length() > 0))
					r.addToRule(spaces);
			}
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


    public static void createFirstSets(Set<NonTerminal> nonterminals) {
        List<NonTerminal> nontermQueue = new ArrayList<NonTerminal>(nonterminals);

        while (!nontermQueue.isEmpty()) {
            NonTerminal nTerm = nontermQueue.remove(0);

            for (Rule rule : nTerm.getRules()) {
                List<Symbol> symbolList = rule.getRule();
                Symbol symbol;
                int idx = 0;
                boolean cont = true;
                boolean result;

                while (cont && idx < symbolList.size()) {
                    symbol = symbolList.get(idx);
                    NonTerminal next = null;
                    for (NonTerminal item : nonterminals) {
                        if (item.getText().equals(symbol.getText())) {
                            next = item;
                        }
                    }

                    if (next == null) {
                        if (symbol instanceof Identifier) {
                            cont = false;
                            result = nTerm.addToFirstSet((Identifier)symbol);
                            if (result) {
                                for (NonTerminal ter : nonterminals) {
                                    if (!nontermQueue.contains(ter)) {
                                        nontermQueue.add(ter);
                                    }
                                }
                            }
                        } else if (symbol instanceof Terminal) {
                            cont = false;
                            result = nTerm.addToFirstSet((Terminal)symbol);
                            if (result) {
                                for (NonTerminal ter : nonterminals) {
                                    if (!nontermQueue.contains(ter)) {
                                        nontermQueue.add(ter);
                                    }
                                }
                            }
                        } else {
                            try {
                                throw new Exception("YOU SHOULDN'T BE HERE.");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Set<Token> nextFirstSet = next.getFirstSet();
                        if (nextFirstSet.isEmpty()) {
                            cont = false;
                        } else {
                            Terminal epsilon = null;
                            for (Symbol firstToken : nextFirstSet) {
                                if (firstToken.getText().equals("<epsilon>")) {
                                    epsilon = (Terminal) firstToken;
                                }
                            }

                            if (epsilon != null) {
                                nextFirstSet.remove(epsilon);
                            } else {
                                cont = false;
                            }

                            result = nTerm.addAllToFirstSet(nextFirstSet);
                            if (result) {
                                for (NonTerminal ter : nonterminals) {
                                    if (!nontermQueue.contains(ter)) {
                                        nontermQueue.add(ter);
                                    }
                                }
                            }
                        }
                    }

                    idx++;
                }
                if (cont) {
                    result = nTerm.addToFirstSet(new Terminal("<epsilon>"));
                    if (result) {
                        for (NonTerminal ter : nonterminals) {
                            if (!nontermQueue.contains(ter)) {
                                nontermQueue.add(ter);
                            }
                        }
                    }
                }
            }
        }
    }
	
	public static void main(String[] args) {
		Parser p = new Parser();
	}
}
