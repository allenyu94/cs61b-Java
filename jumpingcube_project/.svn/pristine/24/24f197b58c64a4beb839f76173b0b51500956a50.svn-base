package jump61;

import static jump61.GameException.error;

/** A Player that gets its moves from manual input.
 *  @author Allen Yu
 */
class HumanPlayer extends Player {

    /** A new player initially playing COLOR taking manual input of
     *  moves from GAME's input source. */
    HumanPlayer(Game game, Color color) {
        super(game, color);
    }

    @Override
    void makeMove() {
        Game game = getGame();
        Board board = getBoard();
        while (true) {
            if (game.getMove(_move)) {
                if (board.isLegal(getColor(), _move[0], _move[1])) {
                    game.makeMove(_move[0], _move[1]);
                    break;
                } else {
                    throw error("invalid move %d, %d", _move[0], _move[1]);
                }
            } else {
                break;
            }
        }
    }

    /** the move that is taken from game. */
    private int[] _move = new int[2];
}
