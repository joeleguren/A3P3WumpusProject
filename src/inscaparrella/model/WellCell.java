package inscaparrella.model;
import inscaparrella.utils.CellType;

public class WellCell extends Cell {

    public WellCell() {
        super();
        super.ctype = CellType.WELL;
    }

    public WellCell(int row, int col) { // Canviem nom dels atributs per a no liar-nos (a row i a col), abans "x" i "y"
        super(row, col);
        super.ctype = CellType.WELL;
    }

    public WellCell(WellCell wc) {
        super(wc);
    }

    @Override
    public String emitEcho() {
        return "\tFFFFfffff...";
    }

    @Override
    public boolean isDangerous() {
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + " - Tipus " + this.ctype;
    }
}

