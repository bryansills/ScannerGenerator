import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ParserTest {
    @Test
    public void basicFirstSet() throws Exception {

        NonTerminal aNonTerm = new NonTerminal("<stmt-sequence>");
        List<? extends Symbol> aRule = new ArrayList<? extends Symbol>();
        aRule.add(new NonTerminal("<stmt>"));
        aRule.add(new NonTerminal("<stmt-seq'>"));
        aNonTerm.addContents(aRule);

        NonTerminal bNonTerm = new NonTerminal("<stmt-seq'>");
        List<? extends Symbol> bRule = new ArrayList<? extends Symbol>();
        bRule.add(new Terminal(";"));
        bRule.add(new NonTerminal("<stmt-sequence>"));
        List<? extends Symbol> bRuleTwo = new ArrayList<? extends Symbol>();
        bRuleTwo.add(new Terminal("<epsilon>"));
        bNonTerm.addContents(bRule);

        NonTerminal cNonTerm = new NonTerminal("<stmt>");
        List<? extends Symbol> cRule = new ArrayList<? extends Symbol>();
        cRule.add(new Terminal("s"));
        cNonTerm.addContents(cRule);

        List<NonTerminal> grammar = new ArrayList<NonTerminal>();
        grammar.add(aNonTerm);
        grammar.add(bNonTerm);
        grammar.add(cNonTerm);

        Parser.createFirstSets(grammar);

        boolean test1 = false;
        boolean test2 = false;
        boolean test3 = false;
        for (Symbol sym : grammar.get(0).getFirstSet()) {
            if (sym.getText().equals("s")) {
                test1 = true;
            } else if (sym.getText().equals(";")) {
                test2 = true;
            } else if (sym.getText().equals("<epsilon>")) {
                test3 = true;
            } else {
                throw new Exception();
            }
        }
        assertTrue(test1);
        assertFalse(test2);
        assertFalse(test3);

        boolean test4 = false;
        boolean test5 = false;
        boolean test6 = false;
        for (Symbol sym : grammar.get(1).getFirstSet()) {
            if (sym.getText().equals("s")) {
                test4 = true;
            } else if (sym.getText().equals(";")) {
                test5 = true;
            } else if (sym.getText().equals("<epsilon>")) {
                test6 = true;
            } else {
                throw new Exception();
            }
        }
        assertTrue(test4);
        assertFalse(test5);
        assertFalse(test6);

        boolean test7 = false;
        boolean test8 = false;
        boolean test9= false;
        for (Symbol sym : grammar.get(2).getFirstSet()) {
            if (sym.getText().equals("s")) {
                test7 = true;
            } else if (sym.getText().equals(";")) {
                test8 = true;
            } else if (sym.getText().equals("<epsilon>")) {
                test9 = true;
            } else {
                throw new Exception();
            }
        }
        assertFalse(test7);
        assertTrue(test8);
        assertTrue(test9);
    }

    @Test
    public static void anotherFirstSet() {

        NonTerminal aNonTerm = new NonTerminal("<statement>");
        List<? extends Symbol> aRule = new ArrayList<? extends Symbol>();
        aRule.add(new NonTerminal("<if-stmt>"));
        List<? extends Symbol> aRuleTwo = new ArrayList<? extends Symbol>();
        aRuleTwo.add(new Terminal("other"));
        aNonTerm.addContents(aRule);

        NonTerminal bNonTerm = new NonTerminal("<if-stmt>");
        List<? extends Symbol> bRule = new ArrayList<? extends Symbol>();
        bRule.add(new Terminal("if"));
        bRule.add(new Terminal("("));
        bRule.add(new NonTerminal("<exp>"));
        bRule.add(new Terminal(")"));
        bRule.add(new NonTerminal("<statement>"));
        bRule.add(new NonTerminal("<else-part>"));
        bNonTerm.addContents(bRule);

        NonTerminal cNonTerm = new NonTerminal("<else-part>");
        List<? extends Symbol> cRule = new ArrayList<? extends Symbol>();
        cRule.add(new Terminal("else"));
        cRule.add(new NonTerminal("<statement>"));
        List<? extends Symbol> cRuleTwo = new ArrayList<? extends Symbol>();
        cRuleTwo.add(new Terminal("<epsilon>"));
        cNonTerm.addContents(cRule);

        NonTerminal dNonTerm = new NonTerminal("<exp>");
        List<? extends Symbol> dRule = new ArrayList<? extends Symbol>();
        dRule.add(new Terminal("0"));
        List<? extends Symbol> dRuleTwo = new ArrayList<? extends Symbol>();
        dRuleTwo.add(new Terminal("1"));
        dNonTerm.addContents(dRule);

        List<NonTerminal> grammar = new ArrayList<NonTerminal>();
        grammar.add(aNonTerm);
        grammar.add(bNonTerm);
        grammar.add(cNonTerm);
        grammar.add(dNonTerm);

        Parser.createFirstSets(grammar);

        boolean test1 = false;
        boolean test2 = false;
        boolean test3 = false;
        boolean test4 = false;
        boolean test5 = false;
        boolean test6 = false;
        for (Symbol sym : grammar.get(0).getFirstSet()) {
            if (sym.getText().equals("if")) {
                test1 = true;
            } else if (sym.getText().equals("other")) {
                test2 = true;
            } else if (sym.getText().equals("else")) {
                test3 = true;
            } else if (sym.getText().equals("0")) {
                test4 = true;
            } else if (sym.getText().equals("1")) {
                test5 = true;
            } else if (sym.getText().equals("<epsilon>")) {
                test6 = true;
            } else {
                throw new Exception();
            }
        }
        assertTrue(test1);
        assertTrue(test2);
        assertFalse(test3);
        assertFalse(test4);
        assertFalse(test5);
        assertFalse(test6);

        boolean test7 = false;
        boolean test8 = false;
        boolean test9 = false;
        boolean test10 = false;
        boolean test11 = false;
        boolean test12 = false;
        for (Symbol sym : grammar.get(1).getFirstSet()) {
            if (sym.getText().equals("if")) {
                test7 = true;
            } else if (sym.getText().equals("other")) {
                test8 = true;
            } else if (sym.getText().equals("else")) {
                test9 = true;
            } else if (sym.getText().equals("0")) {
                test10 = true;
            } else if (sym.getText().equals("1")) {
                test11 = true;
            } else if (sym.getText().equals("<epsilon>")) {
                test12 = true;
            } else {
                throw new Exception();
            }
        }
        assertTrue(test7);
        assertFalse(test8);
        assertFalse(test9);
        assertFalse(test10);
        assertFalse(test11);
        assertFalse(test12);

        boolean test13 = false;
        boolean test14 = false;
        boolean test15 = false;
        boolean test16 = false;
        boolean test17 = false;
        boolean test18 = false;
        for (Symbol sym : grammar.get(2).getFirstSet()) {
            if (sym.getText().equals("if")) {
                test13 = true;
            } else if (sym.getText().equals("other")) {
                test14 = true;
            } else if (sym.getText().equals("else")) {
                test15 = true;
            } else if (sym.getText().equals("0")) {
                test16 = true;
            } else if (sym.getText().equals("1")) {
                test17 = true;
            } else if (sym.getText().equals("<epsilon>")) {
                test18 = true;
            } else {
                throw new Exception();
            }
        }
        assertFalse(test13);
        assertFalse(test14);
        assertTrue(test15);
        assertTrue(test16);
        assertFalse(test17);
        assertFalse(test18);

        boolean test19 = false;
        boolean test20 = false;
        boolean test21 = false;
        boolean test22 = false;
        boolean test23 = false;
        boolean test24 = false;
        for (Symbol sym : grammar.get(3).getFirstSet()) {
            if (sym.getText().equals("if")) {
                test19 = true;
            } else if (sym.getText().equals("other")) {
                test20 = true;
            } else if (sym.getText().equals("else")) {
                test21 = true;
            } else if (sym.getText().equals("0")) {
                test22 = true;
            } else if (sym.getText().equals("1")) {
                test23 = true;
            } else if (sym.getText().equals("<epsilon>")) {
                test24 = true;
            } else {
                throw new Exception();
            }
        }
        assertFalse(test19);
        assertFalse(test20);
        assertFalse(test21);
        assertFalse(test22);
        assertTrue(test23);
        assertTrue(test24);

    }
}
