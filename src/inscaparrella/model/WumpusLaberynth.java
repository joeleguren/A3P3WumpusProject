package inscaparrella.model;

import inscaparrella.utils.CellType;
import inscaparrella.utils.InhabitantType;

import java.util.ArrayList;
import java.util.Random;

public class WumpusLaberynth {
    private static final int ENTITY = 2;
    private static final int MIN_BOARD_SIZE = 5;
    private static final int MAX_BOARD_SIZE = 15;
    private static final int MIN_WELL_CELLS = 2;
    private static final int MIN_POWER_CELLS = 2;
    private static final int MIN_BATS_ENTITIES = 2;
    private ArrayList<ArrayList<Cell>> laberynth;
    private int[] ppos;
    private int[] wumpuspos;
    private int[] batspos;

    WumpusLaberynth() {
        this.laberynth = new ArrayList<>();
        this.ppos = null;
        this.wumpuspos = null;
        this.batspos = null;
    }

    public ArrayList<ArrayList<Cell>> getLaberynth() {
        ArrayList<ArrayList<Cell>> list = new ArrayList<>();

        for (int i = 0; i < this.laberynth.size(); i++) {
            list.add(new ArrayList<Cell>());
            for (int j = 0; j < this.laberynth.get(i).size(); j++) {

                if (this.laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                    list.get(i).add(new NormalCell((NormalCell) this.laberynth.get(i).get(j)));

                } else if (this.laberynth.get(i).get(j).ctype == CellType.WELL) {
                    list.get(i).add(new WellCell((WellCell) this.laberynth.get(i).get(j)));

                } else if (this.laberynth.get(i).get(j).ctype == CellType.POWERUP) {
                    list.get(i).add(new PowerUpCell((PowerUpCell) this.laberynth.get(i).get(j)));
                }
            }
        }
        return list;
    }

    public void setLaberynth(ArrayList<ArrayList<Cell>> laberynth) {
        ArrayList<ArrayList<Cell>> list = new ArrayList<>();

        initBats(laberynth);

        for (int i = 0; i < laberynth.size(); i++) {
            this.laberynth.add(new ArrayList<Cell>());
            for (int j = 0; j < laberynth.get(i).size(); j++) {

                if (laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                    NormalCell ncell = new NormalCell((NormalCell) laberynth.get(i).get(j));
                    this.laberynth.get(i).add(ncell);

                    if (ncell.getInhabitant() == InhabitantType.WUMPUS) {
                        this.wumpuspos[0] = i;
                        this.wumpuspos[1] = j;
                    }

                } else if (laberynth.get(i).get(j).ctype == CellType.WELL) {
                    this.laberynth.get(i).add(new WellCell((WellCell) laberynth.get(i).get(j)));

                } else if (laberynth.get(i).get(j).ctype == CellType.POWERUP) {
                    this.laberynth.get(i).add(new PowerUpCell((PowerUpCell) laberynth.get(i).get(j)));
                }
            }
        }
        this.laberynth = laberynth;
    }

    public void createNewLaberynth() {
        Random rnd = new Random();
        int rows = rnd.nextInt(MIN_BOARD_SIZE, (MAX_BOARD_SIZE+1));
        int cols = rnd.nextInt(MIN_BOARD_SIZE, (MAX_BOARD_SIZE+1));
        int totalBoardCells = rows * cols;

        int maxWellCells = 5 * totalBoardCells / 100;
        int maxPowerCells = 10 * totalBoardCells / 100;

        int totalWellCells = rnd.nextInt(MIN_WELL_CELLS, (maxWellCells+1));
        int totalPowerCells = rnd.nextInt(MIN_POWER_CELLS, (maxPowerCells+1));


        /////////////////////////////////////////////
        int normalCells = countNormalCells(false);
        int maxBats = 10 * normalCells / 100;
        int totalBats = rnd.nextInt(MIN_BATS_ENTITIES, (maxBats+1));
    }

    public int[] getInitialCell() {

        int[] pos = null;

        if (!(laberynth.isEmpty())) {
            pos = new int[2];
            boolean placed = false;
            Random r = new Random();
            int numRnd = r.nextInt(0, countNormalCells(true)+1);

            int contador = 0;
            int i = 0;
            int j = 0;

            while (i < laberynth.size() && !placed) {
                while (j < laberynth.get(i).size() && !placed) {
                    if (laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                        NormalCell ncell = (NormalCell) laberynth.get(i).get(j);
                        if (ncell.getInhabitant() == InhabitantType.NONE) {
                            if (contador==numRnd) {
                                pos[0]=i;
                                pos[1]=j;
                                placed = true;
                            }
                            contador++;
                        }
                    }
                    j++;
                }
                i++;
            }
        }
        return pos;
    }

    /**
     * Retorna el nombre de NormalCells depenent el paràmetre
     * @param inhabited Boolean si és True retorna count caselles NORMAL deshabitades, si és False retorna count totes caselles NORMAL.
     * @return
     */
    private int countNormalCells(boolean inhabited) {
        
        int count = 0;
        
        if (inhabited) {
            for (int i = 0; i < laberynth.size(); i++) {
                for (int j = 0; j < laberynth.get(i).size(); j++) {
                    if (laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                        NormalCell ncell = (NormalCell) laberynth.get(i).get(j);
                        if (ncell.getInhabitant() == InhabitantType.NONE) {
                            count++;
                        }
                    }
                }
            }
        }
        else {
            for (int i = 0; i < laberynth.size(); i++) {
                for (int j = 0; j < laberynth.get(i).size(); j++) {

                    if (laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                        count++;
                    }
                }
            }
        }
        
        return count;
    }

    /**
     * Inicialitza l'array batspos amb el nombre de ratpenats que rep de la funció getBatsCount.
     * Recorre el laberint passat per paràmetre, per cada cel·la que contingui un ratpenat (bat) emmagatzema les dues coordenades del ratpenat dins de batspos.
     * Cada coordenada s'emmagatzemarà en una posició de l'array.
     * @param laberynth ArrayList de ArrayList de Cell - Laberint que es desitjà inicialitzar el seu array de ratpenats.
     */
    private void initBats(ArrayList<ArrayList<Cell>> laberynth) {

        int batsCount = getBatsCount(laberynth);

        this.batspos = new int[batsCount];
        int batsIndex = 0;

        for (int i = 0; i < laberynth.size(); i++) {
            for (int j = 0; j < laberynth.get(i).size(); j++) {

                if (laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                    NormalCell ncell = (NormalCell) laberynth.get(i).get(j);
                    if (ncell.getInhabitant() == InhabitantType.BAT) {
                        this.batspos[batsIndex] = i;
                        this.batspos[batsIndex+1] = j;
                        batsIndex += ENTITY;
                    }
                }
            }
        }
    }

    /**
     * Retorna el nombre de ratpenats (bat) cadascún com a ENTITY (2 posicions) dins del laberint.
     * @param laberynth ArrayList de ArrayList de Cell (laberint)
     * @return el nombre de bats (contant cada bat com a ENTITY)
     */
    private static int getBatsCount(ArrayList<ArrayList<Cell>> laberynth) {
        int batsCount = 0;

        for (int i = 0; i < laberynth.size(); i++) {
            for (int j = 0; j < laberynth.get(i).size(); j++) {

                if (laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                    NormalCell ncell = (NormalCell) laberynth.get(i).get(j);
                    if (ncell.getInhabitant() == InhabitantType.BAT) {
                        batsCount += ENTITY;
                    }
                }
            }
        }
        return batsCount;
    }
}
