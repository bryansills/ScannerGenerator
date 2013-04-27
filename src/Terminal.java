/**
 * We assume that any token which is not an identifier is a terminal.
 * 
 * @author dgreenhalgh
 */
public class Terminal extends Token {
	
	public Terminal(String text) {
		super(text);
	}
	
	public String toString() {
		return super.toString();
	}

}
