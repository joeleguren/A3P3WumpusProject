package inscaparrella.controller;

import inscaparrella.model.*;
import inscaparrella.utils.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


public class LaberynthController {
    private WumpusLaberynth laberynth;
    private Player player;
    private String traverseMessage;
    private String echoes;
    private boolean gameEnded;
    private boolean won;

    public LaberynthController() {
        laberynth = new WumpusLaberynth();
        player = new Player();
        traverseMessage = "";
        echoes = "";
        gameEnded = false;
        won = false;
    }

    public void loadLaberynth(String filename) throws IOException {

        FileReader fr = new FileReader(filename);

        BufferedReader br = new BufferedReader(fr);

        Scanner sc = new Scanner(br);               //Scanner d'arxiu

        String llegit;

        llegit = sc.nextLine();                     //Llegir linia

        ArrayList<ArrayList<Cell>> tauler = new ArrayList<>();  //Tauler a retornar

        int row = 0;
        int col = 0;

        while (llegit != null) {

            ArrayList<Cell> columna = null;
            String[] linia = llegit.split(" ");         //Scanner splitejat

            if (!Character.isDigit(llegit.charAt(0))) {
                NormalCell nCell;
                PowerUpCell pCell;
                WellCell wCell;

                columna = new ArrayList<>();

                for (col = 0; col < linia.length; col++) {

                    if (linia[col].equals("N")) {
                        nCell = new NormalCell(row, col);
                        columna.add(nCell);

                    } else if (linia[col].equals("P")) {
                        pCell = new PowerUpCell(row, col);
                        columna.add(pCell);

                    }else if (linia[col].equals("W")) {
                        wCell = new WellCell(row, col);
                        columna.add(wCell);

                    }

                }

                tauler.add(columna);

            } else {
                int row1 = Integer.parseInt(linia[0]);
                int col1 = Integer.parseInt(linia[1]);

                if (linia.length == 2 && tauler.get(row1).get(col1).getCtype() == CellType.NORMAL) {
                    NormalCell nCell = (NormalCell) tauler.get(Integer.parseInt(linia[0])).get(Integer.parseInt(linia[1]));

                    if (nCell.getInhabitant() == InhabitantType.NONE){
                        nCell.setInhabitant(InhabitantType.WUMPUS);             //Posicionar Wumpus
                    }

                } else {
                    try {
                        for (int i = 0; i < linia.length; i = i+2) {
                            NormalCell nCell = (NormalCell) tauler.get(Integer.parseInt(linia[i])).get(Integer.parseInt(linia[i+1]));

                            if (nCell.getInhabitant() == InhabitantType.NONE){
                                nCell.setInhabitant(InhabitantType.BAT);             //Posicionar Bat
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("Ha petat");
                    }
                }
            }

            row++;

            if (sc.hasNextLine()) {
                llegit = sc.nextLine();
            } else {
                llegit = null;
            }
        }
        laberynth.setLaberynth(tauler);

        fr.close();
        br.close();
        sc.close();
    }

    public void saveLaberynth(String filename) throws IOException {
        laberynth.createNewLaberynth();             //Generar nou tauler
        ArrayList<ArrayList<Cell>> tauler = laberynth.getLaberynth();  //Tauler a retornar


        FileWriter fw;
        String linia = "";

        for (int row = 0; row < tauler.size(); row++) {
            for (int col = 0; col < tauler.get(row).size(); col++) {

                if (tauler.get(row).get(col).getCtype() == CellType.NORMAL){
                    linia += "N ";

                } else if (tauler.get(row).get(col).getCtype() == CellType.POWERUP) {
                    linia += "P ";

                } else if (tauler.get(row).get(col).getCtype() == CellType.WELL) {
                    linia += "W ";

                }
            }

            linia += "\n";

        }

        String batLine = "";
        for (int row = 0; row < tauler.size(); row++) {
            for (int col = 0; col < tauler.get(row).size(); col++) {

                if (tauler.get(row).get(col).getCtype() == CellType.NORMAL) {

                    NormalCell nCell = (NormalCell) tauler.get(row).get(col);

                    if (nCell.getInhabitant() == InhabitantType.WUMPUS){
                        linia += row + " " + col + "\n";
                    } else if (nCell.getInhabitant() == InhabitantType.BAT) {
                        batLine += row + " " + col + " ";
                    }
                }
            }
        }

        fw = new FileWriter(filename);
        fw.write(linia + batLine);
        fw.flush();
    }

    public boolean startGame(){
        boolean started = false;

        int[] celaInicial = this.laberynth.getInitialCell();

        if (celaInicial!=null) {
            this.gameEnded = false;
            this.won = false;
            this.player.setStartingCell(celaInicial[0], celaInicial[1]);
            this.traverseMessage = "\t" + laberynth.currentCellMessage();
            this.echoes = laberynth.emitEchoes();
            started = true;
        }

        return started;

    }

    public void movePlayer(MovementDirection direction) {

        if (!isGameEnded()) {
            int[] playerPos = this.laberynth.movePlayer(direction);

            if (playerPos!=null) {

                int row = playerPos[0];
                int col = playerPos[1];
                player.move(row, col);

                this.traverseMessage = ""; // Modificació a l'enunciat, aquesta línia neteja les rondes anteriors.
                traverseCell();
            }

        }
        if (!isGameEnded()) {

            this.laberynth.moveBats();
            this.echoes = laberynth.emitEchoes();

        }
    }



    public void huntTheWumpus(ShootDirection direction) {
        this.traverseMessage = this.laberynth.currentCellMessage(); // Modificació a l'enunciat, per si el jugador ha sigut anteriorment segrestat, i ara vol disparar una fletxa, se inicialitzi el missatge
        if (isGameEnded() || this.player.getPowerUpQuantity(PowerUp.ARROW) > 0) { // Aqui entrava sempre, perque !isGameEnded() sempre era true mentre juguem.
            this.player.usePower(PowerUp.ARROW);
            if (this.laberynth.shootArrow(direction)){
                this.won = true;
                this.gameEnded = true;
            } else {
                this.laberynth.startleWumpus();
            }
        }
    }

    public String getLastTraverseMessage(){
        return this.traverseMessage;
    }

    public String getLastEchoes() {
        return this.echoes;
    }

    public boolean isGameEnded(){
        return this.gameEnded;
    }

    public boolean isGameWon(){
        return this.won;
    }

    @Override
    public String toString() {
        String retorn = ConsoleColors.GREEN_BOLD + "CEL·LA ACTUAL:\n" + ConsoleColors.RESET;
        retorn += getLastTraverseMessage() + "\n";
        retorn += ConsoleColors.GREEN_BOLD + "ECOS:\n" + ConsoleColors.RESET;
        retorn += getLastEchoes() + "\n";
        retorn += ConsoleColors.GREEN_BOLD + this.player.toString() + ConsoleColors.RESET + "\n";
        retorn += this.laberynth.toString();
        return retorn;
    }

    private void traverseCell() {

        // No modificar ordre instruccions
        Danger actualDanger = this.laberynth.getDanger();

        this.traverseMessage += "\t" + this.laberynth.currentCellMessage();

        PowerUp actualPower = this.laberynth.getPowerUp();

        if (actualDanger == Danger.WUMPUS) {
            this.gameEnded = true;
            this.traverseMessage += "\n\t\tEl Wumpus ha atacat i malferit al jugador";

        } else if (actualDanger == Danger.WELL) {
            if (this.player.getPowerUpQuantity(PowerUp.JUMPER_BOOTS) > 0) {
                this.traverseMessage += "\n\t\tEl jugador ha estat a punt de caure en un pou, però, per sort, portava les JUMPER BOOTS";
                this.player.usePower(PowerUp.JUMPER_BOOTS);

            } else {
                this.traverseMessage += "\n\t\tEl jugador ha caigut en un pou i ha quedat malferit!";
                this.gameEnded = true;
            }
        } else if (actualDanger == Danger.BAT) {

            int[] playerPos = this.laberynth.batKidnapping();
            this.traverseMessage += "\n\t\tUn ratpenat s’enduu el jugador!";
           // this.traverseMessage += "\n" + laberynth.currentCellMessage() + "\n\t\tUn ratpenat s’enduu el jugador!";

            if (playerPos != null) {
                int row = playerPos[0];
                int col = playerPos[1];
                this.player.move(row, col);

                this.traverseMessage += "\n";
                traverseCell();
            }

        } else if (actualPower != PowerUp.NONE) {

            this.traverseMessage += "\n\t\tEl jugador ha trobat una unitat del poder ";

            if (actualPower == PowerUp.JUMPER_BOOTS) this.traverseMessage += "JUMPER_BOOTS";
            else if (actualPower == PowerUp.ARROW) this.traverseMessage += "ARROW";

            this.player.addPower(actualPower);
        }
    }
}
