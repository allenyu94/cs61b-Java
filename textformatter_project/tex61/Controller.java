package tex61;

import java.io.PrintWriter;
import java.util.ArrayList;
import static tex61.FormatException.reportError;


/**
 * Receives (partial) words and commands, performs commands, and accumulates and
 * formats words into lines of text, which are sent to a designated
 * PageAssembler. At any given time, a Controller has a current word, which may
 * be added to by addText, a current list of words that are being accumulated
 * into a line of text, and a list of lines of endnotes.
 *
 * @author Allen Yu
 */
class Controller {

    /** The output destination for the Controller. */
    private PrintWriter _out;
    /** a line constructor _LINE. */
    private LineAssembler _line;
    /** True iff we are currently processing an endnote. */
    private boolean _endnoteMode;
    /** Number of next endnote. */
    private int _refNum;
    /** The line assembler for the normal text. */
    private LineAssembler _mainText;
    /** The line assembler for the endnote text. */
    private LineAssembler _endText;
    /** The List of collected endnotes. */
    private ArrayList<String> _endnotes;

    /** A new Controller that sends formatted output to OUT. */
    Controller(PrintWriter out) {
        _out = out;
        _refNum = 1;
        _endnotes = new ArrayList<>();
        _mainText = new LineAssembler(new PagePrinter(_out));
        _endText = new LineAssembler(new PageCollector(_endnotes));

        setEndnoteMode();
        setTextWidth(Defaults.ENDNOTE_TEXT_WIDTH);
        setParSkip(Defaults.ENDNOTE_PARAGRAPH_SKIP);
        setIndentation(Defaults.ENDNOTE_INDENTATION);
        setParIndentation(Defaults.ENDNOTE_PARAGRAPH_INDENTATION);

        setNormalMode();
        setTextWidth(Defaults.TEXT_WIDTH);
        setTextHeight(Defaults.TEXT_HEIGHT);
        setParSkip(Defaults.PARAGRAPH_SKIP);
        setIndentation(Defaults.INDENTATION);
        setParIndentation(Defaults.PARAGRAPH_INDENTATION);
    }

    /**
     * Add TEXT to the end of the word of formatted text currently being
     * accumulated.
     */
    void addText(String text) {
        _line.addText(text);
    }

    /**
     * Finish any current word of text and, if present, add to the list of words
     * for the next line. Has no effect if no unfinished word is being
     * accumulated.
     */
    void endWord() {
        _line.finishWord();
    }

    /**
     * Finish any current word of formatted text and process an end-of-line
     * according to the current formatting parameters.
     */
    void addNewline() {
        endWord();
        _line.newLine();
    }

    /**
     * Finish any current word of formatted text, format and output any current
     * line of text, and start a new paragraph.
     */
    void endParagraph() {
        _line.endParagraph();
    }

    /**
     * If valid, process TEXT into an endnote, first appending a reference to it
     * to the line currently being accumulated.
     */
    void formatEndnote(String text) {
        if (_endnoteMode) {
            reportError("Endnotes cannot have endnotes.");
            System.exit(1);
        }
        String finalendnote = "[" + _refNum + "]";
        addText(finalendnote);
        InputParser check = new InputParser(text, this);
        setEndnoteMode();
        addText(finalendnote + " ");
        check.process();
        _refNum += 1;
    }

    /**
     * Set the current text height (number of lines per page) to VAL, if it is a
     * valid setting. Ignored when accumulating an endnote.
     */
    void setTextHeight(int val) {
        if (!_endnoteMode) {
            _line.setTextHeight(val);
        }
    }

    /**
     * Set the current text width (width of lines including indentation) to VAL,
     * if it is a valid setting.
     */
    void setTextWidth(int val) {
        _line.setTextWidth(val);
    }

    /**
     * Set the current text indentation (number of spaces inserted before each
     * line of formatted text) to VAL, if it is a valid setting.
     */
    void setIndentation(int val) {
        _line.setIndentation(val);
    }

    /**
     * Set the current paragraph indentation (number of spaces inserted before
     * first line of a paragraph in addition to indentation) to VAL, if it is a
     * valid setting.
     */
    void setParIndentation(int val) {
        _line.setParIndentation(val);
    }

    /**
     * Set the current paragraph skip (number of blank lines inserted before a
     * new paragraph, if it is not the first on a page) to VAL, if it is a valid
     * setting.
     */
    void setParSkip(int val) {
        _line.setParSkip(val);
    }

    /** Iff ON, begin filling lines of formatted text. */
    void setFill(boolean on) {
        _line.setFill(on);
    }

    /**
     * Iff ON, begin justifying lines of formatted text whenever filling is also
     * on.
     */
    void setJustify(boolean on) {
        _line.setJustify(on);
    }

    /**
     * Finish the current formatted document or endnote (depending on mode).
     * Formats and outputs all pending text.
     */
    void close() {
        _line.endParagraph();
        if (_endnoteMode) {
            setNormalMode();
        } else {
            writeEndnotes();
        }
    }

    /** Start directing all formatted text to the endnote assembler. */
    private void setEndnoteMode() {
        _line = _endText;
        _endnoteMode = true;
    }

    /** Return to directing all formatted text to _mainText. */
    private void setNormalMode() {
        _line = _mainText;
        _endnoteMode = false;
    }

    /** Write all accumulated endnotes to _mainText. */
    private void writeEndnotes() {
        for (String endnotes : _endnotes) {
            _mainText.addLine(endnotes);
        }
    }
}
