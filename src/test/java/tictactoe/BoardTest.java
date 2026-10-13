package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BoardTest {

    /** Places marks given as a 9-character string such as "XO X  O X", with ' ' for empty. */
    private static Board boardOf(String layout) {
        Board board = new Board();
        for (int i = 0; i < 9; i++) {
            char mark = layout.charAt(i);
            if (mark != Board.EMPTY) {
                board.place(i + 1, mark);
            }
        }
        return board;
    }

    @ParameterizedTest(name = "line {0}-{1}-{2}")
    @CsvSource({
        "1,2,3", "4,5,6", "7,8,9", // rows
        "1,4,7", "2,5,8", "3,6,9", // columns
        "1,5,9", "3,5,7",          // diagonals
    })
    void detectsEveryWinningLineForBothPlayers(int a, int b, int c) {
        for (char mark : new char[] {Board.X, Board.O}) {
            Board board = new Board();
            board.place(a, mark);
            board.place(b, mark);
            assertEquals(Board.EMPTY, board.winner());
            board.place(c, mark);
            assertEquals(mark, board.winner());
            assertTrue(board.isGameOver());
            assertFalse(board.isDraw());
        }
    }

    @Test
    void emptyBoardHasNoWinnerAndAllCellsFree() {
        Board board = new Board();
        assertEquals(Board.EMPTY, board.winner());
        assertFalse(board.isGameOver());
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), board.emptyCells());
    }

    @Test
    void winOnTheNinthMoveIsAWinNotADraw() {
        // X O X
        // O X O
        // O X _   X takes the last cell, completing the 1-5-9 diagonal.
        Board board = boardOf("XOXOXOOX ");
        assertEquals(Board.EMPTY, board.winner());
        board.place(9, Board.X);
        assertTrue(board.isFull());
        assertEquals(Board.X, board.winner());
        assertFalse(board.isDraw());
        assertTrue(board.isGameOver());
    }

    @Test
    void fullBoardWithNoWinnerIsADraw() {
        // X O X
        // X O O
        // O X X
        Board board = boardOf("XOXXOOOXX");
        assertEquals(Board.EMPTY, board.winner());
        assertTrue(board.isFull());
        assertTrue(board.isDraw());
        assertTrue(board.isGameOver());
    }

    @Test
    void boardWithEmptyCellsIsNotADraw() {
        Board board = boardOf("XOXXOOOX ");
        assertFalse(board.isDraw());
        assertFalse(board.isGameOver());
    }

    @Test
    void rejectsMoveOnOccupiedCell() {
        Board board = new Board();
        board.place(5, Board.X);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> board.place(5, Board.O));
        assertEquals("Cell 5 is already taken", e.getMessage());
        assertEquals(Board.X, board.get(5));
    }

    @Test
    void rejectsMovesAfterTheGameIsWon() {
        Board board = boardOf("OO  XX   ");
        board.place(4, Board.X);
        assertEquals(Board.X, board.winner());
        assertThrows(IllegalStateException.class, () -> board.place(3, Board.O));
        assertTrue(board.isEmpty(3));
    }

    @Test
    void rejectsOutOfRangeCellsAndBadMarks() {
        Board board = new Board();
        assertThrows(IllegalArgumentException.class, () -> board.place(0, Board.X));
        assertThrows(IllegalArgumentException.class, () -> board.place(10, Board.X));
        assertThrows(IllegalArgumentException.class, () -> board.place(1, Board.EMPTY));
    }
}
