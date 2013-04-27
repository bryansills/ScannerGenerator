import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

public class ScannerGenerator {

  public static void main(String[] args) {
    if (args.length < 2) {
      System.out.println("Usage: java ScannerGenerator <spec-file> <input-file>");
      System.exit(1);
    }

    try {
      BufferedReader in = new BufferedReader(new FileReader(new File(args[0])));
      List<String> classSpec = new ArrayList<String>();
      List<String> spec = new ArrayList<String>();
      String line;
      while ((line = in.readLine()) != null && !line.equals("")) {
        classSpec.add(line);
      }

      while ((line = in.readLine()) != null) {
        spec.add(line);
      }

      in.close();

      Map<String, Set<Character>> classes = CharClasses
          .buildMapFromSpec(classSpec);


      Map<String, NFA> nfas = new HashMap<String, NFA>();
      for (Entry<String, Set<Character>> entry : classes.entrySet()) {
        nfas.put(entry.getKey(), new NFA(entry.getValue()));
      }
      Map<String, NFA> regMap = new HashMap<String, NFA>();

      for (String regex : spec) {
        String[] temp = regex.split(" ", 2);
        regMap.put(temp[0], rd(temp[1], nfas));
      }

      // later
      for (Entry<String, NFA> nfa : regMap.entrySet()) {
        nfa.getValue().getEnd().setName(nfa.getKey());
      }

      for (NFA nfa : regMap.values()) {
        System.out.println(nfa.getEnd().getName() + " " + nfa.getEnd().isAccept());
        System.out.println("End state: " + nfa.getEnd().getId());
        System.out.println(nfa);
      }

      // read in input file
      in = new BufferedReader(new FileReader(new File(args[1])));
      List<String> inputs = new ArrayList<String>();
      while ((line = in.readLine()) != null) {
        inputs.add(line);
      }
      in.close();

      PrintWriter out = new PrintWriter(new FileWriter(new File(
          "OutputTokens.txt")));

      for (String input : inputs) {
        List<String> toks = breakLine(input, new char[]{'"', '[', '\''}, new char[]{'"', ']', '\''});
        for (String t : toks) {
          walk(t, regMap, out);
        }
      }

      /*for (Entry<String, NFA> nfa : regMap.entrySet()) {
        System.out.println(nfa.getKey() + " = " + nfa.getValue() + "\n\n");
      }*/
      //System.out.println(regMap.get("$IDENTIFIER"));
      MyDFA.removeEpsilon(regMap.get("$IDENTIFIER").getStartState());

      out.close();
    } catch (FileNotFoundException e) {
      System.err.println("Can't Find that file");
      e.printStackTrace();
    } catch (IOException e) {
      System.err.println("Error Reading from the file");
      e.printStackTrace();
    } catch (BadSpecException e) {
      System.err.println("Spec is messed up");
      e.printStackTrace();
    }
  }

  private static List<String> breakLine(String s, char[] open, char[] close) {
    List<String> tokens = new ArrayList<String>();
    Map<Character, Character> braces = new HashMap<Character, Character>();
    for (int i = 0; i < open.length; i++) {
      braces.put(open[i], close[i]);
    }
    Stack<Character> stack = new Stack<Character>();
    String tok = "";
    char[] chars = s.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (braces.containsValue(c) && !stack.empty()) {
        if (braces.get(stack.peek()).equals(c)) {
          stack.pop();
        }
      } else if (braces.containsKey(c)) {
        stack.push(c);
      }

      if (c == ' ' || i == chars.length - 1) {
        if (stack.empty()) {
          if (c != ' ') {
            tok += c;
          }
          tokens.add(tok);
          tok = "";
        } else {
          tok += c;
        }
      } else {
        tok += c;
      }
    }



    return tokens;
  }
  private static void walk(String s, Map<String, NFA> map, PrintWriter out) {
    while (s.length() > 0) {
      String acceptedNfa = "";
      int lastAccept = -1;
      String sub = "";
      for (int i = 1; i <= s.length(); i++) {
        sub = s.substring(0, i);
        for (Entry<String, NFA> nfa : map.entrySet()) {
          if (nfa.getValue().accepts(sub)) {
            lastAccept = i;
            acceptedNfa = nfa.getKey().substring(1);
          }
        }
      }
      System.out.println(acceptedNfa + " " + s.substring(0, lastAccept));
      s = s.substring(lastAccept);
    }
  }

  // ($DIGIT|$LOWER)+ $THING* a* b
  // (($DIGIT|$LOWER) ($ASSDF))+
  private static NFA rd(String line, Map<String, NFA> nfas) {
    if (line.length() == 0) {
      return new NFA(null); // empty string transition
    }
    //System.out.println(line);
    String[] parts = line.split(" ", 2);
    // handle concat
    if (wellFormedBraces(parts[0]) && parts.length > 1) {
      //System.out.printf("Concat %s %s\n", parts[0], parts[1]);
      return NFAOperations.concat(rd(parts[0], nfas), rd(parts[1], nfas));
    }

    // we can assume nothing beyond this point has a space in it.
    // TODO(magicjarvis): Make this prettier. I demand it.
    // check for escaped stuff when this works
    int star = line.indexOf('*');
    if (star != -1 && line.indexOf("\\*") + 1 != star
        && line.charAt(star - 1) == ')') {
      int start = getStartOfGrouping(line, star - 1);
      if (start == 0) {
        if (star < line.length() - 1) {
          //System.out.printf("Star %s\n", line.substring(start + 1, star - 1));
          return NFAOperations
              .concat(NFAOperations.star(rd(
                  line.substring(start + 1, star - 1), nfas)),
                  rd(line.substring(star + 1), nfas));
        } else {
          //System.out.printf("Star %s\n", line.substring(start + 1, star - 1));
          return NFAOperations.star(rd(line.substring(start + 1, star - 1),
              nfas));

        }
      }
    }

    int plus = line.indexOf('+');
    if (plus != -1 && line.indexOf("\\+") + 1 != plus
        && line.charAt(plus - 1) == ')') {
      int start = getStartOfGrouping(line, plus - 1);
      if (start == 0) {
        if (plus < line.length() - 1) {
          //System.out.printf("Plus %s\n", line.substring(start + 1, plus - 1));
          return NFAOperations
              .concat(NFAOperations.star(rd(
                  line.substring(start + 1, plus - 1), nfas)),
                  rd(line.substring(plus + 1), nfas));
        } else {
          return NFAOperations.plus(rd(line.substring(start + 1, plus - 1),
              nfas));
        }
      }
    }

    String[] unionParts = line.split("\\|", 2);
    if (wellFormedBraces(unionParts[0]) && unionParts.length > 1) {
      //System.out.printf("Union %s %s\n", unionParts[0], unionParts[1]);
      return NFAOperations.union(rd(unionParts[0], nfas),
          rd(unionParts[1], nfas));
    }

    // check if one level deep ()s
    // if (line.charAt(index))
    int lastParen = line.lastIndexOf(')');
    if (lastParen != -1 && line.lastIndexOf("\\(") + 1 != lastParen) {
      int start = getStartOfGrouping(line, lastParen);
      if (start == 0) {
        String grouping = line.substring(start + 1, lastParen);
        if (lastParen == line.length() - 1) {
          return rd(grouping, nfas);
        } else {
          //System.out.printf("Concat %s %s\n", grouping, line.substring(lastParen + 1));
          return NFAOperations.concat(rd(grouping, nfas),
              rd(line.substring(lastParen + 1), nfas));
        }
      }
    }

    if (wellFormedBraces(unionParts[0]) && unionParts.length > 1) {
      //System.out.printf("Union %s %s\n", unionParts[0], line.substring(lastParen + 1));
      return NFAOperations.union(rd(unionParts[0], nfas),
          rd(unionParts[1], nfas));
    }

    // handle plus
    // handle grouping and |
    // handle char class
    char first = line.charAt(0);
    if (first == '(') {
      int open = 1;
      char last = first;
      for (int i = 1; i < line.length(); i++) {
        if (line.charAt(i) == ')' && last != '\\') {
          open--;
        }
        if (open == 0) {
          //System.out.printf("Concat %s %s\n", line.substring(1, i), line.substring(i + 1));
          return NFAOperations.concat(rd(line.substring(1, i), nfas),
              rd(line.substring(i + 1), nfas));
        }
      }
    }
    if (first != '$') {
      if (first == '.') {
        Set<Character> ascii = new HashSet<Character>();
        for (char c = 0; c < 255; c++) {
          ascii.add(c);
        }
        if (line.length() == 1) {
          return new NFA(ascii);
        } else {
          return NFAOperations.concat(new NFA(ascii),
              rd(line.substring(1), nfas));
        }
      }

      Set<Character> singletonSet = singletonSet(first);

      if (first == '\\' && line.length() > 1) {
        //System.out.println("Found an escaped char");
        singletonSet = singletonSet(line.charAt(1));
        //System.out.println("Created this singleton set " + singletonSet);
      }

      NFA oneChar = new NFA(singletonSet);
      if (line.length() == 1 || (line.length() == 2 && first == '\\')) {
        //System.out.println("NFA " + line);
        return oneChar;
      }
      char temp = (first == '\\') ? line.charAt(1) : first;
      int sub = (first == '\\') ? 2 : 1;
      //System.out.printf("Concat %s %s\n", ""+temp, line.substring(sub));
      return NFAOperations.concat(oneChar, rd(line.substring(sub), nfas));
    } else if (nfas.containsKey(line)) {
      //System.out.println("We have this " + line);
      return nfas.get(line);
    }

    return null;

  }

  public static Set<Character> singletonSet(Character c) {
    return new HashSet<Character>(Arrays.asList(new Character[] { c }));
  }

  public static boolean wellFormedBraces(String line) {
    int open = 0;
    char[] chars = line.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == '(') {
        open++;
      } else if (chars[i] == ')') {
        open--;
      }
      if (open < 0) {
        return false;
      }
    }
    return open == 0;
  }

  public static int getStartOfGrouping(String line, int closing) {
    int open = 1;
    char[] chars = line.toCharArray();
    for (int i = closing - 1; i >= 0; i--) {
      if (chars[i] == ')') {
        open++;
      } else if (chars[i] == '(') {
        open--;
      }
      if (open == 0) {
        return i;
      }
    }
    return -1; // broken. should probably throw an exception
  }
}
