package inscaparrella.model;

public class Player {

    int row;
    int col;
    int powers[];

    public Player() {
        this.row = -1;
        this.col = -1;
        this.powers = new int[2];
    }

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
        this.powers = new int[]{2, 0};
    }

    public void setStartingCell(int row, int col) {
        
    }




}
