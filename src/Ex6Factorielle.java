import java.math.BigInteger;
import java.util.Scanner;

/**
 * Ex6 — Factorielle (n!)
 * n! = n × (n - 1) × (n - 2) × ... × 1
 *
 * Visual :
 * - Ask user for n (n > 0 & n <= 50)
 * - Compute factorial
 * - Show each step
 */
public final class Ex6Factorielle {

    ////////////////// SHARED SCANNER ////////////////////

    private static final Scanner SC = new Scanner(System.in);

    ////////////////// CLI COLORS ////////////////////

    private static final String RESET = "\u001B[0m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    /////////////////// DELAY ///////////////////////

    private static final int DELAY_MS = 120;

    ////////////////// MAX BOUND & CHAIN ELLIPSIS /////////////////////

    private static final int MAX_N = 50;
    private static final int FULL_CHAIN_MAX = 12;

    public static void main(String[] args) {
        System.out.println(CYAN + "=== Ex 6 : Factorielle ===" + RESET);

        System.out.print("Saisir un entier supérieur à 0 (max  : " + MAX_N + ") : ");
        int n = readPositiveInt(1, MAX_N);
        System.out.println();

        BigInteger result = factorialVisual(n);

        System.out.printf("%nRésultat final : %s%d! = %s%s%n",
                YELLOW, n, result.toString(), RESET);

        SC.close();
    }

    /**
     * Recursive factorial.
     *
     * @param n     current target value (ex : 5)
     * @param step  current recursion depth for console
     * @param chain current visual chain (ex : "1 × 2 × 3")
     * @return factorial result for n
     *         Example :
     *         Saisir un entier supérieur à 0 : 5
     *         Étape 1 : 1! = 1
     *         Étape 2 : 2! = 1 × 2 = 2
     *         Étape 3 : 3! = 1 x 2 × 3 = 6
     *         Étape 4 : 4! = 1 x 2 x 3 × 4 = 24
     *         Étape 5 : 5! = 1 x 2 x 3 × 4 x 5 = 120
     */
    private static BigInteger factorialVisual(int n) {
        if (n == 1) {

            System.out.println("Étape 1 : 1! = 1");
            pause(DELAY_MS);
            return BigInteger.ONE;
        }

        // Recurse down to 1 to print steps
        BigInteger prev = factorialVisual(n - 1); // (n-1)!
        BigInteger result = prev.multiply(BigInteger.valueOf(n)); // n! = (n-1)! × n

        // Build human readable chain
        String chain = buildChain(n, FULL_CHAIN_MAX);

        System.out.printf("Étape %d : %s%d!%s = %s = %s%s%s%n",
                n, RED, n, RESET, chain, YELLOW, result.toString(), RESET);
        pause(DELAY_MS);

        return result;
    }

    /**
     * Build the visual chain "1 × 2 × ... × n".
     *
     * @param n       upper bound
     * @param fullMax ellipsis start
     * @return string like "1 × 2 × 3 × ... × n"
     *         - If n <= fullMax: "1 × 2 × ... × n"
     *         - Else : "1 × 2 × 3 × … × (n-1) × n"
     */
    private static String buildChain(int n, int fullMax) {
        if (n <= 1)
            return "1";
        if (n <= fullMax) {
            // Full chain : "1 × 2 × ... × n"
            StringBuilder sb = new StringBuilder("1");
            for (int i = 2; i <= n; i++)
                sb.append(" × ").append(i);
            return sb.toString();
        }
        // Compact chain : "1 × 2 × 3 × … × (n-1) × n"
        if (n == 2)
            return "1 × 2";
        if (n == 3)
            return "1 × 2 × 3";
        return "1 × 2 × 3 × … × " + (n - 1) + " × " + n;
    }

    /**
     * Read integer & re-prompt until valid.
     */
    private static int readPositiveInt(int min, int max) {
        while (true) {
            while (!SC.hasNextInt()) {
                System.out.print("Erreur : entrez un entier valide : ");
                SC.nextLine(); // discard invalid token
            }
            int val = SC.nextInt();
            SC.nextLine(); // clear
            if (val < min || val > max) {
                System.out.print("Le nombre doit être entre " + min + " et " + max + ". Réessaye : ");
                continue;
            }
            return val;
        }
    }

    /**
     * Pause.
     */
    private static void pause(int ms) {
        if (ms <= 0)
            return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
