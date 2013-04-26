import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class Parser {

    private static List<Nonterminal> nonterminals;

    public static void main(String[] args) {

        nonterminals = new ArrayList<Nonterminal>();

        createFirstSets(nonterminals);
    }

    public static void createFirstSets(List<Nonterminal> nonterminals) {
        List<Nonterminal> nontermQueue = new ArrayList<Nonterminal>(nonterminals);

        while (!nontermQueue.isEmpty()) {
            Nonterminal nTerm = nontermQueue.get(0);

            for (String rule : nTerm.getRules()) {
                String[] tokens = rule.split(" ");
                int idx = 0;
                boolean cont = true;

                while (cont && idx < tokens.length) {
//                    if (token.charAt(0) == '<') {
//                        if (token.contains(">")) {
//
//                        }
//                    }
                    Nonterminal next = null;
                    for (Nonterminal item : nonterminals) {
                        if (item.getText().equals(token)) {
                            next = item;
                        }
                    }

                    boolean result;
                    if (next == null) {
                        result = nTerm.addToFirstSet(new Nonterminal(token));
                        if (result) {
                            nontermQueue.addAll(nonterminals);
                        }
                    } else {
                        HashSet<Nonterminal> nextFirstSet = next.getAllFirstSet();
                        Nonterminal epsilon = nextFirstSet.remove("<epsilon>");

                        if (epsilon != null) {
                            cont = false;
                        }

                        result = nTerm.addAllToFirstSet();
                    }

                    idx++;
                }
                if (cont) {
                    nTerm.addToFirstSet("<epsilon>");
                }
            }
        }
    }
}
