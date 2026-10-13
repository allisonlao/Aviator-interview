package tictactoe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public final class Main {

    private static final String USAGE = "Usage: java -jar tictactoe.jar [--seed <long>]";

    private Main() {
    }

    public static void main(String[] args) {
        Random random;
        if (args.length == 0) {
            random = new Random();
        } else if (args.length == 2 && args[0].equals("--seed")) {
            try {
                random = new Random(Long.parseLong(args[1]));
            } catch (NumberFormatException e) {
                System.err.println("Invalid seed '" + args[1] + "': must be a whole number.");
                System.err.println(USAGE);
                System.exit(2);
                return;
            }
        } else {
            System.err.println(USAGE);
            System.exit(2);
            return;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        new Game(in, System.out, new ComputerPlayer(random)).run();
    }
}
