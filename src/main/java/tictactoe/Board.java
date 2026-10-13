package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** A 3x3 Tic-Tac-Toe board. Cells are numbered 1-9, left to right, top to bottom. */
public final class Board {

    public static final char X = 'X';
    public static final char O = 'O';
    public static final char EMPTY = ' ';

    /** Every winning line, as zero-based cell indexes. */
    private static final int[][] LINES = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
        {0, 4, 8}, {2, 4, 6},            // diagonals
    };

    private final char[] cells = new char[9];

    public Board() {
        Arrays.fill(cells, EMPTY);
    }

    public char get(int cell) {
        checkRange(cell);
        return cells[cell - 1];
    }

    public boolean isEmpty(int cell) {
        return get(cell) == EMPTY;
    }

    public List<Integer> emptyCells() {
        List<Integer> result = new ArrayList<>();
        for (int cell = 1; cell <= 9; cell++) {
            if (isEmpty(cell)) {
                result.add(cell);
            }
        }
        return result;
    }

    /**
     * Places a mark on an empty cell.
     *
     * @throws IllegalStateException if the game is already over or the cell is occupied
     */
    public void place(int cell, char mark) {
        checkRange(cell);
        if (mark != X && mark != O) {
            throw new IllegalArgumentException("Mark must be X or O, got '" + mark + "'");
        }
        if (isGameOver()) {
            throw new IllegalStateException("The game is over; no more moves are allowed");
        }
        if (!isEmpty(cell)) {
            throw new IllegalStateException("Cell " + cell + " is already taken");
        }
        cells[cell - 1] = mark;
    }

    /** Returns X or O if that player has three in a line, otherwise EMPTY. */
    public char winner() {
        for (int[] line : LINES) {
            char first = cells[line[0]];
            if (first != EMPTY && first == cells[line[1]] && first == cells[line[2]]) {
                return first;
            }
        }
        return EMPTY;
    }

    public boolean isFull() {
        return emptyCells().isEmpty();
    }

    /** A draw is a full board with no winner, so a win on the last move is still a win. */
    public boolean isDraw() {
        return winner() == EMPTY && isFull();
    }

    public boolean isGameOver() {
        return winner() != EMPTY || isFull();
    }

    private static void checkRange(int cell) {
        if (cell < 1 || cell > 9) {
            throw new IllegalArgumentException("Cell must be between 1 and 9, got " + cell);
        }
    }
}
