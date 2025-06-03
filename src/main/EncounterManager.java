package main;

import entities.encounters.Encounter;
import entities.encounters.FloweyEncounter;
import entities.utils.Hitbox;
import enums.EncounterStateEnum;
import enums.GameStateEnum;

public class EncounterManager {
    GamePanel gp;

    public EncounterManager(GamePanel gp) {
        this.gp = gp;
    }

    public void startEncounter(Encounter encounter){
        gp.hitboxes.clear();

        gp.hitboxes.add(new Hitbox(gp, gp.tileSize * 7, 0, gp.tileSize * 3, gp.tileSize * 8));
        gp.hitboxes.add(new Hitbox(gp, 0, 0, gp.tileSize * 3, gp.tileSize * 8));
        gp.hitboxes.add(new Hitbox(gp, gp.tileSize * 3, 0, gp.tileSize * 4, (int) (gp.tileSize * 4f)));
        gp.hitboxes.add(new Hitbox(gp, gp.tileSize * 3, gp.tileSize * 6, gp.tileSize * 4, gp.tileSize * 2));

        gp.gameState = GameStateEnum.ENCOUNTER;
        gp.encounterState = EncounterStateEnum.TRANSITION;
        gp.currentEncounter = new FloweyEncounter(gp);
    }
}
