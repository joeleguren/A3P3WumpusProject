package inscaparrella.model;

import inscaparrella.utils.*;

import java.util.ArrayList;
import java.util.Random;

public class WumpusLaberynth {
    private static final int ENTITY = 2;
    private static final int COORDS = 2;
    private static final int POS = 1;
    private static final int MIN_BOARD_SIZE = 5;
    private static final int MAX_BOARD_SIZE = 15;
    private static final int MIN_WELL_CELLS = 2;
    private static final int MIN_POWER_CELLS = 2;
    private static final int MIN_BATS_ENTITIES = 2;
    private static final int TOTAL_WUMPUS = 1;
    private static final double FIVE_PERCENT = 0.05;
    private static final double TEN_PERCENT = 0.1;

    private static final String CLOSED_CELL_SYMBOL = ConsoleColors.BLUE_BOLD + "#" + ConsoleColors.RESET;
    private static final String OPENED_CELL = " ";
    private static final String WELL_CELL_SYMBOL = ConsoleColors.PURPLE_BOLD + "O" + ConsoleColors.RESET;
    private static final String POWERUP_CELL_SYMBOL = ConsoleColors.GREEN_BOLD + "&" + ConsoleColors.RESET;
    private static final String BAT_SYMBOL = "*";
    private static final String WUMPUS_SYMBOL = ConsoleColors.RED_BOLD + "W" + ConsoleColors.RESET;
    private static final String PLAYER_SYMBOL = ConsoleColors.RED_BOLD_BRIGHT + "P" + ConsoleColors.RESET;

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
                        this.wumpuspos = new int[WumpusLaberynth.ENTITY];
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
        int rows = r.nextInt(WumpusLaberynth.MIN_BOARD_SIZE, (WumpusLaberynth.MAX_BOARD_SIZE+1)); // Generar nombre files
        int cols = r.nextInt(WumpusLaberynth.MIN_BOARD_SIZE, (WumpusLaberynth.MAX_BOARD_SIZE+1)); // Generar nombre columnes

        int totalBoardCells = rows * cols;
//        System.out.println("Rows " + rows);
//        System.out.println("Cols " + cols);
//        System.out.println("Total " + totalBoardCells);
        initLaberynth(rows, cols);  // Inicialitzar tauler només amb NormalCells

        int maxWellCells = (int) (totalBoardCells * WumpusLaberynth.FIVE_PERCENT); // Calcular màxim de WellCells
        int totalWellCells;
        if (maxWellCells>WumpusLaberynth.MIN_WELL_CELLS) {
            totalWellCells = r.nextInt(WumpusLaberynth.MIN_WELL_CELLS, (maxWellCells+1)); // Calcular total de WellCells que contindrà laberynth
        }
        else totalWellCells = WumpusLaberynth.MIN_WELL_CELLS;

        placeSpecialCell(CellType.WELL, totalWellCells); // Col·loca les WellCells al laberynth

        int totalPowerCells;
        int maxPowerCells = (int) (totalBoardCells * WumpusLaberynth.TEN_PERCENT); // Calcular màxim de PowerUpCells
        if (maxPowerCells>WumpusLaberynth.MIN_POWER_CELLS) {
            totalPowerCells = r.nextInt(WumpusLaberynth.MIN_POWER_CELLS, (maxPowerCells+1)); // Calcular total de PowerUp Cells que contindrà laberynth
        }
        else totalPowerCells = WumpusLaberynth.MIN_POWER_CELLS;

        placeSpecialCell(CellType.POWERUP, totalPowerCells); // Col·loca les PowerUpCells al laberynth

        int normalCells = countNormalCells(false); // Calcular nombre de NormalCells
        int maxBats = (int) (normalCells * WumpusLaberynth.TEN_PERCENT); // Calcular màxim de Ratpenats
        int totalBats;
        if (maxBats>WumpusLaberynth.MIN_BATS_ENTITIES) {
            totalBats = r.nextInt(WumpusLaberynth.MIN_BATS_ENTITIES, (maxBats+1)); // Calcular total de Ratpenats que contindrà laberynth
        } else totalBats = WumpusLaberynth.MIN_BATS_ENTITIES;

        placeEntity(InhabitantType.BAT, totalBats); // Col·loca els ratpenats al laberynth
        placeEntity(InhabitantType.WUMPUS, WumpusLaberynth.TOTAL_WUMPUS); // Col·loca el Wupus al laberynth
    }


    public int[] getInitialCell() { // Ho podriem arreglar per a vagi generant nombres randoms fins que trobi una posició apta per al jugador.

        int[] playerPos = null;

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
                            int row = i;
                            int col = j;
                            this.ppos = new int[]{row, col};
                            playerPos = new int[]{this.ppos[0], this.ppos[1]};
                            placed = true;
                            laberynth.get(i).get(j).openCell(); // Obrim la cel·la
                        }
                        contador++;
                    }
                    j++;
                }
                j=0; // Reiniciem valor
                i++;
            }
        }

        return playerPos;
    }

    public int[] movePlayer(MovementDirection dir) {
        int[] newCellMoved = new int[WumpusLaberynth.COORDS];

        if (isLaberynthAndPlayerReady()) {
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

        if (isLaberynthAndPlayerReady()) {
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

        if (isLaberynthAndPlayerReady()) {
            if (this.laberynth.get(ppos[0]).get(ppos[1]).getCtype() == CellType.POWERUP) {
                PowerUpCell pcell = (PowerUpCell) laberynth.get(ppos[0]).get(ppos[1]); // Copiem referència
                p = pcell.consumePowerUp(); // Consumim poder de la referència.
            }
        }

        return p;
    }

    public int[] batKidnapping() { // Mirar si esta bé, potser s'ha d'arreglar
        int[] newPos = null;

        if (isLaberynthAndPlayerReady() && getDanger() == Danger.BAT) {
            newPos = randomCoordsLaberynth(); // Genera inicialment una possible nova posició per al jugador, = {rndRow, rndCol}

            while (newPos[0] == ppos[0] && newPos[1] == ppos[1]) { // Mentre les posicions rnd siguin iguals a les actuals del ppos, repeteix
                newPos = randomCoordsLaberynth(); // Generem noves posicions.
                //newPos = randomCoordsLaberynthSafe(); Aqui ho hem de preguntar
            }
            this.ppos[0]=newPos[0];
            this.ppos[1]=newPos[1];
            this.laberynth.get(ppos[0]).get(ppos[1]).openCell();
        }

        return newPos;
    }

    public boolean shootArrow(ShootDirection dir) {

        boolean successfulShot = false;

        if (isPlayerInitialized()) {
            int prow = ppos[0];
            int pcol = ppos[1];
            int wrow = wumpuspos[0];
            int wcol = wumpuspos[1];
            
            if (dir == ShootDirection.UP) {
                successfulShot = checkCorrectCell(prow-WumpusLaberynth.POS, pcol) && prow-1 == wrow && pcol == wcol;

            } else if (dir == ShootDirection.DOWN) {
                successfulShot = checkCorrectCell(prow+WumpusLaberynth.POS, pcol) && prow+1 == wrow && pcol == wcol;

            } else if (dir == ShootDirection.LEFT) {
                successfulShot = checkCorrectCell(prow, pcol-WumpusLaberynth.POS) && prow == wrow && pcol-WumpusLaberynth.POS == wcol;

            } else if (dir == ShootDirection.RIGHT) {
                successfulShot = checkCorrectCell(prow, pcol+WumpusLaberynth.POS) && prow == wrow && pcol+WumpusLaberynth.POS == wcol;
            }

        }
        return successfulShot;
    }

    public boolean startleWumpus() {

        boolean scared = false;

        if (isLaberynthAndPlayerReady()) {
            Random r = new Random();

            int numRandom = r.nextInt(100); // En comptes de un nombre, faci boolean
           // int numRandom = 2;

            if (numRandom%2 == 0) {
                int[] rndPos = randomCoordsLaberynth();
                while (!isSafeCell(rndPos[0], rndPos[1]) || (rndPos[0] == ppos[0] && rndPos[1] == ppos[1])) {
                    rndPos = randomCoordsLaberynth();
                }
                // Intercanviar el wumpus de la posicio actual, wumpuspos
                NormalCell wumpusNewCell = (NormalCell) this.laberynth.get(rndPos[0]).get(rndPos[1]); // Copiem referència com a NormalCell de la nova posició Wumpus
                wumpusNewCell.setInhabitant(InhabitantType.WUMPUS); // Afegim inhabitant wumpus

                NormalCell wumpusOldCell = (NormalCell) this.laberynth.get(wumpuspos[0]).get(wumpuspos[1]); // Copiem referència com a NormalCell on trobava el Wumpus
                wumpusOldCell.setInhabitant(InhabitantType.NONE); // Eliminem inhabitant on anteriorment estava el Wumpus

                // Actualitzem l'array wumpuspos amb la nova posició
                wumpuspos[0] = rndPos[0];
                wumpuspos[1] = rndPos[1];

                scared = true;
            }
        }

        return scared;
    }

    public void moveBats() {
        if (isLaberynthAndPlayerReady()) {

            for (int batIndex = 0; batIndex < batspos.length; batIndex += WumpusLaberynth.ENTITY) {
                int[] rndPos = randomCoordsLaberynth();
                while (!isSafeCell(rndPos[0], rndPos[1]) || (rndPos[0] == ppos[0] && rndPos[1] == ppos[1])) {
                    rndPos = randomCoordsLaberynth();
                }

                NormalCell batNewCell = (NormalCell) this.laberynth.get(rndPos[0]).get(rndPos[1]); // Copiem referència com a NormalCell de la nova posició del bat
                batNewCell.setInhabitant(InhabitantType.BAT); // Afegim inhabitant Bat

                NormalCell batOldCell = (NormalCell) this.laberynth.get(batspos[batIndex]).get(batspos[batIndex+WumpusLaberynth.POS]); // Copiem referència com a NormalCell on trobava del bat
                batOldCell.setInhabitant(InhabitantType.NONE); // Eliminem inhabitant on anteriorment estava el bat

                // Actualitzem l'array batspos amb la nova posició
                batspos[batIndex] = rndPos[0];
                batspos[batIndex+WumpusLaberynth.POS] = rndPos[1];

            }
        }
    }

    public String emitEchoes() {
        String str = "";

        if (isLaberynthAndPlayerReady()) {
            str += returnEcho(ppos[0]-WumpusLaberynth.POS, ppos[1]); // A DALT
            str += returnEcho(ppos[0]+WumpusLaberynth.POS, ppos[1]); // A BAIX
            str += returnEcho(ppos[0], ppos[1]-WumpusLaberynth.POS); // ESQUERRA
            str += returnEcho(ppos[0], ppos[1]+WumpusLaberynth.POS);  // DRETA
            str += returnEcho(ppos[0]-WumpusLaberynth.POS, ppos[1]-WumpusLaberynth.POS); // A DALT-ESQUERRA
            str += returnEcho(ppos[0]-WumpusLaberynth.POS, ppos[1]+WumpusLaberynth.POS); // A DALT-DRETA
            str += returnEcho(ppos[0]+WumpusLaberynth.POS, ppos[1]-WumpusLaberynth.POS); // BAIX-ESQUERRA
            str += returnEcho(ppos[0]+WumpusLaberynth.POS, ppos[1]+WumpusLaberynth.POS); // BAIX-DRETA
        }
        // dalt = row-1 col
        // baix = row+1 col
        // esquerra = row col-1
        // dreta = row col+1
        // dalt-esquerra = row-1 col-1
        // dalt-dreta = row-1 col+1
        // baix-esquerra = row+1 col-1
        // baix-dreta = row+1 col+1
        return str;
    }

    public String currentCellMessage() {
        String str = "";
        if (isLaberynthAndPlayerReady()) {
            str += this.laberynth.get(ppos[0]).get(ppos[1]);
        }
        return str;
    }

    @Override
    public String toString() {
        String str = "";

        // En el següent mètode, podrem triar veure-ho en mode debugar (és a dir, que veurem tots els elements).
        // Per a poder utilitzar el mètode debugant, descomentar el codi entre "// Inici mode debugar" i "// Fi mode debugar".
        // Hi ha també dues linies i un else-if que s'han de comentar en cas de debugar.


        // Si es una cela tancada #
        // Si es una cela normal habitada amb bat *
        // Si es una cela normal habitada amb wumpus W
        // Si es la cela que es troba el jugador P
        // Si es la cela oberta, fiquem espai en blanc
        // Si es una cella well, fiquem O

        str +=  "  ";
        for (int i = 0; i < getLaberynth().get(0).size(); i++) {
            if (i <= 9) str += " 0" + String.valueOf(i);
            else str += " " + String.valueOf(i);
        }

        str += "\n";

        for (int i = 0; i < laberynth.size(); i++) {

            if (i<= 9) str += "0" + String.valueOf(i) + "  ";
            else str += String.valueOf(i) + "  ";

            for (int j = 0; j < laberynth.get(i).size(); j++) {

                if (laberynth.get(i).get(j).open) {
                    if (isPlayerInitialized() && ppos[0] == i && ppos[1] ==j) {
                        if (laberynth.get(i).get(j).getCtype() == CellType.NORMAL) {
                            NormalCell ncell = (NormalCell) laberynth.get(i).get(j);
                            InhabitantType itype = ncell.getInhabitant();
                            if (itype == InhabitantType.WUMPUS) {
                                str += WumpusLaberynth.WUMPUS_SYMBOL + "  ";
                            } else {
                                str += WumpusLaberynth.PLAYER_SYMBOL + "  ";
                            }
                        } else if(laberynth.get(i).get(j).getCtype() == CellType.WELL) {
                            str += WumpusLaberynth.WELL_CELL_SYMBOL + "  ";
                        } else {
                            str += WumpusLaberynth.PLAYER_SYMBOL + "  ";
                        }

                    } else if (laberynth.get(i).get(j).getCtype() == CellType.WELL) {
                        str += WumpusLaberynth.WELL_CELL_SYMBOL + "  ";
                    }
                    // En cas de mode debugar, comentar aquest else, sino descomentar
                    else  {
                        str += WumpusLaberynth.OPENED_CELL + "  ";
                    }


                    // Inici mode debugar
//                    else if (laberynth.get(i).get(j).getCtype() == CellType.NORMAL) {  // Si esta oberta i no esta el jugador a sobre
//
//                        NormalCell ncell = (NormalCell) laberynth.get(i).get(j);
//
//                        if (ncell.getInhabitant() == InhabitantType.BAT) {
//                            str += WumpusLaberynth.BAT_SYMBOL + "  ";
//
//                        } else if (ncell.getInhabitant() == InhabitantType.WUMPUS) {
//                            str += WumpusLaberynth.WUMPUS_SYMBOL + "  ";
//                        }
//                        else {
//                            str += WumpusLaberynth.OPENED_CELL + "  ";
//                        }
//                    } else { // Es una powerup
//                        //str += WumpusLaberynth.OPENED_CELL + "  "; // DESCOMENTAR, ERA UNA PROVA
//                        str += WumpusLaberynth.POWERUP_CELL_SYMBOL + "  "; // COMENTAR ERA UNA PROVA
//                    }
                    // Fi mode debugar
                } else {
                    // En cas de mode debugar, comentar aquesta linea, sino descomentar
                    str += WumpusLaberynth.CLOSED_CELL_SYMBOL + "  ";

                    // Inici mode debugar
//                    if (laberynth.get(i).get(j).getCtype() == CellType.NORMAL) {
//                        NormalCell ncell = (NormalCell) laberynth.get(i).get(j); // Copiem referència per a consultar
//
//                        if (ncell.getInhabitant() == InhabitantType.BAT) {
//                            str += WumpusLaberynth.BAT_SYMBOL + "  ";
//                        } else if (ncell.getInhabitant() == InhabitantType.WUMPUS) {
//                            str += WumpusLaberynth.WUMPUS_SYMBOL + "  ";
//                        } else {
//                            str += WumpusLaberynth.CLOSED_CELL_SYMBOL + "  ";
//                        }
//
//                    } else if(laberynth.get(i).get(j).getCtype() == CellType.WELL) {
//                        str += WumpusLaberynth.WELL_CELL_SYMBOL + "  ";
//                    } else if (laberynth.get(i).get(j).getCtype() == CellType.POWERUP) {
//                        str += WumpusLaberynth.POWERUP_CELL_SYMBOL + "  ";
//                    }
                    // Fi mode debugar
                }

            }
            str += "\n";
        }
        return str;
    }

    private boolean checkCorrectCell(int row, int col) {
        boolean isValid = false;

        if (!this.laberynth.isEmpty()) {
            isValid = (row >= 0 && row < this.laberynth.size() && col >= 0 && col < this.laberynth.get(0).size());
        }

        return isValid;
    }

    /**
     * Comprova si les coordenades {@code row} i {@code col} són vàlides i es troben dins dels límits del laberint.
     * @param row fila
     * @param col columna
     * @return {@code String} Si la cel·la conté un echo, retorna una String amb el soroll del echo, en cas contrari retorna una String buida.
     */
    private String returnEcho(int row, int col) {
        String echo = "";
        if (checkCorrectCell(row, col)) {
            echo = this.laberynth.get(row).get(col).emitEcho();
            if (!echo.isEmpty()) echo += "\n";
        }
        return echo;
    }

    /**
     * Comprova si el laberynth no esta buit i el jugador està inicialitzat
     * @return {@code True} Si el laberynth no esta buit i el jugador està inicialitzat
     */
    private boolean isLaberynthAndPlayerReady() {
        return !this.laberynth.isEmpty() && isPlayerInitialized();
    }

    /**
     * Comprova si la posició del jugador esta inicialitzada
     * @return {@code True} si la posició actual del jugador esta inicialitzada
     */
    private boolean isPlayerInitialized() {
        return this.ppos != null;
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
                if (rndpos != null && isSafeCell(rndpos[0], rndpos[1])) { // Comprovem que la posició no sigui null, i que sigui segura

                    NormalCell ncell = (NormalCell) this.laberynth.get(rndpos[0]).get(rndpos[1]);
                    if (itype == InhabitantType.BAT) {
                        ncell.setInhabitant(InhabitantType.BAT);
                        added = true;

                    } else if (itype == InhabitantType.WUMPUS) {
                        ncell.setInhabitant(InhabitantType.WUMPUS);
                        // this.wumpuspos = new int[ENTITY*TOTAL_WUMPUS]; Per si vulguessim col·locar mes Wumpus
                        this.wumpuspos = new int[WumpusLaberynth.ENTITY];
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
            randomCoords = new int[WumpusLaberynth.COORDS];
            int nrows = this.laberynth.size();
            int ncols = this.laberynth.get(0).size();
            randomCoords[0] = r.nextInt(0, nrows); //row
            randomCoords[1] = r.nextInt(0, ncols); //col
        }

        return randomCoords;
    }

    /**
     * Inicialitza la matriu laberynth de la Classe amb new NormalCells(parametritzades) deshabitades.
     * @param ncols Nombre total de columnes
     * @param nrows Nombre total de files
     */
    private void initLaberynth(int nrows, int ncols) {
        for (int i = 0; i < nrows; i++) {
            this.laberynth.add(new ArrayList<Cell>());
            for (int j = 0; j < ncols; j++) {
                this.laberynth.get(i).add(new NormalCell(i, j));
            }
        }
    }

    /**
     * Retorna el nombre de NormalCells depenent el paràmetre.
     * @param uninhabited Boolean que si és True retorna count caselles NORMAL deshabitades, si és False retorna count totes caselles NORMAL.
     * @return
     */
    private int countNormalCells(boolean uninhabited) {
        
        int count = 0;

        for (int i = 0; i < laberynth.size(); i++) {
            for (int j = 0; j < laberynth.get(i).size(); j++) {
                if (uninhabited) {
                    if (isSafeCell(i, j)) {
                        count++;
                    }
                } else if (laberynth.get(i).get(j).ctype == CellType.NORMAL) {
                    count++;
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
                        batsIndex += WumpusLaberynth.ENTITY;
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
                        batsCount += WumpusLaberynth.ENTITY;
                    }
                }
            }
        }
        return batsCount;
    }
}
