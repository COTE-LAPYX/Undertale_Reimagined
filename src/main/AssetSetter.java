package main;

import entities.monsters.Flowey;

public class AssetSetter {

    GamePanel gp;

    public AssetSetter(GamePanel gp) {
        this.gp = gp;
    }

    public void setEntity() {
        gp.entityList.clear();

        switch (gp.currentMapName) {
            case "tutorialroom" -> {
                gp.entityList.add(new Flowey(gp));
            }
        }
    }

    public void loadEntity(){
        setEntity();
    }
}
