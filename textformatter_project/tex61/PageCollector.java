package tex61;

import java.util.List;

/**
 * A PageAssembler that collects its lines into a designated List.
 *
 * @author Allen Yu
 */
class PageCollector extends PageAssembler {

    /** The output that stores the lines. */
    private List<String> _lines;

    /** A new PageCollector that stores lines in OUT. */
    PageCollector(List<String> out) {
        _lines = out;
    }

    /** Add LINE to my List. */
    @Override
    void write(String line) {
        _lines.add(line);
    }

}
