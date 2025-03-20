package inscaparrella.model;
import inscaparrella.utils.CellType;

public class WellCell extends Cell {

    public WellCell() {
        super();
        super.ctype = CellType.WELL;
    }

    public WellCell(int x, int y) {
        super(x, y);
        super.ctype = CellType.WELL;
    }

    public WellCell(WellCell wc) {
        super(wc);
    }

    @Override
    public String emitEcho() {
        return "FFFFfffff...";
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
