/**
 * Superclass encompassing nonterminals, terminals, and identifiers.
 * 
 * @author dgreenhalgh
 */
public class Token {
	
	private String text = "";
	
	public Token() {}
	
	public Token(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
