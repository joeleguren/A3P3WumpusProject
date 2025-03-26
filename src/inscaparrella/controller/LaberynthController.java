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
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(fr);

        Scanner sc = new Scanner(br);               //Scanner d'arxiu

        String llegit;

        llegit = sc.nextLine();                     //Llegir linia

        ArrayList<ArrayList<Cell>> tauler = new ArrayList<>();  //Tauler a retornar

        int col = 0;
        int row = 0;
        while (llegit != null) {

            ArrayList<Cell> fila = null;
            String[] linia = llegit.split(" ");         //Scanner splitejat

            if (!Character.isDigit(llegit.charAt(0))) {
                NormalCell nCell;
                PowerUpCell pCell;
                WellCell wCell;
                for (row = 0; row < linia.length; row++) {

                    fila = new ArrayList<>();

                    if (linia[row] == "N") {
                        nCell = new NormalCell(row, col);
                        fila.add(nCell);
                    } else if (linia[row] == "P") {
                        pCell = new PowerUpCell(row, col);
                        fila.add(pCell);
                    }else if (linia[row] == "W") {
                        wCell = new WellCell(row, col);
                        fila.add(wCell);
                    }

                }

                tauler.add(fila);

            } else {
                if (linia.length == 2 && tauler.get(Integer.parseInt(linia[0])).get(Integer.parseInt(linia[1])).getCtype() == CellType.NORMAL) {
                    NormalCell nCell = (NormalCell) tauler.get(Integer.parseInt(linia[0])).get(Integer.parseInt(linia[1]));

                    if (nCell.getInhabitant() == InhabitantType.NONE){
                        nCell.setInhabitant(InhabitantType.WUMPUS);             //Posicionar Wumpus
                    }

                } else {
                    try {
                        for (int i = 0; i < linia.length; i = i+2) {
                            NormalCell nCell = (NormalCell) tauler.get(Integer.parseInt(linia[i])).get(Integer.parseInt(linia[i+1]));

                            if (nCell.getInhabitant() == InhabitantType.NONE){
                                nCell.setInhabitant(InhabitantType.BAT);             //Posicionar Wumpus
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {}
                }
            }
            col++;

            llegit = sc.nextLine();
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
        gameEnded = false;
        won = false;
        player.setStartingCell(laberynth.getInitialCell()[0], laberynth.getInitialCell()[1]);
        traverseMessage = laberynth.currentCellMessage();
        echoes = laberynth.emitEchoes();
        return true;
    }

    public void movePlayer(MovementDirection direction) {
        if (!isGameEnded()) {
            laberynth.movePlayer(direction);
            traverseCell();
        }
        if (!isGameEnded()) {
            laberynth.moveBats();
            laberynth.emitEchoes();
        }
    }

    public void huntTheWumpus(ShootDirection direction) {
        if (!isGameEnded() || player.getPowerUpQuantity(PowerUp.ARROW) > 0) {
            player.usePower(PowerUp.ARROW);
            if (laberynth.shootArrow(direction)){
                won = true;
                gameEnded = true;
            } else {
                laberynth.startleWumpus();
            }
        }
    }

    public String getLasTraverseMessage(){
        return traverseMessage;
    }

    public String getLastEchoes() {
        return echoes;
    }

    public boolean isGameEnded(){
        return gameEnded;
    }

    public boolean isGameWon(){
        return won;
    }

    @Override
    public String toString() {
        return "WumpusController{" +
                "laberynth=" + laberynth +
                ", player=" + player +
                ", traverseMessage='" + traverseMessage + '\'' +
                ", echoes='" + echoes + '\'' +
                ", gameEnded=" + gameEnded +
                ", won=" + won +
                '}';
    }

    private void traverseCell() {

    }
}
