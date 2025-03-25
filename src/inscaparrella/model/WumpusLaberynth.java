package inscaparrella.model;

import inscaparrella.utils.*;

import java.util.ArrayList;
import java.util.Random;

public class WumpusLaberynth {
    private static final int ENTITY = 2;
    private static final int COORDS = 2;
    private static final int MIN_BOARD_SIZE = 5;
    private static final int MAX_BOARD_SIZE = 15;
    private static final int MIN_WELL_CELLS = 2;
    private static final int MIN_POWER_CELLS = 2;
    private static final int MIN_BATS_ENTITIES = 2;
    private static final int TOTAL_WUMPUS = 1;
    private static final double FIVE_PERCENT = 0.05;
    private static final double TEN_PERCENT = 0.1;

    private ArrayList<ArrayList<Cell>> laberynth;
    private int[] ppos;
    private int[] wumpuspos;
    private int[] batspos;

    public WumpusLaberynth() {
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
                        this.wumpuspos = new int[ENTITY];
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
        Random r = new Random();
        int rows = r.nextInt(MIN_BOARD_SIZE, (MAX_BOARD_SIZE+1)); // Generar nombre files
        int cols = r.nextInt(MIN_BOARD_SIZE, (MAX_BOARD_SIZE+1)); // Generar nombre columnes
        int totalBoardCells = rows * cols;

        initLaberynth(rows, cols);  // Inicialitzar tauler només amb NormalCells

        int maxWellCells = (int) (totalBoardCells * FIVE_PERCENT); // Calcular màxim de WellCells
        int totalWellCells = r.nextInt(MIN_WELL_CELLS, (maxWellCells+1)); // Calcular total de WellCells que contindrà laberynth
        placeSpecialCell(CellType.WELL, totalWellCells); // Col·loca les WellCells al laberynth

        int maxPowerCells = (int) (totalBoardCells * TEN_PERCENT); // Calcular màxim de PowerUpCells
        int totalPowerCells = r.nextInt(MIN_POWER_CELLS, (maxPowerCells+1)); // Calcular total de PowerUp Cells que contindrà laberynth
        placeSpecialCell(CellType.POWERUP, totalPowerCells); // Col·loca les PowerUpCells al laberynth

        int normalCells = countNormalCells(false); // Calcular nombre de NormalCells
        int maxBats = (int) (normalCells * TEN_PERCENT); // Calcular màxim de Ratpenats
        int totalBats = r.nextInt(MIN_BATS_ENTITIES, (maxBats+1)); // Calcular total de Ratpenats que contindrà laberynth

        placeEntity(InhabitantType.BAT, totalBats); // Col·loca els ratpenats al laberynth
        placeEntity(InhabitantType.WUMPUS, TOTAL_WUMPUS); // Col·loca el Wupus al laberynth
    }


    public int[] getInitialCell() {

        if (!(laberynth.isEmpty())) {
            boolean placed = false;
            Random r = new Random();
            int numRnd = r.nextInt(0, countNormalCells(true) + 1);

            int contador = 0;
            int i = 0;
            int j = 0;

            while (i < laberynth.size() && !placed) {
                while (j < laberynth.get(i).size() && !placed) {
                    if (isSafeCell(i, j)) {
                        if (contador == numRnd) {
                            this.ppos[0] = i;
                            this.ppos[1] = j;
                            placed = true;
                            laberynth.get(i).get(j).openCell(); // Obrim la cel·la
                        }
                        contador++;
                    }
                    j++;
                }
                i++;
            }
        }
        return this.ppos;
    }

    public int[] movePlayer(MovementDirection dir) {
        int[] newCellMoved = new int[COORDS];

        if (!this.laberynth.isEmpty() && ppos != null) {
            if (isMovementValid(dir)) { // Mirem si el moviment no surt dels limits del laberint

                if (dir == MovementDirection.UP) {
                    ppos[0] -= 1;
                    this.laberynth.get(ppos[0]).get(ppos[1]).openCell();
                    newCellMoved[0] = ppos[0];
                    newCellMoved[1] = ppos[1];

                } else if (dir == MovementDirection.DOWN) {
                    ppos[0] += 1;
                    this.laberynth.get(ppos[0]).get(ppos[1]).openCell();
                    newCellMoved[0] = ppos[0];
                    newCellMoved[1] = ppos[1];

                } else if (dir == MovementDirection.LEFT) {
                    ppos[1] -= 1;
                    this.laberynth.get(ppos[0]).get(ppos[1]).openCell();
                    newCellMoved[0] = ppos[0];
                    newCellMoved[1] = ppos[1];

                } else if (dir == MovementDirection.RIGHT) {
                    ppos[1] += 1;
                    this.laberynth.get(ppos[0]).get(ppos[1]).openCell();
                    newCellMoved[0] = ppos[0];
                    newCellMoved[1] = ppos[1];
                }
            } else {
                newCellMoved = null;
            }
        } else {
            newCellMoved = null;
        }

        return newCellMoved;
    }

    public Danger getDanger() {
        Danger d = Danger.NONE;

        if (!this.laberynth.isEmpty() && ppos != null) {
            if (this.laberynth.get(ppos[0]).get(ppos[1]).getCtype() == CellType.WELL) {
                d = Danger.WELL; // Si la cel·la actual del jugador és de tipus WELL, el perill sera a l'igual de tipus WELL

            } else if (this.laberynth.get(ppos[0]).get(ppos[1]).getCtype() == CellType.NORMAL) {
                NormalCell ncell = new NormalCell((NormalCell) laberynth.get(ppos[0]).get(ppos[1])); // Si la cel·la actual del jugador es de tipus normal, haurem de revisar si te algun habitant

                if (ncell.getInhabitant() == InhabitantType.BAT)
                    d = Danger.BAT; // Si habita un ratpenat, el perill sera de tipus BAT

                else if (ncell.getInhabitant() == InhabitantType.WUMPUS)
                    d = Danger.WUMPUS; // Si habita el Wumpus, el perill sera de tipus WUMPUS
            }
        }

        return d;
    }

    public PowerUp getPowerUp() {
        PowerUp p = PowerUp.NONE;

        if (!this.laberynth.isEmpty() && ppos != null) {
            if (this.laberynth.get(ppos[0]).get(ppos[1]).getCtype() == CellType.POWERUP) {
                PowerUpCell pcell = new PowerUpCell((PowerUpCell) laberynth.get(ppos[0]).get(ppos[1]));
                p = pcell.consumePowerUp();
            }
        }

        return p;
    }

    public int[] batKidnapping() { // Mirar si esta bé, potser s'ha d'arreglar
        int[] newPos = null;

        if (!this.laberynth.isEmpty() && ppos != null && getDanger() == Danger.BAT) {
            newPos = randomCoordsLaberynth(); // Genera inicialment una possible nova posició per al jugador, = {rndRow, rndCol}
            //int[] lastPos = {ppos[0],ppos[1]};
            while (newPos[0] == ppos[0] && newPos[1] == ppos[1]) { // Mentre les posicions siguin iguals, repeteix
                newPos = randomCoordsLaberynth(); // Generem noves posicions.
                //newPos = randomCoordsLaberynthSafe();
            }
            this.ppos[0]=newPos[0];
            this.ppos[1]=newPos[1];
            // laberynth.get(ppos[0]).get(ppos[1]).openCell(); aquesta línia potser fa falta, perquè el jugador es queda a la Cel·la.
        }

        return newPos;
    }

    private boolean checkCorrectCell(int row, int col) {
        boolean isValid = false;

        if (!this.laberynth.isEmpty()) {
            isValid = (row >= 0 && row < this.laberynth.size() && col >= 0 && col < this.laberynth.get(0).size());
        }

        return isValid;
    }

    /**
     * Una cel·la és segura si està dins del {@code laberynth}, és {@code CellType.NORMAL}, i té {@code InhabitantType.NONE}
     * @param row fila
     * @param col columna
     * @return {@code True} Si la cel·la amb posició row i col és segura.
     */
    private boolean isSafeCell(int row, int col) {
        boolean isSafe = false;
        if (checkCorrectCell(row, col)) {
            if (laberynth.get(row).get(col).ctype == CellType.NORMAL) {

                NormalCell ncell = (NormalCell) laberynth.get(row).get(col);
                isSafe = ncell.getInhabitant() == InhabitantType.NONE;
            }
        }
        return isSafe;
    }

    /**
     * Comprova si un moviment en {@code this.laberynth} es vàlid, és a dir, retorna {@code false} si {@code MovementDirection dir} es surt de les dimensions del {@code this.laberynth}.
     * @param dir Direcció de un moviment, que pot ser {@code MovementDirection.UP, MovementDirection.DOWN, MovementDirection.LEFT, MovementDirection.RIGHT}
     * @return {@code true} Si el moviment dir es vàlid
     */
    private boolean isMovementValid(MovementDirection dir) {
        boolean isValid = false;

        if (dir == MovementDirection.UP && checkCorrectCell(ppos[0]-1, ppos[1])) {
            isValid = true;
        } else if (dir == MovementDirection.DOWN && checkCorrectCell(ppos[0]+1, ppos[1])) {
            isValid = true;
        } else if (dir == MovementDirection.LEFT && checkCorrectCell(ppos[0],ppos[1]-1)) {
            isValid = true;
        } else if (dir == MovementDirection.RIGHT && checkCorrectCell(ppos[0],ppos[1]+1)) {
            isValid = true;
        }

        return isValid;
    }


    /**
     * Col·loca aleatòriament al this.laberynth tantes entitats itype com totalEntities hi hagi.
     * Emmagatzema les posicions dels itype en el seu respectiu array.
     * @param itype Tipus d'entitat a col·locar.
     * @param totalEntities Nombre d'entitats que es colocaran en el tauler.
     */
    private void placeEntity(InhabitantType itype, int totalEntities) {
        int count = 0;
        while (count < totalEntities) {
            boolean added = false;
            while (!added) {
                int[] rndpos = randomCoordsLaberynth();
               // if (rndpos != null && isSafeCell(rndpos[0], rndpos[1])) No s'hauria de comprovar si la Cell està deshabitada també???
                if (rndpos != null && this.laberynth.get(rndpos[0]).get(rndpos[1]).getCtype() == CellType.NORMAL) { // No comprovem inhabited!?

                    if (itype == InhabitantType.BAT) {
                        // this.laberynth.get(rndpos[0]).get(rndpos[1]).setInhabitant(InhabitantType.BAT); Podriem ficar això directament
                        NormalCell ncell = new NormalCell(rndpos[0], rndpos[1]);
                        ncell.setInhabitant(InhabitantType.BAT);
                        this.laberynth.get(rndpos[0]).set(rndpos[1], new NormalCell(ncell));
                        added = true;

                    } else if (itype == InhabitantType.WUMPUS) {
                        // this.laberynth.get(rndpos[0]).get(rndpos[1]).setInhabitant(InhabitantType.WUMPUS); Podriem ficar això directament
                        NormalCell ncell = new NormalCell(rndpos[0], rndpos[1]);
                        ncell.setInhabitant(InhabitantType.WUMPUS);
                        this.laberynth.get(rndpos[0]).set(rndpos[1], new NormalCell(ncell));
                        // this.wumpuspos = new int[ENTITY*TOTAL_WUMPUS]; Per si vulguessim col·locar mes Wumpus
                        this.wumpuspos = new int[ENTITY];
                        this.wumpuspos[0] = rndpos[0];
                        this.wumpuspos[1] = rndpos[1];
                        added = true;
                    }
                }
                if (added) count++;
            }
        }
        if (itype == InhabitantType.BAT) initBats(this.laberynth);
    }

    /**
     * Col·loca aleatòriament en el this.laberynth la quantitat de tipus de cel·les especificades en els paràmetres.
     * Les cel·les especials només es col·locaran sobre cel·les de tipus NormalCell.
     * @param cellType Tipus de cel·la a col·locar en el tauler.
     * @param totalTypeCell Quantitat de cel·les que es col·locaran en el tauler.
     */
    private void placeSpecialCell(CellType cellType, int totalTypeCell) {
        if (!this.laberynth.isEmpty()) {
            int count = 0;
            while (count < totalTypeCell) {
                boolean added = false;
                while (!added) {
                    int[] rndpos = randomCoordsLaberynth();
                    if (this.laberynth.get(rndpos[0]).get(rndpos[1]).getCtype() == CellType.NORMAL) {

                        if (cellType == CellType.WELL) {
                            WellCell wcell = new WellCell(rndpos[0],rndpos[1]);
                            this.laberynth.get(rndpos[0]).set(rndpos[1], new WellCell(wcell));
                            added = true;

                        } else if (cellType == CellType.POWERUP) {
                            PowerUpCell pcell = new PowerUpCell(rndpos[0], rndpos[1]);
                            this.laberynth.get(rndpos[0]).set(rndpos[1], new PowerUpCell(pcell));
                            added = true;
                        }
                    }
                    count++;
                }
            }
        }
    }

    /**
     * Genera unes cordenades random dins del {@code this.laberynth}
     * @return {@code int[COORDS]} Retorna dos coordenades
     */
    private int[] randomCoordsLaberynth() {

        int[] randomCoords = null;

        if (!this.laberynth.isEmpty()) {
            Random r = new Random();
            randomCoords = new int[COORDS];
            int nrows = this.laberynth.size();
            int ncols = this.laberynth.get(0).size();
            randomCoords[0] = r.nextInt(0, nrows); //row
            randomCoords[1] = r.nextInt(0, ncols); //col
        }

        return randomCoords;
    }

    /**
     * Inicialitza la matriu laberynth de la Classe amb new NormalCells(parametritzades) deshabitades.
     * @param cols Nombre total de columnes
     * @param rows Nombre total de files
     */
    private void initLaberynth(int rows, int cols) {
        for (int i = 0; i < rows; i++) {
            this.laberynth.add(new ArrayList<Cell>());
            for (int j = 0; j < cols; j++) {
                this.laberynth.get(i).add(new NormalCell(i, j));
            }
        }
    }

    /**
     * Retorna el nombre de NormalCells depenent el paràmetre.
     * @param inhabited Boolean que si és True retorna count caselles NORMAL deshabitades, si és False retorna count totes caselles NORMAL.
     * @return
     */
    private int countNormalCells(boolean inhabited) {
        
        int count = 0;

        for (int i = 0; i < laberynth.size(); i++) {
            for (int j = 0; j < laberynth.get(i).size(); j++) {
                if (laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                    if (inhabited) {
                        NormalCell ncell = (NormalCell) laberynth.get(i).get(j);
                        if (ncell.getInhabitant() == InhabitantType.NONE) {
                            count++;
                        }
                    }
                    else {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Inicialitza l'array atribut de classe batspos amb el nombre de ratpenats que rep de la funció getBatsCount.
     * Recorre el laberint passat per paràmetre, per cada cel·la que contingui un ratpenat (bat) emmagatzema les dues coordenades del ratpenat dins de batspos.
     * Cada coordenada s'emmagatzemarà en una posició de l'array.
     * @param laberynth ArrayList de ArrayList de Cell - Laberint que es desitjà inicialitzar el seu array de ratpenats.
     */
    private void initBats(ArrayList<ArrayList<Cell>> laberynth) {

        int batsCount = getBatsCount(laberynth); // Obtenim nombre bats com a e ENTITY

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
