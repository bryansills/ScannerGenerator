import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class manages the group of character classes.
 * Basically a glorified map. Also, it's a singleton.
 * @author magicjarvis
 */
public class CharClasses {
  private Map<String, Set<Character>> classes;

  /**
   * Creates a CharClasses object based on
   * $DIGIT   [0-9]
   * $NON-ZERO    [^0]  IN  $DIGIT
   * $CHAR   [a-zA-Z]
   * $UPPER  [^a-z] IN $CHAR
   * Doesn't handle the regex spec if included. (so don't include it)
   * @param spec
   * @throws BadSpecException
   */
  public CharClasses(String spec) throws BadSpecException {
    classes = new HashMap<String, Set<Character>>();
    buildMapFromSpec(spec);
  }

  /**
   * This method tells you whether a defined character class
   * accepts a specified character c.
   * @param charClass The class in question.
   * @param c the character you're testing.
   * @return whether it is accepted.
   */
  public boolean accept(String charClass, Character c) {
    return classes.get(charClass).contains(c);
  }
  /**
   * Returns the set of accepted characters for a class.
   * Used exclusively for testing.
   * @param charClass The class
   * @return the associated characters
   */
  Set<Character> get(String charClass) {
    return classes.get(charClass);
  }

  private void buildMapFromSpec(String spec) throws BadSpecException {
    // split into lines
    String[] lines = spec.split("\n");
    for (String line : lines) {
      // break up the line into parts.
      String[] tokens = line.split(" ");
      Set<Character> chars = new HashSet<Character>();
      Set<Character> excludeSet = new HashSet<Character>();
      Set<Character> escaped = new HashSet<Character>(
          Arrays.asList(new Character[]{'-', '^', '\\', '[',']'}));
      classes.put(tokens[0], chars);
      char[] range = tokens[1].toCharArray();
      if (range[0] != '[' || range[range.length - 1] != ']') {
        throw new BadSpecException("Malformed braces. Do your classes have [] surrounding them?");
      }
      boolean exclude = false;
      char last = range[0];
      int start = 1;
      if (range.length > 2 && range[1] == '^') {
        exclude = true;
        start = 2;
      }
      for (int i = start; i < range.length - 1; i++) {
        Set<Character> set = exclude ? excludeSet : chars; // which set we add to.
        if (range[i] == '-' && last != '\\') {
          if (range[i+1] <= last) {
            throw new BadSpecException("Invalid Range");
          }
          if (escaped.contains(range[i+1])) {
            throw new BadSpecException("Range missing second argument");
          }
          for (char j = last; j <= range[i+1]; j++) {
            // this condition fails if [a-]. doesn't fail but doesn't handle.
            set.add(j);
          }
          // skip the last char in the range
          last = range[i+1];
          i++;
          continue;
        } else if (((last == '\\' && escaped.contains(range[i]))
            || !escaped.contains(range[i]))) {
          set.add(range[i]);
        }
        last = range[i];
      }

      if (exclude) {
        if (!tokens[2].equalsIgnoreCase("IN")) {
          throw new BadSpecException("Expected IN operator. Got: " + tokens[2]);
        }
        Set<Character> in = classes.get(tokens[3]);
        for (Character c : in) {
          if (!excludeSet.contains(c)) {
            chars.add(c);
          }
        }
      }
    }
    System.out.println(classes);
  }
}
