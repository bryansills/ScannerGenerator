import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonTerminal {
	
	private String text = "";
	private List<String> contents = new ArrayList<String>();
	
	Set<NonTerminal> firstSet = new HashSet<NonTerminal>();
	Set<NonTerminal> followSet = new HashSet<NonTerminal>();
	
	public NonTerminal() {}
	
	public NonTerminal(String text) {
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
	
	public List<String> getContents() {
		return contents;
	}

	public void setContents(List<String> contents) {
		this.contents = contents;
	}
	
	public void setContents(String[] contentList) {
		contents = new ArrayList();
		
		for(String content : contentList) {
			contents.add(content);
		}
	}
	
	public void removeFromContents(String content) {
		contents.remove(content); //test
	}
	
	public void addToText(char c) {
		text += String.valueOf(c);
	}
}
