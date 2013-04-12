import java.util.ArrayList;
import java.util.List;

public class TableWalker {

    public static List<String> walk(NFA nfa, String input) {
      List<String> classifList = new ArrayList<String>();
      // start by splitting by space
      String[] toks = input.split(" ");
      for (String t : toks) {
        String classification = nfa.accepts(t);
        if (nfa.accepts(t) != null) {
           classifList.add(classification.substring(1) + " " + t);
        }
        else {
          for (int i = t.length(); i >= 1; i--) {
            String sub = t.substring(0, i);
            String subClassif = nfa.accepts(sub);
            if (subClassif != null) {
              classifList.add(subClassif.substring(1) + " " + sub);
              t = t.substring(i);
              i = t.length() + 1;
            }
          }
        }
      }

      return classifList;
    }
}
