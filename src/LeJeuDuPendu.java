import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 * Le Super Jeu du Pendu
 * - Pick a random word
 * - Player guesses letters
 * - Wrong guess increases error counter
 * - At 10, you loose
 **/
public final class LeJeuDuPendu {

    ////////////////// SCANNER ////////////////////

    private static final Scanner SC = new Scanner(System.in);

    ////////////////// CLI COLORS ////////////////////

    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String CYAN = "\u001B[36m";

    /////////////////// GAME CONFIG ///////////////////////

    private static final int DELAY_MS = 120;
    private static final int MAX_ERRORS = 10;
    private static final List<String> WORDS = List.of(
            "python", "orange", "banane", "fenetre", "module",
            "lambda", "stylo", "plage", "tomate", "girafe", "pendu");

    public static void main(String[] args) {
        run();
        SC.close();
    }

    //////////////////// GAME LOOP ////////////////////////////

    private static void run() {
        printlnWithDelay(CYAN + "=== Le Super jeu du Pendu ===" + RESET);

        boolean playAgain = true;

        while (playAgain) {
            playMySuperGame();

            System.out.print("\nRejouer ? (o/n) : ");
            // Accept UpperCase
            String playAgainAnswer = SC.nextLine().trim().toLowerCase(Locale.ROOT);

            // Compare to "o"
            // Anything else is treated as "no".
            playAgain = playAgainAnswer.equals("o");
        }

        System.out.println("Bye...!");
    }

    private static void playMySuperGame() {
        // Random number to pick secret word by index.
        final Random rng = new Random();

        // Pick one word from WORDS & normalize to lowercase.
        final String secret = pickRandomWord(rng);

        // Structures :
        // - attempts : all try
        // - correct : correct letters.
        // - wrong : ordered list of wrong letters.

        final Set<Character> attempts = new HashSet<>(); // 'Set' to prevent duplicate
        final Set<Character> correct = new HashSet<>();
        final List<Character> wrong = new ArrayList<>(); // 'List' to keeep order

        // Wrong letter counter
        int errors = 0;

        // Initial rendering
        drawBoard(errors, secret, attempts, wrong);

        // Main game loop
        while (true) {
            // readLetter returns letter that is NOT in "attempts"
            char letter = readLetter(attempts);
            // Update "attempts"
            attempts.add(letter);

            // get index of letter in 'secret' | -1
            if (secret.indexOf(letter) >= 0) {
                // Correct : update "correct".
                correct.add(letter);
                printlnWithDelay(GREEN + "✅ GG !" + RESET);
            } else {
                // Wrong : increment errors, update 'wrong'.
                errors++;
                wrong.add(letter);
                int remain = MAX_ERRORS - errors;

                // Try left ? continue : "you loose"
                if (remain > 0) {
                    printlnWithDelay(RED + "❌ No way. Erreurs restantes: " + remain + RESET);
                } else {
                    // Final render
                    drawBoard(errors, secret, attempts, wrong);
                    System.out.println(RED + BOLD + "💀 You looooooseeee ! " + MAX_ERRORS + " erreurs...!!" + RESET);
                    System.out.println(YELLOW + "Le mot était : " + secret + RESET);
                    return;
                }
            }

            // After each try, draw board
            drawBoard(errors, secret, attempts, wrong);

            if (isWordRevealed(secret, correct)) {
                System.out.println(GREEN + BOLD + "🎉 GG, tu as trouvé le mot !" + RESET);
                return;
            }
        }
    }

    /////////////////////////// HELPERS /////////////////////////////////////

    // Returns a random word
    private static String pickRandomWord(Random rng) {
        return WORDS.get(rng.nextInt(WORDS.size())).toLowerCase(Locale.ROOT);
    }

    // Builds placeholder string.

    private static String renderProgress(String secret, Set<Character> attempts) {
        StringBuilder stringProgress = new StringBuilder();

        // Iterate over each character : append char | "_",
        for (int i = 0; i < secret.length(); i++) {
            char charac = secret.charAt(i);
            if (attempts.contains(charac))
                stringProgress.append(charac); // revealed
            else
                stringProgress.append('_'); // hidden
            if (i < secret.length() - 1)
                stringProgress.append(' '); // space for human
        }
        return stringProgress.toString();
    }

    private static boolean isWordRevealed(String secret, Set<Character> correct) {
        for (int i = 0; i < secret.length(); i++) {
            if (!correct.contains(secret.charAt(i)))
                return false;
        }
        return true;
    }

    private static char readLetter(Set<Character> already) {
        while (true) {
            System.out.print("Entre une lettre : ");
            String raw = SC.nextLine().trim().toLowerCase(Locale.ROOT);

            if (raw.length() != 1 || !Character.isLetter(raw.charAt(0))) {
                System.out.println(RED + "⚠️  Saisis une seule lettre valide." + RESET);
                continue;
            }

            char validLetter = raw.charAt(0);

            // !allow same letter twice.
            if (already.contains(validLetter)) {
                System.out.println(YELLOW + "ℹ️  Noob..!! Tu as déjà proposé cette lettre." + RESET);
                continue;
            }
            return validLetter;
        }
    }

    // Board rendering
    private static void drawBoard(int errors, String secret, Set<Character> attempts, List<Character> wrong) {
        System.out.println();
        System.out.println(BLUE + "--------------------" + RESET);

        // Draw frame based on number of errors.
        visualGame(errors);

        System.out.println(RED + "Vies : " + BOLD + renderLives(errors) + RESET);
        pause(DELAY_MS / 2);

        System.out.println();
        // Show current state.
        System.out.println("Mot : " + BOLD + renderProgress(secret, attempts) + RESET);

        // Show wrong letters if any
        if (!wrong.isEmpty()) {
            System.out.print("Lettres fausses : ");
            for (Character ch : wrong) {
                System.out.print(RED + ch + RESET + " ");
                pause(DELAY_MS / 2);
            }
            System.out.println();
        }

        // Visual separator for normally constituted human.
        System.out.println(BLUE + "--------------------" + RESET);
    }

    ///////////////// VISUAL ////////////////////////////////////

    // Picks correct "frame" regarding errors.
    private static void visualGame(int errors) {

        int errorIndex = Math.max(0, Math.min(MAX_ERRORS, errors));

        // 11 frames
        String[] frames = new String[] {
                // 0
                """
                          +----+
                          |
                          |
                          |
                          |
                          |
                        =========
                        """,
                // 1
                """
                          +----+
                          |    |
                          |
                          |
                          |
                          |
                        =========
                        """,
                // 2
                """
                          +----+
                          |    |
                          |    O
                          |
                          |
                          |
                        =========
                        """,
                // 3
                """
                          +----+
                          |    |
                          |    O
                          |    |
                          |
                          |
                        =========
                        """,
                // 4
                """
                          +----+
                          |    |
                          |    O
                          |   /|
                          |
                          |
                        =========
                        """,
                // 5
                """
                          +----+
                          |    |
                          |    O
                          |   /|\\
                          |
                          |
                        =========
                        """,
                // 6
                """
                          +----+
                          |    |
                          |    O
                          |   /|\\
                          |   /
                          |
                        =========
                        """,
                // 7
                """
                          +----+
                          |    |
                          |    O
                          |   /|\\
                          |   / \\
                          |
                        =========
                        """,
                // 8
                """
                          +----+
                          |    |
                          |   \\O/
                          |    |
                          |   / \\
                          |
                        =========
                        """,
                // 9
                """
                          +----+
                          |    |
                          |   \\O/
                          |   (x)
                          |   / \\
                          |
                        =========
                        """,
                // 10
                """
                          +----+
                          |    |
                          |    X
                          |   /|\\
                          |   / \\
                          |
                        =========
                        """
        };

        // "Animate" the chosen frame by printing it line-by-line with a small pause.
        String sketch = frames[errorIndex];
        for (String line : sketch.split("\n")) {
            System.out.println(line);
            pause(DELAY_MS);
        }
    }

    private static String renderLives(int errors) {
        int remain = Math.max(0, MAX_ERRORS - errors);
        StringBuilder lifeString = new StringBuilder(MAX_ERRORS * 2);

        for (int i = 0; i < MAX_ERRORS; i++) {
            lifeString.append(i < remain ? '♥' : '·');

            boolean notLast = (i < MAX_ERRORS - 1);
            if (notLast) {
                lifeString.append(' ');

            }
        }
        return lifeString.toString();
    }
    //////////////////// DELAY ////////////////////////////////

    private static void printlnWithDelay(String s) {
        System.out.println(s);
        pause(DELAY_MS);
    }

    private static void pause(int ms) {
        if (ms <= 0)
            return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
