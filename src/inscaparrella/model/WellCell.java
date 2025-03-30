package inscaparrella.model;
import inscaparrella.utils.CellType;
import inscaparrella.utils.ConsoleColors;

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
        return ConsoleColors.PURPLE_BOLD +  "\tFFFFfffff..." + ConsoleColors.RESET;
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

