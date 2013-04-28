/**
 * Symbol includes tokens and nonterminals.
 * 
 * @author dgreenhalgh
 */
public class Symbol {
	
	private String text = "";
	
	public Symbol() {}
	
	public Symbol(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String toString() {
		return getText();
	}
}
