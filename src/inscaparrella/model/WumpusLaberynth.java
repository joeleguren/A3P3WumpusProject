package inscaparrella.model;

import inscaparrella.utils.CellType;

import java.util.ArrayList;

public class WumpusLaberynth {
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
        this.laberynth = laberynth;
    }
}
