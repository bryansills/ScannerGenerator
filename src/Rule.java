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

	public Rule(String[] ruleStr) {
		rule = new ArrayList<Symbol>();
		for(String sym : ruleStr) {
			String trimSym = sym.trim();
			if(trimSym.length() > 0)
				rule.add(new Symbol(trimSym));
		}
	}
	
	public List<Symbol> getRule() {
		return rule;
	}
	
	@Override
	public boolean equals(Object o) {
		boolean equals = true;
		
		/* Special size cases */
		if((this.getRule().size() == 0) && (((Rule)o).getRule().size() == 0))
			return true;
		else if((this.getRule().size() == 0) ^ (((Rule)o).getRule().size() == 0))
			return false;
		
		for(int i = 0; i < this.getRule().size(); i++) {
			if(this.getRule().get(i).getText() != ((Rule)o).getRule().get(i).getText())
				return false;
		}
		
		return equals;
	}
}
