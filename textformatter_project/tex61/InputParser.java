package tex61;

import static tex61.FormatException.reportError;

import java.io.Reader;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Reads commands and text from an input source and send the results to a
 * designated Controller. This essentially breaks the input down into
 * "tokens"---commands and pieces of text.
 *
 * @author Allen Yu
 */
class InputParser {

    /**
     * Matches text between { } in a command, including the last }, but not the
     * opening {. When matched, group 1 is the matched text. Always matches at
     * least one character against a non-empty string or input source. If it
     * matches and group 1 is null, the argument was not well-formed (the final
     * } was missing or the argument list was nested too deeply).
     */
    private static final Pattern BALANCED_TEXT = Pattern
            .compile("(?s)((?:\\\\.|[^\\\\{}]"
                    + "|[{](?:\\\\.|[^\\\\{}])*[}])*)" + "\\}" + "|.");

    /**
     * Matches input to the text formatter. Always matches something in a
     * non-empty string or input source. After matching, one or more of the
     * groups described by *_TOKEN declarations will be non-null. See these
     * declarations for descriptions of what this pattern matches. To test
     * whether .group(*_TOKEN) is null quickly, check for .end(*_TOKEN) > -1).
     */
    private static final Pattern INPUT_PATTERN = Pattern
            .compile("(?s)(\\p{Blank}+)" + "|(\\r?\\n((?:\\r?\\n)+)?)"
                    + "|\\\\([\\p{Blank}{}\\\\])" + "|\\\\(\\p{Alpha}+)([{]?)"
                    + "|((?:[^\\p{Blank}\\r\\n\\\\{}]+))" + "|(.)");

    /** Symbolic names for the groups in INPUT_PATTERN. */
    private static final int
        /** Blank or tab. */
        BLANK_TOKEN = 1,
        /** End of line or paragraph. */
        EOL_TOKEN = 2,
        /**
         * End of paragraph (>1 newline). EOL_TOKEN group will also be present.
         */
        EOP_TOKEN = 3,
        /**
         * \{, \}, \\, or \ . .group(ESCAPED_CHAR_TOKEN)
         * will be the character after the backslash.
         */
        ESCAPED_CHAR_TOKEN = 4,
        /**
         * Command (\<alphabetic characters>). .group(COMMAND_TOKEN) will be the
         * characters after the backslash.
         */
        COMMAND_TOKEN = 5,
        /**
         * A '{' immediately following a command. When this group is present,
         * .group(COMMAND_TOKEN) will also be present.
         */
        COMMAND_ARG_TOKEN = 6,
        /**
         * Segment of other text (none of the above, not including any of the
         * special characters \, {, or }).
         */
        TEXT_TOKEN = 7,
        /** A character that should not be here. */
        ERROR_TOKEN = 8;

    /**
     * A new InputParser taking input from READER and sending tokens to OUT.
     */
    InputParser(Reader reader, Controller out) {
        _input = new Scanner(reader);
        _out = out;
    }

    /**
     * A new InputParser whose input is TEXT and that sends tokens to OUT.
     */
    InputParser(String text, Controller out) {
        _input = new Scanner(text);
        _out = out;
    }

    /**
     * Break all input source text into tokens, and send them to our output
     * controller. Finishes by calling .close on the controller.
     */
    void process() {
        while (_input.findWithinHorizon(INPUT_PATTERN, 0) != null) {
            MatchResult tokens = _input.match();
            if (tokens.end(BLANK_TOKEN) > -1) {
                _out.endWord();
            } else if (tokens.end(EOP_TOKEN) > -1) {
                _out.endParagraph();
            } else if (tokens.end(EOL_TOKEN) > -1) {
                _out.addNewline();
            } else if (tokens.end(ESCAPED_CHAR_TOKEN) > -1) {
                _out.addText(tokens.group(ESCAPED_CHAR_TOKEN));
            } else if (tokens.end(TEXT_TOKEN) > -1) {
                _out.addText(tokens.group(TEXT_TOKEN));
            } else if (tokens.end(COMMAND_TOKEN) > -1) {
                String command = tokens.group(COMMAND_TOKEN);
                String arg;
                if (!tokens.group(COMMAND_ARG_TOKEN).isEmpty()) {
                    if (_input.findWithinHorizon(BALANCED_TEXT, 0) == null) {
                        reportError("endnote missing args");
                        System.exit(1);
                    }
                    arg = _input.match().group(1);
                } else {
                    arg = null;
                }
                processCommand(command, arg);
            } else if (tokens.end(ERROR_TOKEN) > -1) {
                reportError("Error: unrecognized input.");
                System.exit(1);
            }
        }
        _out.close();
    }

    /** Returns value of the string after checking to see that the
     * argument passed in is an int VALUE. */
    private int argnum(String value) {
        int val;
        try {
            val = Integer.parseInt(value);
            return val;
        } catch (NumberFormatException e) {
            throw new FormatException("argument is not an integer value");
        }
    }

    /**
     * Process \COMMAND{ARG} or (if ARG is null) \COMMAND. Call the appropriate
     * methods in our Controller (_out).
     */
    private void processCommand(String command, String arg) {
        try {
            switch (command) {
            case "indent":
                _out.setIndentation(argnum(arg));
                break;
            case "parindent":
                _out.setParIndentation(argnum(arg));
                break;
            case "textwidth":
                _out.setTextWidth(argnum(arg));
                break;
            case "textheight":
                _out.setTextHeight(argnum(arg));
                break;
            case "parskip":
                _out.setParSkip(argnum(arg));
                break;
            case "nofill":
                if (arg != null) {
                    throw new FormatException("unexpected argument");
                }
                _out.setFill(false);
                break;
            case "fill":
                if (arg != null) {
                    throw new FormatException("unexpected argument");
                }
                _out.setFill(true);
                break;
            case "justify":
                if (arg != null) {
                    throw new FormatException("unexpected argument");
                }
                _out.setJustify(true);
                break;
            case "nojustify":
                if (arg != null) {
                    throw new FormatException("unexpected argument");
                }
                _out.setJustify(false);
                break;
            case "endnote":
                if (arg == null) {
                    throw new FormatException("argument not found");
                }
                _out.formatEndnote(arg);
                break;
            default:
                reportError("unknown command: %s", command);
                break;
            }
        } catch (FormatException e) {
            System.out.println("There was a formatting error");
        }
    }

    /** My input source. */
    private final Scanner _input;
    /** The Controller to which I send input tokens. */
    private Controller _out;

}
