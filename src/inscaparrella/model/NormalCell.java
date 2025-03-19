package inscaparrella.model;

import inscaparrella.utils.InhabitantType;

public class NormalCell {
    private InhabitantType inhabitantType;

    public NormalCell() {
        this.inhabitantType = InhabitantType.NONE;
    }

    public NormalCell(int x, int y) {

    }

    public NormalCell(NormalCell nc) {
        this.inhabitantType = nc.inhabitantType;
    }

    public void setInhabitant(InhabitantType inhabitantType) {
        this.inhabitantType = inhabitantType;
    }

    public InhabitantType getInhabitant() {
        return inhabitantType;
    }

    public String emitEcho() {
        String retorn = "";
        if (this.inhabitantType == InhabitantType.WUMPUS) {
            retorn += "gggrrr... gggGGGGGRRRRRrrr...";
        } else if (this.inhabitantType == InhabitantType.BAT) {
            retorn += "Flap, flap, flap";
        }

        return retorn;
    }

    public boolean isDangerous() {
        return (this.inhabitantType != InhabitantType.NONE);
    }

    @Override
    public String toString() {
        String retorn = "L'habitant de la cel·la és: ";
        if (this.inhabitantType == InhabitantType.NONE) {
            retorn = "La cel·la és buida";
        } else if (inhabitantType == InhabitantType.BAT) {
            retorn += "un ratpenat";
        } else {
            retorn += "el Wumpus";
        }
        return retorn;
    }
}

