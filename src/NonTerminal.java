import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NonTerminal extends Token {
	
	private List<String> contents = new ArrayList<String>();
	
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
		return getText();
	}
	
	public List<String> getContents() {
		return contents;
	}

	public void setContents(List<String> contents) {
		this.contents = contents;
	}
	
	public void setContents(String[] contentList) {
		contents = new ArrayList<String>();
		
		for(String content : contentList) {
			contents.add(content);
		}
	}
	
	public void removeFromContents(String content) {
		contents.remove(content);
	}
}
