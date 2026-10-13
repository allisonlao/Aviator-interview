package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.junit.jupiter.api.Test;

class GameTest {

    private static final String PROMPT = "Your move (1-9, h for help, q to quit): ";
    private static final String KEY = " 1 | 2 | 3 \n-----------\n 4 | 5 | 6 \n-----------\n 7 | 8 | 9 \n";

    /** Runs a game with the given input lines and seed, returning everything printed. */
    private static String play(long seed, String input) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(new StringReader(input));
        new Game(in, out, new ComputerPlayer(new Random(seed))).run();
        return bytes.toString(StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    private static int count(String haystack, String needle) {
        int count = 0;
        for (int i = haystack.indexOf(needle); i >= 0; i = haystack.indexOf(needle, i + 1)) {
            count++;
        }
        return count;
    }

    @Test
    void scriptedFullGameWithFixedSeedProducesExactOutput() {
        // With seed 42 the computer answers 7, then 6. X wins on the 1-5-9 diagonal.
        String output = play(42, "5\n1\n9\nn\n");

        String expected = "Cells are numbered like this:\n"
            + KEY
            + "\n"
            + "   |   |   \n"
            + "-----------\n"
            + "   |   |   \n"
            + "-----------\n"
            + "   |   |   \n"
            + PROMPT + "Computer plays 7\n"
            + "\n"
            + "   |   |   \n"
            + "-----------\n"
            + "   | X |   \n"
            + "-----------\n"
            + " O |   |   \n"
            + PROMPT + "Computer plays 6\n"
            + "\n"
            + " X |   |   \n"
            + "-----------\n"
            + "   | X | O \n"
            + "-----------\n"
            + " O |   |   \n"
            + PROMPT + "\n"
            + " X |   |   \n"
            + "-----------\n"
            + "   | X | O \n"
            + "-----------\n"
            + " O |   | X \n"
            + "You win\n"
            + "Play again? (y/n) Goodbye.\n";
        assertEquals(expected, output);
    }

    @Test
    void reportsComputerWin() {
        // With seed 3 the computer answers 7, 5, 3 and wins on the 3-5-7 diagonal.
        String output = play(3, "1\n2\n4\nn\n");
        assertTrue(output.contains(" X | X | O \n-----------\n X | O |   \n-----------\n O |   |   \nComputer wins\n"));
    }

    @Test
    void occupiedCellIsRejectedAndPlayerKeepsTheirTurn() {
        // Seed 42: computer takes 7 after X plays 5. The player then tries 5 and 7 before playing 1.
        String output = play(42, "5\n5\n7\n1\n9\nn\n");
        assertTrue(output.contains("Cell 5 is already taken. Choose an empty cell.\n"));
        assertTrue(output.contains("Cell 7 is already taken. Choose an empty cell.\n"));
        assertEquals(2, count(output, "Computer plays"));
        assertTrue(output.contains("You win\n"));
    }

    @Test
    void invalidInputShowsErrorAndReprompts() {
        String output = play(42, "\nabc\n0\n5 6\n5\n1\n9\nn\n");
        assertTrue(output.contains(PROMPT + "Please enter a cell number from 1 to 9.\n"));
        assertTrue(output.contains(PROMPT + "'abc' is not a cell number. Enter a digit from 1 to 9.\n"));
        assertTrue(output.contains(PROMPT + "Cell 0 is out of range. Enter a digit from 1 to 9.\n"));
        assertTrue(output.contains(PROMPT + "Please enter only one cell number.\n"));
        // Same computer replies as the clean game: invalid input never cost a turn.
        assertTrue(output.contains("Computer plays 7\n"));
        assertTrue(output.contains("Computer plays 6\n"));
        assertTrue(output.contains("You win\n"));
    }

    @Test
    void helpPrintsTheNumberKeyAgain() {
        String output = play(42, "h\nq\n");
        assertEquals(2, count(output, KEY));
    }

    @Test
    void quitEndsTheProgramMidGame() {
        String output = play(42, "5\nQUIT\n");
        assertTrue(output.endsWith(PROMPT + "Goodbye.\n"));
        assertFalse(output.contains("Play again?"));
        assertEquals(1, count(output, "Computer plays"));
    }

    @Test
    void endOfInputMidGameExitsCleanly() {
        String output = play(42, "5\n");
        assertTrue(output.endsWith(PROMPT + "\nGoodbye.\n"));
        assertFalse(output.contains("Play again?"));
    }

    @Test
    void endOfInputAtReplayPromptMeansNo() {
        String output = play(42, "5\n1\n9\n");
        assertTrue(output.endsWith("You win\nPlay again? (y/n) \nGoodbye.\n"));
    }

    @Test
    void replayPromptRejectsUnknownAnswers() {
        String output = play(42, "5\n1\n9\nmaybe\n\nNO\n");
        assertEquals(3, count(output, "Play again? (y/n) "));
        assertEquals(2, count(output, "Please answer y or n.\n"));
        assertTrue(output.endsWith("Goodbye.\n"));
    }

    @Test
    void answeringYesStartsAFreshGame() {
        String output = play(42, "5\n1\n9\nYes\nq\n");
        int secondGame = output.indexOf("Cells are numbered like this:", 1);
        assertTrue(secondGame > 0, "second game did not start");
        String secondGameOutput = output.substring(secondGame);
        assertTrue(secondGameOutput.contains(
            "   |   |   \n-----------\n   |   |   \n-----------\n   |   |   \n" + PROMPT));
        assertTrue(output.endsWith("Goodbye.\n"));
    }
}
