import java.util.ArrayList;
import java.util.List;


public class Rule {
	
	List<Symbol> rule;

	public Rule(String[] ruleStr) {
		rule = new ArrayList<Symbol>();
		for(String sym : ruleStr)
			rule.add(new Symbol(sym.trim()));
	}
	
	public List<Symbol> getRule() {
		return rule;
	}
}
