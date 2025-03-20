package inscaparrella.model;

import inscaparrella.utils.CellType;
import inscaparrella.utils.InhabitantType;

import java.util.Objects;

public class NormalCell extends Cell{
    private InhabitantType itype;

    public NormalCell() {
        super();
        super.ctype = CellType.NORMAL;
        this.itype = InhabitantType.NONE;
    }

    public NormalCell(int x, int y) {
        super(x,y);
        super.ctype = CellType.NORMAL;
        this.itype = InhabitantType.NONE;
    }

    public NormalCell(NormalCell nc) {
        this.itype = nc.itype;
    }

    public void setInhabitant(InhabitantType inhabitantType) {
        this.itype = inhabitantType;
    }

    public InhabitantType getInhabitant() {
        return itype;
    }

    public String emitEcho() {
        String retorn = "";
        if (this.itype == InhabitantType.WUMPUS) {
            retorn += "gggrrr... gggGGGGGRRRRRrrr...";
        } else if (this.itype == InhabitantType.BAT) {
            retorn += "Flap, flap, flap";
        }
        return retorn;
    }

    public boolean isDangerous() {
        return (this.itype != InhabitantType.NONE);
    }

    @Override
    public String toString() {
        String retorn = super.toString() + " - ";

        if (this.itype == InhabitantType.WUMPUS) {
            retorn += "Normal (habitada pel Wumpus)";
        } else if (this.itype == InhabitantType.BAT) {
            retorn += "Tipus NORMAL (habitada per un ratpenat)";
        } else {
            retorn += "Tipus NORMAL";
        }
        return retorn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;      //Compara null i classe igual
        if (!super.equals(o)) return false;                             //Compara l'espai en memòria
        NormalCell ncell = (NormalCell) o;                              //Si encara son iguals comprovem l'habitant
        return itype ==  ncell.itype;                                   //Retornem true si tot es igual
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), itype);
    }
}

