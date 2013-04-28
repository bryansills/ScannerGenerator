import java.util.ArrayList;
import java.util.List;

/**
 * The tokenized right-hand side of a line, the rule is implemented as a list
 * of Symbols.
 *
 * @author dgreenhalgh
 */
public class Rule {

	List<Symbol> rule;

	public Rule() {
		rule = new ArrayList<Symbol>();
	}

	public Rule(String[] ruleStr) {
		rule = new ArrayList<Symbol>();
		for(String sym : ruleStr) {
			String trimSym = sym.trim();
			if(trimSym.length() > 0) {
        if (trimSym.equals("<epsilon>")) {
          rule.add(new Terminal("<epsilon>"));
        } else if (trimSym.matches("<.*>")) {
          rule.add(new NonTerminal(trimSym));
        } else if (trimSym.matches("[^A-Z]*")) {
          rule.add(new Terminal(trimSym));
        } else if (trimSym.matches("[^a-z]*")){
          rule.add(new Identifier(trimSym));
        }
      }
		}
	}

	public List<Symbol> getRule() {
		return rule;
	}

	public void addToRule(String s) {
		rule.add(new Symbol(s));
	}

	@Override
  public String toString() {
	  return rule.toString();
	}
	/**
	 * Compares rules based on the text of all of their symbols
	 */
	@Override
	public boolean equals(Object o) {
		boolean equals = true;

    /* Basic equals cases */
    if (o == null || !(o instanceof Rule)) {
      return false;
    }

		/* Special size cases */
		if((this.getRule().size() == 0) && (((Rule)o).getRule().size() == 0))
			return true;
		else if((this.getRule().size() == 0) != (((Rule)o).getRule().size() == 0))
			return false;

		for(int i = 0; i < this.getRule().size(); i++) {
			if(this.getRule().get(i).getText() != ((Rule)o).getRule().get(i).getText())
				return false;
		}

		return equals;
	}
}
