package tex61;

import static org.junit.Assert.*;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Test;

public class LineAssemblerTest {

    /** Collects output to a PrintWriter. */
    private StringWriter output;
    /** Collects output from a PageAssembler. */
    private PrintWriter writer;
    /** Target PageAssembler. */
    private PageAssembler pages;
    /** Target LineAssembler. */
    private LineAssembler line;

    private static final String NL = System.getProperty("line.separator");

    private void setupWriter() {
        output = new StringWriter();
        writer = new PrintWriter(output);
    }

    private void writeTestLines(String[] test) {
        for (String L : test) {
            pages.addLine(L);
        }
    }

    private String strcompare(String[] str) {
        StringBuilder S = new StringBuilder();
        for (String curr: str) {
            S.append(curr);
            S.append(NL);
        }
        return S.toString();
    }

    private void run(String str) {
        Controller control = new Controller(writer);
        InputParser scan = new InputParser(str, control);
        scan.process();
    }

    @Test
    public void test1() {
        String str = "\\parindent{0}"
                + "This is a general test for the overall process";
        String answer = "This is a general test for the overall process";
        String[] thisline = new String[1];
        thisline[0] = answer;
        setupWriter();
        run(str);
        assertEquals("test1 failed", output.toString(), strcompare(thisline));
    }

    @Test
    public void test2() {
        String str = "\\parindent{0}"
                + "\\textwidth{5}Testing textwidth";
        String answer1 = "Testing";
        String answer2 = "textwidth";
        String[] thisline = new String[2];
        thisline[0] = answer1;
        thisline[1] = answer2;
        setupWriter();
        run(str);
        assertEquals("test1 failed", output.toString(), strcompare(thisline));
    }

    @Test
    public void test3() {
        String str = "\\parindent{0}"
                + "\\endnote{This is an endnote test}";
        String answer1 = "[1]";
        String answer2 = "[1] This is an endnote test";
        String[] thisline = new String[2];
        thisline[0] = answer1;
        thisline[1] = answer2;
        setupWriter();
        run(str);
        assertEquals("test1 failed", output.toString(), strcompare(thisline));
    }

}
