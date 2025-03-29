package inscaparrella.model;

import inscaparrella.utils.PowerUp;

public class Player {

    private int row;
    private int col;
    private int powers[];

    public Player() {
        this.row = -1;
        this.col = -1;
        this.powers = new int[]{2, 0};
    }

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
        this.powers = new int[]{2, 0};
    }

    public void setStartingCell(int row, int col) {
        if (this.row == -1 && this.col == -1) {
            this.row = row;
            this.col = col;
        }
    }

    public void move (int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getPowerUpQuantity (PowerUp pUp) {
        int total = 0;
        if (pUp == PowerUp.ARROW) {
            total = powers[0];
        } else if (pUp == PowerUp.JUMPER_BOOTS) {
            total = powers[1];
        }
        return total;
    }

    public boolean usePower(PowerUp pUp) {
        boolean atac = false;
        if (pUp == PowerUp.ARROW) {
            if (powers[0]>0) {
                atac = true;
                powers[0]--;
            }
        } else if (pUp == PowerUp.JUMPER_BOOTS) {
            if (powers[1]>0) {
                atac = true;
                powers[1]--;
            }
        }
        return atac;
    }

    public void addPower (PowerUp pUp) {
        if (pUp == PowerUp.ARROW) {
            powers[0]++;
        } else if (pUp == PowerUp.JUMPER_BOOTS) {
            powers[1]++;
        }
    }

    @Override
    public String toString() {
        String retorn = "Jugador situat a (" + this.row + " " + this.col + ")";
        retorn += "\nPOWERS: \n\tN_ARROWS: " + this.powers[0] + "\n\tN_JUMPER_BOOTS: " + this.powers[1];
        return retorn;
    }
}