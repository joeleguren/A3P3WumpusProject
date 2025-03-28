package inscaparrella.view;

import inscaparrella.controller.LaberynthController;
import inscaparrella.utils.MovementDirection;
import inscaparrella.utils.ShootDirection;

import java.io.File;
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
            option = keyboard.nextInt();
            keyboard.nextLine();

            switch (option) {
                case 0:
                    System.out.println("Sortint...");
                    break;
                case 1:
                    System.out.println("Carregar partida...");
                    System.out.print("Indica quin fitxer de partida vols carrregar (per defecte files/wumpus1.txt): ");
                    String filename = keyboard.nextLine();
                    if (filename.trim().isEmpty() || filename.equals("\n") || filename.isBlank())  {
                        filename = "files" + File.separator + "wumpus1.txt";
                    }
                    System.out.println("\n\n");
                    lc.loadLaberynth(filename);
                    started = lc.startGame();
                    break;
                case 2:
                    System.out.println("Crear nova partida...");
                    System.out.print("Indica un fitxer per guardar la nova partida (per defecte files/wumpus2.txt): ");
                    String filenameToSave = keyboard.nextLine();
                    if (filenameToSave.trim().isEmpty() || filenameToSave.equals("\n") || filenameToSave.isBlank()) {
                        filenameToSave = "files" + File.separator + "wumpus2.txt";
                    }
                    System.out.println("\n\n");
                    lc.saveLaberynth(filenameToSave);
                    started = lc.startGame();
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

                if (isActionValid(playerAction) && isUpperCase(playerAction)) {
                    shoot(lc, playerAction);

                } else if (isActionValid(playerAction) && isLowerCase(playerAction)) {
                    move(lc, playerAction);
                }

                if (lc.isGameEnded() && lc.isGameWon()) {
                    System.out.println(lc);
                    System.out.println("ENHORABONA, HAS POGUT CAÇAR AL WUMPUS");

                } else if (lc.isGameEnded() && !lc.isGameWon()) {
                    System.out.println(lc);
                    System.out.println("GAME OVER");
                }

            }
        } while (option!=0);

    }

    /**
     * Mou el jugador en la posició especifi
     * @param lc
     * @param playerAction
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

    private static boolean isUpperCase(String str) {
        return str.equals(str.toUpperCase());
    }

    private static boolean isLowerCase(String str) {
        return str.equals(str.toLowerCase());
    }

    private static boolean isActionValid(String str) {
        str = str.toUpperCase();
        return str.equals("W") || str.equals("A") || str.equals("S") || str.equals("D");
    }

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
    
    private static String getActionMenu() {
        return "w -> moure amunt; s -> moure abaix; a -> moure esquerra; d -> moure dreta\n" +
        "W -> disparar amunt; S -> disparar abaix; A -> disparar esquerra; D disparar dreta\n" +
        "Opció: ";
    }

    private static String getMenu() {
        return "~~~ HUNT THE WUMPUS ~~~\n" +
                "   0. Sortir\n" +
                "   1. Carregar partida\n" +
                "   2. Crear nova partida\n";
    }

}