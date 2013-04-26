import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Parser {
	
	Set<Terminal> terminals = new HashSet<Terminal>();
	Set<NonTerminal> nonTerminals = new HashSet<NonTerminal>();
	Set<Identifier> identifiers = new HashSet<Identifier>();
	
	List<String> terminalsAndIds = new ArrayList<String>();
	
	public Parser() {
		File inFile = new File("grammar.txt");
		
		try {
			Scanner in = new Scanner(inFile);
			
			while(in.hasNextLine()) {
				String s = in.nextLine();
				char[] chars = s.toCharArray();
				NonTerminal nt = new NonTerminal();
				int i = 0;
				while(chars[i] != '>') {
					nt.addToText(chars[i++]);
				}
				nt.addToText('>');
				nonTerminals.add(nt);
				
				String secondHalf = String.valueOf(chars).split("::=")[1];
				nt.setContents(secondHalf.split("\\|"));
				
				for(String content : nt.getContents()) {
					String[] temp = content.split(" ");
					for(String termOrId : temp) {
						if(!termOrId.contains("<") && (termOrId.length() > 0)) {
							terminalsAndIds.add(termOrId);
						}
					}
				}
				
				for(String element : terminalsAndIds) {
					if(element.matches(".*[A-Z]+.*")) {
						identifiers.add(new Identifier(element));
					}
					else {
						terminals.add(new Terminal(element));
					}
				}
				
				for(Identifier id : identifiers) {
					System.out.println(id.getText());
				}
				
				/*for(Terminal t : terminals) {
					System.out.println(t.getText());
				}*/
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		/*for(NonTerminal non : nonTerminals) {
			for(String s : non.getContents())
				System.out.println(s);
		}*/
		
		/*for(String element : terminalsAndIds) {
			System.out.println(element);
		}*/
	}
	
	public boolean nonTerminalsContains(NonTerminal nt) {
		for(NonTerminal n : nonTerminals) {
			if(n.getText().equals(nt.getText()))
				return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		Parser p = new Parser();
	}
}
