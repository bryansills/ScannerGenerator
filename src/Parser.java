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

				NonTerminal nt = parseLine(s);

        NonTerminal prev = null;
        for (NonTerminal nTerm : nonTerminals) {
          if (nTerm.getText().equals(nt.getText())) {
            prev = nTerm;
          }
        }

        if (prev == null) {
          nonTerminals.add(nt);
        } else {
          for (Rule rule : nt.getRules()) {
            prev.addRule(rule);
          }
        }

				tokenizeRules(nt);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


    createFirstSets(nonTerminals);
	}
	
	/**
	 * Parses the string and creates a nonterminal out of the contents
	 * 
	 * @param s Next line in grammar
	 * @return nt The nonterminal on line s
	 */
	public NonTerminal parseLine(String s) {
		NonTerminal nt = new NonTerminal();
		
		String[] ruleDivided = s.split("::=");
		nt.setText(ruleDivided[0].trim());
		for(String ruleStr : ruleDivided[1].split("\\|")) {
      String[] ruleToken = tokenizeRule(ruleStr.trim());
      nt.addRule(new Rule(ruleToken));
		}
		
		return nt;
	}

  /**
   * Parses the contents of a rule and tokenizes the contents
   * @param str the string to be tokenized
   * @return string array containing all the contents
   */
  public String[] tokenizeRule(String str) {
    String[] splitOnSpace = str.split(" ");
    List<String> tokenizedStrings = new ArrayList<String>();
    List<String> finalTokenizedStrings = new ArrayList<String>();

    // handle nonterminals
    for(int ii = 0; ii < splitOnSpace.length; ii++) {
      if (splitOnSpace[ii].indexOf('<') > 0) {
        String pre = splitOnSpace[ii].substring(0, splitOnSpace[ii].indexOf('<'));
        tokenizedStrings.add(pre);
        splitOnSpace[ii] = splitOnSpace[ii].substring(splitOnSpace[ii].indexOf('<'));
      }
      while (splitOnSpace[ii].contains("<")) {
        int endIndex = splitOnSpace[ii].indexOf('>');
        String tok = splitOnSpace[ii].substring(0, endIndex + 1);
        tokenizedStrings.add(tok);
        splitOnSpace[ii] = splitOnSpace[ii].substring(splitOnSpace[ii].indexOf('>') + 1);
      }
      if(splitOnSpace[ii].length() > 0) {
        tokenizedStrings.add(splitOnSpace[ii]);
      }
    }

    // handle tokens with parens
    for(String tok : tokenizedStrings) {
      // splits on open and close parens, but does not loose the parens
      String[] splitOnParen = tok.split("((?<=[()])|(?=[()]))");

      String temp = "";
      for (String parenTok : splitOnParen) {
        if (parenTok.length() > 0) {
          if (parenTok.matches("[^a-z()]*")) {
            if (temp.length() > 0) {
              finalTokenizedStrings.add(temp);
              temp = "";
            }
            finalTokenizedStrings.add(parenTok);
          } else {
            temp += parenTok;
          }
        }
      }

      if (temp.length() > 0) {
        finalTokenizedStrings.add(temp);
      }
    }

    return finalTokenizedStrings.toArray(new String[finalTokenizedStrings.size()]);
  }

  /**
	 * Adds all the tokens to a list
	 */
	public void tokenizeRules(NonTerminal nt) {
		for(Rule r : nt.getRules()) {
			for(Symbol sym : r.getRule()) {
				if(!sym.getText().contains("<") && (sym.getText().length() > 0)) {
					boolean contains = false;
					for (Token token : tokens) {
						if(token.getText().equals(sym.getText())) {
              contains = true;
            }
					}
					
					if(!contains) {
						tokens.add((Token) sym);
          }
				}
			}
		}
	}

  /**
   * creates the first sets for all the nonterminals
   * @param nonterminals
   */
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
