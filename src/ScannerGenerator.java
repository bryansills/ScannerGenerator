import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class ScannerGenerator {

  public static void main(String[] args) {
    if (args.length < 1) {
      System.out.println("Usage: java ScannerGenerator <spec-file>");
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

      Map<String, Set<Character>> classes = CharClasses.buildMapFromSpec(classSpec);

      Map<String, NFA> nfas = new HashMap<String, NFA>();
      for (Entry<String, Set<Character>> entry : classes.entrySet()) {
        nfas.put(entry.getKey(), new NFA(entry.getValue()));
      }

      for (String regex : spec) {
        String[] temp = regex.split(" ", 2);
        // dirty i know. This gets the identifier out of the line
        // and sends the regex to the rd method
        nfas.put(temp[0], rd(temp[1], nfas));
      }



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

  // ($DIGIT|$LOWER)+ $THING* a* b
  // (($DIGIT|$LOWER) ($ASSDF))+
  private static NFA rd(String line, Map<String, NFA> nfas) {
    String[] parts = line.split(" ", 2);
    // handle concat
    if (wellFormedBraces(parts[0]) && parts.length > 1) {
      return NFAOperations.concat(rd(parts[0], nfas), rd(parts[1], nfas));
    }


    // we can assume nothing beyond this point has a space in it.
    // TODO(magicjarvis): Make this prettier. I demand it.
    // check for escaped stuff when this works
    int star = line.indexOf('*');
    if (star != -1 && line.charAt(star - 1) == ')') {
      int start = getStartOfGrouping(line, star - 1);
      if (start == 0) {
        return NFAOperations.star(rd(line.substring(start + 1, star - 1), nfas));
      }
    }

    int plus = line.indexOf('+');
    if (plus != -1 && line.charAt(plus - 1) == ')') {
      int start = getStartOfGrouping(line, plus - 1);
      if (start == 0) {
        return NFAOperations.plus(rd(line.substring(start + 1, plus - 1), nfas));
      }
    }

    String[] unionParts = line.split("\\|");
    if (wellFormedBraces(unionParts[0]) && unionParts.length > 1) {
      return NFAOperations.union(rd(unionParts[0], nfas), rd(unionParts[1], nfas));
    }

    int lastParen = line.lastIndexOf(')');
    if (lastParen != -1) {
      int start = getStartOfGrouping(line, lastParen);
      if (start == 0) {
        String grouping = line.substring(start + 1, lastParen);
        if (lastParen == line.length() - 1) {
          return rd(grouping, nfas);
        } else {
          return NFAOperations.concat(rd(grouping, nfas), rd(line.substring(lastParen + 1), nfas));
        }
      }
    }

    if (unionParts.length > 1) {
      return NFAOperations.union(rd(unionParts[0], nfas), rd(unionParts[1], nfas));
    }

    // handle plus
    // handle grouping and |
    // handle char class
    char first = line.charAt(0);
    if (first != '$') {
      if (first == '.') {
        Set<Character> ascii = new HashSet<Character>();
        for (char c = 0; c < 255; c++) {
          ascii.add(c);
        }
        if (line.length() == 1) {
          return new NFA(ascii);
        } else {
          return NFAOperations.concat(new NFA(ascii), rd(line.substring(1), nfas));
        }
      }

      Set<Character> singletonSet = singletonSet(first);

      if (first == '\\' && line.length() > 1) {
        singletonSet = singletonSet(line.charAt(1));
      }

      NFA oneChar = new NFA(singletonSet);
      if (line.length() == 1 || (line.length() == 2 && first == '\\')) {
        return oneChar;
      }
      return NFAOperations.concat(oneChar, rd(line.substring(1), nfas));
    } else if (nfas.containsKey(line)) {
      return nfas.get(line);
    }

    return null;


  }

  public static Set<Character> singletonSet(Character c) {
    return new HashSet<Character>(Arrays.asList(new Character[]{c}));
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
