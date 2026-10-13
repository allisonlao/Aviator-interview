package tictactoe;

import java.util.Locale;

/** Turns one line of player input into a command. Knows nothing about the board's contents. */
public final class InputParser {

    public enum Type { MOVE, QUIT, HELP, INVALID }

    /** The parsed input. {@code cell} is set only for MOVE, {@code error} only for INVALID. */
    public record Result(Type type, int cell, String error) {

        static Result move(int cell) {
            return new Result(Type.MOVE, cell, null);
        }

        static Result quit() {
            return new Result(Type.QUIT, 0, null);
        }

        static Result help() {
            return new Result(Type.HELP, 0, null);
        }

        static Result invalid(String error) {
            return new Result(Type.INVALID, 0, error);
        }
    }

    private InputParser() {
    }

    public static Result parse(String line) {
        String input = line.strip();
        if (input.isEmpty()) {
            return Result.invalid("Please enter a cell number from 1 to 9.");
        }

        String lower = input.toLowerCase(Locale.ROOT);
        if (lower.equals("q") || lower.equals("quit")) {
            return Result.quit();
        }
        if (lower.equals("h")) {
            return Result.help();
        }

        if (input.chars().anyMatch(Character::isWhitespace)) {
            return Result.invalid("Please enter only one cell number.");
        }
        // Checked explicitly so non-ASCII digits (e.g. Arabic-Indic numerals) are rejected.
        boolean allAsciiDigits = input.chars().allMatch(c -> c >= '0' && c <= '9');
        if (!allAsciiDigits) {
            return Result.invalid("'" + input + "' is not a cell number. Enter a digit from 1 to 9.");
        }
        if (input.length() != 1 || input.charAt(0) == '0') {
            return Result.invalid("Cell " + input + " is out of range. Enter a digit from 1 to 9.");
        }
        return Result.move(input.charAt(0) - '0');
    }
}
