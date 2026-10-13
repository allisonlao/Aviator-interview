package tictactoe;

import java.util.List;
import java.util.Random;

/** Picks a uniformly random empty cell. */
public final class ComputerPlayer {

    private final Random random;

    public ComputerPlayer(Random random) {
        this.random = random;
    }

    public int chooseMove(Board board) {
        List<Integer> empty = board.emptyCells();
        if (empty.isEmpty()) {
            throw new IllegalStateException("No legal moves left");
        }
        return empty.get(random.nextInt(empty.size()));
    }
}
