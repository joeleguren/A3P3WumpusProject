package inscaparrella.model;

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
            for (int j = 0; j < this.laberynth.get(i).size(); j++) {
                //list.get(j).add(new Cell(this.laberynth.get(j)));
            }
        }

        return laberynth;
    }

    public void setLaberynth(ArrayList<ArrayList<Cell>> laberynth) {
        this.laberynth = laberynth;
    }
}
