package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import org.junit.jupiter.api.Test;

class ComputerPlayerTest {

    @Test
    void alwaysPicksAnEmptyCell() {
        ComputerPlayer computer = new ComputerPlayer(new Random(1));
        for (int i = 0; i < 100; i++) {
            Board board = new Board();
            board.place(1, Board.X);
            board.place(5, Board.O);
            board.place(9, Board.X);
            int move = computer.chooseMove(board);
            assertTrue(board.isEmpty(move), "picked occupied cell " + move);
        }
    }

    @Test
    void picksTheOnlyRemainingCell() {
        // X O X
        // O O X
        // X _ O
        Board board = boardOf("XOXOOXX O");
        assertEquals(8, new ComputerPlayer(new Random()).chooseMove(board));
    }

    @Test
    void sameSeedGivesSameMoves() {
        Board board = new Board();
        int first = new ComputerPlayer(new Random(42)).chooseMove(board);
        int second = new ComputerPlayer(new Random(42)).chooseMove(board);
        assertEquals(first, second);
    }

    @Test
    void failsWhenNoMovesAreLeft() {
        Board board = boardOf("XOXXOOOXX");
        assertThrows(IllegalStateException.class, () -> new ComputerPlayer(new Random()).chooseMove(board));
    }

    private static Board boardOf(String layout) {
        Board board = new Board();
        for (int i = 0; i < 9; i++) {
            if (layout.charAt(i) != Board.EMPTY) {
                board.place(i + 1, layout.charAt(i));
            }
        }
        return board;
    }
}
