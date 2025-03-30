package inscaparrella.view;

import inscaparrella.controller.LaberynthController;
import inscaparrella.utils.ConsoleColors;
import inscaparrella.utils.MovementDirection;
import inscaparrella.utils.ShootDirection;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Autors:
 * Joel Eguren
 * Joel Espinós
 * Guim Moya
 *
 */

public class WumpusMain {

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        LaberynthController lc = new LaberynthController();
        int option;

        boolean started = false;

        do {

            System.out.print("\n" + getMenu());
            System.out.print("Opció: ");
            try {
                option = keyboard.nextInt();
                keyboard.nextLine();
            } catch (Exception e) {
                keyboard.nextLine(); // Neteja el buffer
                option = -1; // Per a que surti opció per defecte.
            }

            switch (option) {
                case 0:
                    System.out.println(ConsoleColors.GREEN_BOLD + "Sortint..." + ConsoleColors.RESET);
                    break;
                case 1:
                    System.out.print("Indica quin fitxer de partida vols carrregar (per defecte files/wumpus1.txt): ");
                    String filename = keyboard.nextLine();
                    if (filename.trim().isEmpty() || filename.equals("\n") || filename.isBlank())  {
                        filename = "files" + File.separator + "wumpus1.txt";
                    }
                    try {
                        lc.loadLaberynth(filename);
                        started = lc.startGame();
                    } catch (Exception e) {
                        System.out.println(ConsoleColors.RED_BOLD + "ERROR: " + ConsoleColors.RED + "el fitxer no existeix o està mal format." + ConsoleColors.RESET);
                    }
                    break;
                case 2:
                    System.out.print("Indica un fitxer per guardar la nova partida (per defecte files/wumpus2.txt): ");
                    String filenameToSave = keyboard.nextLine();
                    if (filenameToSave.trim().isEmpty() || filenameToSave.equals("\n") || filenameToSave.isBlank()) {
                        filenameToSave = "files" + File.separator + "wumpus2.txt";
                    }

                    try {
                        lc.saveLaberynth(filenameToSave);
                        started = lc.startGame();
                    } catch (IOException e) {
                        System.out.println(ConsoleColors.RED_BOLD + "ERROR: " + ConsoleColors.RED +"la partida no s'ha pogut guardar." + ConsoleColors.RESET);
                    }
                    break;
                default:
                    System.out.println("Opció incorrecta...");
                    break;
            }

            while (started && !lc.isGameEnded()) {
                System.out.println("\n\n");
                System.out.println(lc); // Mostrem estat partida
                System.out.print(getActionMenu()); // Mostrem opcions usuari
                String playerAction = keyboard.next(); // Demanem opció

                if (isActionValid(playerAction) && isUpperCase(playerAction)) { // Disparar una fletxa
                    shoot(lc, playerAction);

                } else if (isActionValid(playerAction) && isLowerCase(playerAction)) { // Moure's
                    move(lc, playerAction);
                }

                if (lc.isGameEnded()) { // Arreglat, no tocar
                    System.out.println(lc);
                    if (lc.isGameWon()) System.out.println("ENHORABONA, HAS POGUT CAÇAR AL WUMPUS");
                    else  System.out.println("GAME OVER");
                    lc = new LaberynthController();
                    started = false; // faltava ficar això
                }

            }
        } while (option!=0);

    }

    /**
     * Mou el jugador en la posició especificada
     * @param lc LaberynthController
     * @param playerAction Direcció on es vol moure (String)
     */
    private static void move(LaberynthController lc, String playerAction) {

        if (playerAction.equals("w")) {
            lc.movePlayer(MovementDirection.UP);

        } else if (playerAction.equals("a")) {
            lc.movePlayer(MovementDirection.LEFT);

        } else if (playerAction.equals("s")) {
            lc.movePlayer(MovementDirection.DOWN);

        } else if (playerAction.equals("d")) {
            lc.movePlayer(MovementDirection.RIGHT);
        }
    }

    /**
     * Comprova si una String està en majúscules
     * @param str
     * @return {@code True} si una String està en majúscules
     */
    private static boolean isUpperCase(String str) {
        return str.equals(str.toUpperCase());
    }

    /**
     * Comprova si una String està en minúscules
     * @param str
     * @return {@code True} si una String està en minúscules
     */
    private static boolean isLowerCase(String str) {
        return str.equals(str.toLowerCase());
    }

    /**
     * Comprova si una acció és vàlida, és a dir, el moviment/tir està dins les possibilitats del joc.
     * @param str
     * @return {@code True} si l'acció (String) és vàlida.
     */
    private static boolean isActionValid(String str) {
        str = str.toUpperCase();
        return str.equals("W") || str.equals("A") || str.equals("S") || str.equals("D");
    }

    /**
     * Dispara una fletxa en la posició especificada
     * @param lc LaberynthController
     * @param direction Direcció on vol disparar (String)
     */
    private static void shoot (LaberynthController lc, String direction) {
        if (direction.equals("W")) {
            lc.huntTheWumpus(ShootDirection.UP);
        } else if (direction.equals("A")) {
            lc.huntTheWumpus(ShootDirection.LEFT);
        } else if (direction.equals("S")) {
            lc.huntTheWumpus(ShootDirection.DOWN);
        } else if (direction.equals("D")) {
            lc.huntTheWumpus(ShootDirection.RIGHT);
        }
    }

    /**
     * Ajuda d'accions que pot realitzar el jugador
     * @return {@code String} L'ajuda d'accions.
     */
    private static String getActionMenu() {
        return "w -> moure amunt; s -> moure abaix; a -> moure esquerra; d -> moure dreta\n" +
        "W -> disparar amunt; S -> disparar abaix; A -> disparar esquerra; D disparar dreta\n" +
        "Opció: ";
    }

    /**
     * Menú principal del joc
     * @return {@code String} Menú amb llistat d'opcions.
     */
    private static String getMenu() {
        return ConsoleColors.GREEN_BOLD +  "~~~ HUNT THE WUMPUS ~~~\n" + ConsoleColors.RESET +
              "\t0. Sortir\n" +
                "\t1. Carregar partida\n" +
                "\t2. Crear nova partida\n";
    }

}