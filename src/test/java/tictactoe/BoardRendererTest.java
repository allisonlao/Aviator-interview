package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class BoardRendererTest {

    @Test
    void rendersSpecExampleExactlyIncludingTrailingSpaces() {
        Board board = new Board();
        board.place(1, Board.X);
        board.place(2, Board.O);
        board.place(5, Board.X);
        board.place(7, Board.O);
        board.place(9, Board.X);

        String expected = " X | O |   \n"
                        + "-----------\n"
                        + "   | X |   \n"
                        + "-----------\n"
                        + " O |   | X ";
        assertEquals(expected, BoardRenderer.render(board));
    }

    @Test
    void rendersEmptyBoard() {
        String expected = "   |   |   \n"
                        + "-----------\n"
                        + "   |   |   \n"
                        + "-----------\n"
                        + "   |   |   ";
        assertEquals(expected, BoardRenderer.render(new Board()));
    }

    @Test
    void everyLineIsElevenCharactersWide() {
        for (String line : BoardRenderer.render(new Board()).split("\n", -1)) {
            assertEquals(11, line.length(), "line: '" + line + "'");
        }
    }

    @Test
    void rendersNumberKey() {
        String expected = " 1 | 2 | 3 \n"
                        + "-----------\n"
                        + " 4 | 5 | 6 \n"
                        + "-----------\n"
                        + " 7 | 8 | 9 ";
        assertEquals(expected, BoardRenderer.renderKey());
    }
}
