import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
        nfas.put(temp[0], recursiveDescent(temp[1], nfas));
      }

      System.out.println(nfas);



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
  private static NFA recursiveDescent(String line, Map<String, NFA> nfas) {
    String[] parts = line.split(" ", 2);
    // handle concat
    if (parts.length > 1) {
      //return NFAOperations.concat(recursiveDescent(parts[0], nfas), recursiveDescent(parts[1], nfas));
    }

    return null;


  }
}
