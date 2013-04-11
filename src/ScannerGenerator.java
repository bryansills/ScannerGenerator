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

  public NFA recursiveDescent(String line, Map<String, NFA> nfas) {
    return null;
  }
}
