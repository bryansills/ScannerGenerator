import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonTerminal extends Symbol {
	
	private List<Rule> rules = new ArrayList<Rule>();
	
	private boolean isStart;
	
	Set<NonTerminal> firstSet = new HashSet<NonTerminal>();
	Set<NonTerminal> followSet = new HashSet<NonTerminal>();
	
	public NonTerminal() {}
	
	public NonTerminal(String text) {
		super(text);
	}
	
	public NonTerminal(String text, boolean isStart) {
		super(text);
		this.isStart = isStart;
	}
	
	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public String toString() {
		return super.toString();
	}
	
	public void addRule(Rule rule) {
		if(!rules.contains(rule))
			rules.add(rule);
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
}
