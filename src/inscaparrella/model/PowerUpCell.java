package inscaparrella.model;

import inscaparrella.utils.CellType;
import inscaparrella.utils.PowerUp;

import java.util.Objects;
import java.util.Random;

public class PowerUpCell extends Cell{

    private PowerUp power;
    // Entre els poders random s'ha d'incloure el NONE??

    public PowerUpCell() {
        super();
        super.ctype= CellType.POWERUP;
        Random r = new Random();

        PowerUp[] types = PowerUp.values();

        int num = r.nextInt(0, types.length);

        this.power = types[num];
    }

    public PowerUpCell(int row, int col) {
        super(row, col);
        super.ctype= CellType.POWERUP;

        Random r = new Random();

        PowerUp[] types = PowerUp.values();

        int num = r.nextInt(0, types.length);

        this.power = types[num];
    }

    public PowerUpCell(PowerUpCell o) {
        super(o);
        this.power = o.power;
    }

    void createPowerUp() {
        Random r = new Random();

        PowerUp[] types = PowerUp.values();
        int num = r.nextInt(1, types.length);
        this.power = types[num];
    }

    public PowerUp consumePowerUp() {

        PowerUp power = PowerUp.NONE;

        if (super.open) {
            power = this.power;
            this.power = PowerUp.NONE;
        }

        return power;
    }

    @Override
    public String emitEcho() {
        String str = "";

        if (!(power==PowerUp.NONE)) str += "Clic, clic...";

        return str;
    }

    @Override
    public boolean isDangerous() {
        return false;
    }

    @Override
    public String toString() {
        String str = super.toString() + " - Tipus POWERUP";

            if (!(power==PowerUp.NONE)) str += " (concedeix el poder " + power.name() + ")";

        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PowerUpCell that = (PowerUpCell) o;
        return power == that.power;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), power);
    }
}



