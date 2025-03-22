package inscaparrella.model;

import inscaparrella.utils.CellType;
import inscaparrella.utils.InhabitantType;

import java.util.ArrayList;

public class WumpusLaberynth {
    private static final int ENTITY = 2;
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
