package inscaparrella.view;

import inscaparrella.controller.LaberynthController;

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

        do {

            System.out.print("\n" + getMenu());
            System.out.print("Opció: ");
            option = keyboard.nextInt();

            switch (option) {
                case 0:
                    System.out.println("Sortint...");
                    break;
                case 1:
                    System.out.println("Carregar partida...");
                    break;
                case 2:
                    System.out.println("Crear nova partida...");
                    break;
                default:
                    System.out.println("Opció incorrecta...");
                    break;

            }

        } while (option!=0);


    }

    private static String getMenu() {
        return "~~~ HUNT THE WUMPUS ~~~\n" +
                "   0. Sortir\n" +
                "   1. Carregar partida\n" +
                "   2. Crear nova partida\n";
    }
}
