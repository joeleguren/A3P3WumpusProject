package inscaparrella.model;

import inscaparrella.utils.CellType;
import inscaparrella.utils.PowerUp;

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


}



