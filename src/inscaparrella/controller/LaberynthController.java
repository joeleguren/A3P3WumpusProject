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

    public void loadLaberynth(String filename) {

        FileReader fr = null;

        try {
            fr = new FileReader(filename);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e); // Canviar excepció per un missatge d'error
        }

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
        try {
            fr.close();
            br.close();
            sc.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveLaberynth(String filename) {
        FileWriter fw;
        String linia = "";

        for (int col = 0; col < laberynth.getLaberynth().size(); col++) {
            for (int row = 0; row < laberynth.getLaberynth().get(col).size(); row++) {

                if (laberynth.getLaberynth().get(row).get(col).getCtype() == CellType.NORMAL){
                    linia += "N ";

                } else if (laberynth.getLaberynth().get(row).get(col).getCtype() == CellType.POWERUP) {
                    linia += "P ";

                } else if (laberynth.getLaberynth().get(row).get(col).getCtype() == CellType.WELL) {
                    linia += "W ";

                }
            }

            linia += "\n";

            try {
                fw = new FileWriter(filename);
                fw.write(linia);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (int col = 0; col < laberynth.getLaberynth().size(); col++) {
            for (int row = 0; row < laberynth.getLaberynth().get(col).size(); row++) {

                if (laberynth.getLaberynth().get(row).get(col).getCtype() == CellType.NORMAL) {

                    NormalCell nCell = (NormalCell) laberynth.getLaberynth().get(row).get(col);

                    if (nCell.getInhabitant() == InhabitantType.WUMPUS){
                        linia = row + " " + col + "\n";
                    } else if (nCell.getInhabitant() == InhabitantType.BAT) {
                        linia = row + " " + col + " ";
                    }
                }
            }
        }

        try {
            fw = new FileWriter(filename);
            fw.write(linia);
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean startGame(){
        boolean started = false;

        int[] celaInicial = this.laberynth.getInitialCell();

        if (celaInicial!=null) {
            this.gameEnded = false;
            this.won = false;
            this.player.setStartingCell(celaInicial[0], celaInicial[1]);
            this.traverseMessage = laberynth.currentCellMessage();
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
                traverseCell();
            }

        }
        if (!isGameEnded()) {

            this.laberynth.moveBats();
            this.echoes = laberynth.emitEchoes();

        }
    }

    public void huntTheWumpus(ShootDirection direction) {
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
        String retorn = "CEL·LA ACTUAL:\n\t";
        retorn += getLastTraverseMessage() + "\n\n"; // TraverseMessage ha de retornar, currentCellMessage() + text de la informació de la cel·la  (mal, powerups...). Vigila que si cau damunt d'un bat ho repetirà varios cops. I també mira els pasos d'execució del main al pdf, que deia algo del bat.
        retorn += this.player.toString() + "\n";
        retorn += getLastEchoes() + "\n";
        retorn += this.laberynth.toString();

        return retorn;
    }

    private void traverseCell() {
        Danger actualDanger = this.laberynth.getDanger();

        this.traverseMessage = this.laberynth.currentCellMessage();

        if (actualDanger == Danger.WUMPUS) {
            this.gameEnded = true;
            this.traverseMessage += "\n\t\tEl Wumpus ha atacat i malferit al jugador";

        } else if (actualDanger == Danger.BAT) {
            this.traverseMessage += "\n\t\tUn ratpenat s’enduu el jugador!";
            int[] playerPos = this.laberynth.batKidnapping();
            if (playerPos != null) {
                int row = playerPos[0];
                int col = playerPos[1];
                this.player.move(row, col);

                actualDanger = laberynth.getDanger(); // Aqui mira de nou el getDanger, tornem a cridar la funció

                if (actualDanger != Danger.NONE) { // Si el segrest del bat, el fa caure en una cela perillosa
                    this.traverseMessage += "\n";
                    traverseCell();
                } else if (laberynth.getPowerUp() != PowerUp.NONE) { // Si no cau en una cel·la perillosa
                    this.traverseMessage += "\n";
                    traverseCell();
                }
            }
        } else if (this.laberynth.getPowerUp() != PowerUp.NONE) {
            this.laberynth.getPowerUp();

            this.traverseMessage += "\n\t\tEl jugador ha trobat una unitat del poder ";

            if (this.laberynth.getPowerUp() == PowerUp.JUMPER_BOOTS) {
                this.traverseMessage += "JUMPER_BOOTS";
                this.player.addPower(PowerUp.JUMPER_BOOTS);
            } else {
                this.traverseMessage += "ARROW";
                this.player.addPower(PowerUp.ARROW);
            }

        } else if (actualDanger == Danger.WELL) {
            if (this.player.getPowerUpQuantity(PowerUp.JUMPER_BOOTS) > 0) {
                this.traverseMessage += "\n\t\tEl jugador ha estat a punt de caure en un pou, però, per sort, portava les JUMPER BOOTS";
                this.player.usePower(PowerUp.JUMPER_BOOTS);

            } else {
                this.traverseMessage += "\n\t\tEl jugador ha caigut en un pou i ha quedat malferit!";
                this.gameEnded = true;
            }
        } else if (actualDanger == Danger.NONE){ // Si no hi ha mal
            this.traverseMessage += " ";
        }
    }
}
