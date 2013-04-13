import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;


public class ScannerGeneratorTest {

  @Test
  public void getStartOfGroupingNaive() {
    assertEquals(0, ScannerGenerator.getStartOfGrouping("()", 1));
    assertEquals(0, ScannerGenerator.getStartOfGrouping("(abc)", 1));
    assertEquals(0, ScannerGenerator.getStartOfGrouping("(()())", 1));
  }

  @Test
  public void validBraceCheckWorks() {
    assert(ScannerGenerator.wellFormedBraces("()"));
    assertFalse(ScannerGenerator.wellFormedBraces("(())("));
  }

}
