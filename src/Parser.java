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
				
				/* Pull out nonterminals on left-hand side */
				NonTerminal nt = new NonTerminal();
				
				String[] ruleDivided = s.split("::=");
				nt.setText(ruleDivided[0].trim());
				nt.setContents(ruleDivided[1].split("\\|"));
				
				nonTerminals.add(nt);
				
				/* Tokenize terminals and IDs on right-hand side, 
				 * disregarding nonterminals */
				for(String content : nt.getContents()) {
					String[] contentsTrimmed = content.split(" ");
					for(String termOrId : contentsTrimmed) {
						if(!termOrId.contains("<") && (termOrId.length() > 0)) {
							terminalsAndIds.add(termOrId);
						}
					}
				}
				
				/* Separate terminals and IDs */
				while(terminalsAndIds.iterator().hasNext()) {
					String sId = terminalsAndIds.iterator().next();
					if(sId.substring(0,1).matches("[A-Z]")) {
						if(identifiers.size() > 0) {
							for(Identifier id : identifiers) {
								if(!id.getText().equals(sId)) {
									identifiers.add(new Identifier(sId));
									terminalsAndIds.remove(0);
								}
							}
						}
						else {
							identifiers.add(new Identifier(sId));
							terminalsAndIds.remove(0);
						}
					}
					else {
						terminals.add(new Terminal(sId));
						terminalsAndIds.remove(0);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		for(Identifier id : identifiers) {
			System.out.println(id.getText());
		}
	}
	
	public static void main(String[] args) {
		Parser p = new Parser();
	}
}
