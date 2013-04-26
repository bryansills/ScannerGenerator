
public class Terminal {
	
	private String text = "";

	public Terminal(String text) {
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
