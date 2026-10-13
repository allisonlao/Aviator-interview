package tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

/** Runs games of human (X) vs. computer (O) until the player quits or declines a rematch. */
public final class Game {

    private final BufferedReader in;
    private final PrintStream out;
    private final ComputerPlayer computer;

    public Game(BufferedReader in, PrintStream out, ComputerPlayer computer) {
        this.in = in;
        this.out = out;
        this.computer = computer;
    }

    public void run() {
        while (playOneGame() && askPlayAgain()) {
            out.println();
        }
        out.println("Goodbye.");
    }

    /** Plays a single game. Returns false if the player quit or input ended mid-game. */
    private boolean playOneGame() {
        Board board = new Board();
        out.println("Cells are numbered like this:");
        out.println(BoardRenderer.renderKey());

        while (true) {
            out.println();
            out.println(BoardRenderer.render(board));
            Integer move = readHumanMove(board);
            if (move == null) {
                return false;
            }
            board.place(move, Board.X);
            if (board.isGameOver()) {
                break;
            }

            int computerMove = computer.chooseMove(board);
            board.place(computerMove, Board.O);
            out.println("Computer plays " + computerMove);
            if (board.isGameOver()) {
                break;
            }
        }

        out.println();
        out.println(BoardRenderer.render(board));
        out.println(resultMessage(board));
        return true;
    }

    /** Prompts until the player enters a legal move. Returns null if they quit or input ended. */
    private Integer readHumanMove(Board board) {
        while (true) {
            out.print("Your move (1-9, h for help, q to quit): ");
            String line = readLine();
            if (line == null) {
                out.println();
                return null;
            }
            InputParser.Result result = InputParser.parse(line);
            switch (result.type()) {
                case QUIT:
                    return null;
                case HELP:
                    out.println(BoardRenderer.renderKey());
                    break;
                case INVALID:
                    out.println(result.error());
                    break;
                case MOVE:
                    if (board.isEmpty(result.cell())) {
                        return result.cell();
                    }
                    out.println("Cell " + result.cell() + " is already taken. Choose an empty cell.");
                    break;
            }
        }
    }

    private boolean askPlayAgain() {
        while (true) {
            out.print("Play again? (y/n) ");
            String line = readLine();
            if (line == null) {
                out.println();
                return false;
            }
            String answer = line.strip().toLowerCase(Locale.ROOT);
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            out.println("Please answer y or n.");
        }
    }

    private static String resultMessage(Board board) {
        char winner = board.winner();
        if (winner == Board.X) {
            return "You win";
        }
        if (winner == Board.O) {
            return "Computer wins";
        }
        return "Draw";
    }

    /** Returns the next line, or null at end of input. Read errors are treated as end of input. */
    private String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            return null;
        }
    }
}
