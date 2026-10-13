package tictactoe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class InputParserTest {

    @Test
    void parsesEveryDigitFromOneToNine() {
        for (int cell = 1; cell <= 9; cell++) {
            InputParser.Result result = InputParser.parse(String.valueOf(cell));
            assertEquals(InputParser.Type.MOVE, result.type());
            assertEquals(cell, result.cell());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {" 5", "5 ", "  5  ", "\t5\t", "5\r"})
    void ignoresSurroundingWhitespace(String input) {
        InputParser.Result result = InputParser.parse(input);
        assertEquals(InputParser.Type.MOVE, result.type());
        assertEquals(5, result.cell());
    }

    @ParameterizedTest
    @ValueSource(strings = {"q", "Q", "quit", "QUIT", "QuIt", "  q  "})
    void recognisesQuitInAnyCase(String input) {
        assertEquals(InputParser.Type.QUIT, InputParser.parse(input).type());
    }

    @ParameterizedTest
    @ValueSource(strings = {"h", "H", " h "})
    void recognisesHelp(String input) {
        assertEquals(InputParser.Type.HELP, InputParser.parse(input).type());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t"})
    void rejectsEmptyInput(String input) {
        assertInvalid(input, "Please enter a cell number from 1 to 9.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "10", "99", "05"})
    void rejectsOutOfRangeNumbers(String input) {
        assertInvalid(input, "Cell " + input + " is out of range. Enter a digit from 1 to 9.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "x", "5.0", "+5", "-1", "five", "5,6", "qq", "help", "\u0665", "\u00b2"})
    void rejectsNonDigits(String input) {
        assertInvalid(input, "'" + input + "' is not a cell number. Enter a digit from 1 to 9.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"5 6", "1 2 3", "q 5", "5\t6"})
    void rejectsMultipleValues(String input) {
        assertInvalid(input, "Please enter only one cell number.");
    }

    private static void assertInvalid(String input, String expectedError) {
        InputParser.Result result = InputParser.parse(input);
        assertEquals(InputParser.Type.INVALID, result.type(), "input: '" + input + "'");
        assertEquals(expectedError, result.error());
    }
}
