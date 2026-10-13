package tictactoe;

/** Turns a board into text. Rows are 11 characters wide, including any trailing spaces. */
public final class BoardRenderer {

    private static final String DIVIDER = "-----------";

    private BoardRenderer() {
    }

    /** Renders the board's marks. Lines are separated by '\n' with no trailing newline. */
    public static String render(Board board) {
        char[] symbols = new char[9];
        for (int cell = 1; cell <= 9; cell++) {
            symbols[cell - 1] = board.get(cell);
        }
        return renderGrid(symbols);
    }

    /** Renders the cell numbers 1-9 in the same grid shape as the board. */
    public static String renderKey() {
        return renderGrid("123456789".toCharArray());
    }

    private static String renderGrid(char[] symbols) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 3; row++) {
            if (row > 0) {
                sb.append('\n').append(DIVIDER).append('\n');
            }
            int i = row * 3;
            sb.append(' ').append(symbols[i])
              .append(" | ").append(symbols[i + 1])
              .append(" | ").append(symbols[i + 2]).append(' ');
        }
        return sb.toString();
    }
}
