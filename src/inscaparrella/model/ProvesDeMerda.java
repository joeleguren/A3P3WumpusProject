package inscaparrella.model;

import inscaparrella.utils.MovementDirection;

public class ProvesDeMerda {
    public static void main(String[] args) {

        String str = "";

        WumpusLaberynth wl = new WumpusLaberynth();

        wl.createNewLaberynth();
        int[] celaInicial = wl.getInitialCell();

        System.out.println(wl);

        System.out.println("Echos: ");
        System.out.println(wl.emitEchoes());

        if (celaInicial==null) {
            System.out.println("jugador no generat");
        }

        int[] novaPos = wl.movePlayer(MovementDirection.DOWN);
        novaPos = wl.movePlayer(MovementDirection.LEFT);
        novaPos = wl.movePlayer(MovementDirection.LEFT);
        novaPos = wl.movePlayer(MovementDirection.LEFT);
        novaPos = wl.movePlayer(MovementDirection.LEFT);
        novaPos = wl.movePlayer(MovementDirection.LEFT);


        if (novaPos==null) {
            System.out.println("No s'ha pogut moure");
        }
        else {
            System.out.println("Jugador es troba a " + novaPos[0] + " " + novaPos[1]);
        }

        System.out.println(wl);

    }

}
