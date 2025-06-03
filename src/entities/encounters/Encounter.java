package entities.encounters;

import entities.Entity;
import main.GamePanel;

public class Encounter extends Entity {
    GamePanel gp;

    public Encounter(GamePanel gp) {
        super(gp);

        this.gp = gp;
    }
}
