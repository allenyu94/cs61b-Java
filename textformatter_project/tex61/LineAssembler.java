package tex61;

import static tex61.FormatException.reportError;

import java.util.ArrayList;

/**
 * An object that receives a sequence of words of text and formats the words
 * into filled and justified text lines that are sent to a receiver.
 *
 * @author Allen Yu
 */
class LineAssembler {

    /** Destination given in constructor for formatted lines. */
    private final PageAssembler _pages;
    /** The SINGLE WORD that Line Assembler is currently forming. */
    private String _singleword;
    /** Words being accumulated WORDS. */
    private ArrayList<String> _words;

    /** Text Width setting _TEXTWIDTH. */
    private int _textwidth;
    /** Paragraph skip setting _PARSKIP. */
    private int _parskip;
    /** Paragraph Indentation _PARINDENT. */
    private int _parindent;
    /** Indentation setting _INDENT. */
    private int _indent;
    /** Mode for turning on/off Fill _FILL. */
    private boolean _fill;
    /** Mode for turning on/off Justify _JUSTIFY. */
    private boolean _justify;
    /** Determines if it is currently the first line. */
    private boolean firstline;
    /** The current number of chars in this line. */
    private int chars;
    /** The top of the first page. */
    private boolean toppage;
    /** Saved justify setting. */
    private boolean save;

    /**
     * A new, empty line assembler with default settings of all parameters,
     * sending finished lines to PAGES.
     */
    LineAssembler(PageAssembler pages) {
        _pages = pages;
        _fill = true;
        _justify = true;
        save = _justify;
        _singleword = null;
        firstline = true;
        _words = new ArrayList<String>();
        toppage = true;
    }

    /** Add TEXT to the word currently being built. */
    void addText(String text) {
        if (_singleword != null) {
            _singleword += text;
        } else {
            _singleword = text;
        }
    }

    /** Finish the current word, if any, and add to words being accumulated. */
    void finishWord() {
        if (_singleword != null) {
            addWord(_singleword);
        }
        _singleword = null;
    }

    /** Add WORD to the formatted text. */
    void addWord(String word) {
        if (_fill) {
            if (_textwidth < (indentation() + chars
                    + _words.size() + _singleword.length())) {
                outputLine(false);
            }
        }
        _words.add(word);
        chars += _singleword.length();
    }

    /**
     * Add LINE to our output, with no preceding paragraph skip. There must not
     * be an unfinished line pending.
     */
    void addLine(String line) {
        if (_words == null) {
            reportError("cannot have empty words in endnotes");
            System.exit(1);
        }
        _pages.addLine(line);
    }

    /** Set the current indentation to VAL. VAL >= 0. */
    void setIndentation(int val) {
        if (val < 0) {
            reportError("indentation cannot be negative value");
            System.exit(1);
        }
        _indent = val;
    }

    /** Set the current paragraph indentation to VAL. VAL >= 0. */
    void setParIndentation(int val) {
        _parindent = val;
    }

    /** Set the text width to VAL, where VAL >= 0. */
    void setTextWidth(int val) {
        if (val < 0 || val < _parindent + _indent) {
            reportError("textwidth cannot"
                    + " be negative or less than indentation");
            System.exit(1);
        }
        _textwidth = val;
    }

    /** Iff ON, set fill mode. */
    void setFill(boolean on) {
        _fill = on;
        if (!on) {
            save = _justify;
            setJustify(false);
        } else {
            setJustify(save);
        }
    }

    /**
     * Iff ON, set justify mode (which is active only when filling is also on).
     */
    void setJustify(boolean on) {
        _justify = on;
    }

    /** Set paragraph skip to VAL. VAL >= 0. */
    void setParSkip(int val) {
        if (val < 0) {
            reportError("paragraph skip cannot be negative value");
            System.exit(1);
        }
        _parskip = val;
    }

    /** Set page height to VAL > 0. */
    void setTextHeight(int val) {
        if (val <= 0) {
            reportError("textheight cannot be less than zero");
            System.exit(1);
        }
        _pages.setTextHeight(val);
    }

    /**
     * Process the end of the current input line. No effect if current line
     * accumulator is empty or in fill mode. Otherwise, adds a new complete line
     * to the finished line queue and clears the line accumulator.
     */
    void newLine() {
        if (!_fill) {
            outputLine(false);
        }
    }

    /**
     * If there is a current unfinished paragraph pending, close it out and
     * start a new one.
     */
    void endParagraph() {
        finishWord();
        outputLine(true);
    }

    /**
     * If the line accumulator is non-empty, justify its current contents, if
     * needed, add a new complete line to _pages, and clear the line
     * accumulator. LASTLINE indicates the last line of a paragraph.
     */
    void outputLine(boolean lastLine) {
        if (_words.isEmpty()) {
            return;
        }
        if (!toppage) {
            parskipping(firstline);
        }

        String finalline;
        finalline = strspaces(indentation());

        finalline = finalline.concat(_words.get(0));
        int blanks = numofblanks(lastLine);
        int prev = 0;

        double frac = (double) blanks / (_words.size() - 1);
        int curr;
        for (int i = 1; i < _words.size(); i++) {
            curr = (int) (0.5 + frac * i);
            int now = curr - prev;
            finalline = finalline.concat(strspaces(Math.min(now, 3)));
            finalline = finalline.concat(_words.get(i));
            prev = curr;
        }

        _pages.addLine(finalline);
        toppage = false;
        firstline = lastLine;
        _words.clear();
        chars = 0;
    }

    /**
     * Returns the total number of spaces for this given line.
     * It returns at most 3 spaces except when it is the LASTLINE. */
    private int numofblanks(boolean lastline) {
        int blanks;
        if (_fill && _justify && !lastline) {
            blanks = _textwidth - chars - indentation();
        } else {
            blanks = _words.size() - 1;
        }
        return blanks;
    }

    /** Returns a string of spaces given by the NUMOFSPACES. */
    String strspaces(int numofspaces) {
        String spaces = "";
        while (numofspaces > 0) {
            spaces = spaces.concat(" ");
            numofspaces -= 1;
        }
        return spaces;
    }

    /** Returns the number of indentation for this current line. */
    private int indentation() {
        if (firstline) {
            return _parindent + _indent;
        } else {
            return _indent;
        }
    }

    /** Returns the paragraph skips if it is FIRST. */
    private void parskipping(boolean first) {
        if (first) {
            for (int i = 0; i < _parskip; i++) {
                _pages.addLine(null);
            }
        }
    }

}
