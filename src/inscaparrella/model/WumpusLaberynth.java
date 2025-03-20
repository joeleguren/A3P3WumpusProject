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
}
