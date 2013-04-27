/**
 * Superclass encompassing nonterminals, terminals, and identifiers.
 * 
 * @author dgreenhalgh
 */
public class Token extends Symbol{
	
	public Token() {}
	
	public Token(String text) {
		super(text);
	}
	
	public String toString() {
		return super.toString();
	}
}
