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
public final class LeJeuDuPendu {

    ////////////////// SHARED SCANNER ////////////////////

    private static final Scanner SC = new Scanner(System.in);

    ////////////////// CLI COLORS ////////////////////

    private static final String RESET = "\u001B[0m";
    // private static final String YELLOW = "\u001B[33m";
    // private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    /////////////////// DELAY ///////////////////////

    // private static final int DELAY_MS = 120;

    ////////////////// MAX BOUND & CHAIN ELLIPSIS /////////////////////

    public static void main(String[] args) {
        System.out.println(CYAN + "=== Le Super jeu du Pendu ===" + RESET);

        // Faire un jeu de data (private static String)
        // Random un mot (fnct randomWord)
        // dessiner le placeholder (system.out.print("_"))
        // initialiser le counter (i=0)
        // tant que count <=10 -> loop
        // Demander une lettre (scan +check input(char) gestion erreurs)
        // check si le mot contient user input (recup index dans un array car possible
        // plusieurs occurences)
        // si non: dessiner la potence et nbr d'essais restants
        // si si oui : afficher la lettre à la place de _
        // incrementer le counter
        // counter >10 ->pendu

        // afficher lettres dejà proposées

        SC.close();
    }

    /**
     * Pause.
     */
    // private static void pause(int ms) {
    // if (ms <= 0)
    // return;
    // try {
    // Thread.sleep(ms);
    // } catch (InterruptedException ignored) {
    // }
    // }
}
