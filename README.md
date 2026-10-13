# Aviator-interview

Command-line Tic-Tac-Toe: you (X) against the computer (O), played in the terminal.

## Requirements

- Java 17 or newer
- Maven 3.6 or newer

## Build and test

```bash
mvn package        # compiles, runs the tests, builds target/tictactoe.jar
mvn test           # tests only
```

## Run

```bash
java -jar target/tictactoe.jar
java -jar target/tictactoe.jar --seed 42   # repeatable computer moves
```

## How to play

Cells are numbered 1-9, left to right, top to bottom:

```
 1 | 2 | 3 
-----------
 4 | 5 | 6 
-----------
 7 | 8 | 9 
```

At the prompt, type a cell number and press Enter. You always move first.

| Input          | Effect                                |
|----------------|---------------------------------------|
| `1`-`9`        | Place X in that cell                  |
| `h`            | Show the cell number key again        |
| `q` / `quit`   | Exit immediately                      |

After each game, answer `y`/`yes` or `n`/`no` to play again.

## Project layout

```
src/main/java/tictactoe/
  Board.java           board state, legal moves, win/draw detection (no I/O)
  BoardRenderer.java   board and number key to text (pure)
  InputParser.java     one input line to a move, quit, help, or error (pure)
  ComputerPlayer.java  random legal move from an injected java.util.Random
  Game.java            game loop over an injected reader and PrintStream
  Main.java            parses --seed and starts the game
src/test/java/tictactoe/   JUnit 5 tests for each class
```

## Behaviour notes

Choices made where the spec was silent:

- **Exit codes.** Quitting and end of input (Ctrl+D) exit with code 0. An invalid
  command line (unknown argument, or a `--seed` that isn't a whole number) prints
  usage to stderr and exits with code 2. Ctrl+C is left to the JVM's default
  handling, which exits with code 130 and no stack trace.
- **When the board is shown.** The number key and an empty board are shown at
  the start of each game. The board is shown before each of your moves, so after
  "Computer plays N" the next thing you see is the updated board. When the game
  ends, the final board is shown once, followed by the result.
- **Input.** `h` is accepted in any case, like `q`. Multi-digit numbers such as
  `05` or `10` count as out of range. Anything containing a non-ASCII-digit
  character (including non-Latin digits such as `٥`) counts as not a number.
  Input with internal whitespace (`5 6`) counts as multiple values.
- **Replay prompt.** An empty answer is treated as invalid and the prompt is
  repeated.
- **Line endings.** Rendered boards use `\n` between rows. Other lines use the
  platform line separator via `println`.
