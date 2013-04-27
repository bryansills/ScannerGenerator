import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class Parser {

    private static List<NonTerminal> nonterminals;

    public static void main(String[] args) {

        nonterminals = new ArrayList<NonTerminal>();

        createFirstSets(nonterminals);
    }

    public static void createFirstSets(List<NonTerminal> nonterminals) {
        List<NonTerminal> nontermQueue = new ArrayList<NonTerminal>(nonterminals);

        while (!nontermQueue.isEmpty()) {
            NonTerminal nTerm = nontermQueue.get(0);

            for (List<Symbol> rule : nTerm.getContents()) {
                Symbol symbol = rule.remove(0);
                int idx = 0;
                boolean cont = true;

                while (cont && idx < rule.size()) {
                    NonTerminal next = null;
                    for (NonTerminal item : nonterminals) {
                        if (item.getText().equals(symbol.getText())) {
                            next = item;
                        }
                    }

                    boolean result;
                    if (next == null) {
                        if (symbol instanceof Identifier) {
                            result = nTerm.addToFirstSet((Identifier)symbol);
                            if (result) {
                                for (NonTerminal ter : nonterminals) {
                                    if (!nontermQueue.contains(ter)) {
                                        nontermQueue.add(ter);
                                    }
                                }
                            }
                        } else if (symbol instanceof Terminal) {
                            result = nTerm.addToFirstSet((Terminal)symbol);
                            if (result) {
                                for (NonTerminal ter : nonterminals) {
                                    if (!nontermQueue.contains(ter)) {
                                        nontermQueue.add(ter);
                                    }
                                }
                            }
                        } else {
                            try {
                                throw new Exception("YOU SHOULDN'T BE HERE.");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        HashSet<Symbol> nextFirstSet = next.getAllFirstSet();
                        Terminal epsilon = null;
                        for (Symbol symbol : nextFirstSet) {
                            if (symbol.getText() == "<epsilon>") {
                                epsilon = (Terminal) symbol;
                            }
                        }

                        if (epsilon != null) {
                            nextFirstSet.remove(epsilon);
                            cont = false;
                        }

                        result = nTerm.addAllToFirstSet();
                        if (result) {
                            for (NonTerminal ter : nonterminals) {
                                if (!nontermQueue.contains(ter)) {
                                    nontermQueue.add(ter);
                                }
                            }
                        }
                    }

                    idx++;
                }
                if (cont) {
                    nTerm.addToFirstSet(new Terminal("<epsilon>"));
                }
            }
        }
    }

    public static void createFollowSets(List<NonTerminal> nonterminals) {

    }
}
