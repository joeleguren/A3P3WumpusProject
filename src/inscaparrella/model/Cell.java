package inscaparrella.model;

import inscaparrella.utils.CellType;

import java.util.Objects;

public abstract class Cell {
    private int row;
    private int col;
    protected CellType ctype;
    protected boolean open;

    public Cell() {
        this.row = -1;
        this.col = -1;
        this.ctype = CellType.NONE;
    }

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Cell(Cell cell) {
        this.row = cell.row;
        this.col = cell.col;
        this.ctype = cell.ctype;
    }

    public CellType getCtype() {
        return ctype;
    }

    public void setCtype(CellType ctype) {
        this.ctype = ctype;
    }

    public boolean isOpen() {
        if (open) {
            return true;
        }
        else {
            return false;
        }
    }

    public void openCell() {
        this.open = true;
    }

    public abstract String emitEcho();

    public abstract boolean isDangerous();


    @Override
    public String toString() {
        return "Cel·la [" + row + ", " + col + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col && open == cell.open && ctype == cell.ctype;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, ctype, open);
    }
}
