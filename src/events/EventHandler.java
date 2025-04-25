package events;

import enums.DirectionEnum;
import enums.GameStateEnum;
import main.GamePanel;

import java.util.Objects;

public class EventHandler {
    GamePanel gp;
    EventRect[][] eventRect;

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;


    public EventHandler(GamePanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxWorldCol][gp.maxWorldRow];

        int col = 0;
        int row = 0;
        while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
            eventRect[col][row] = new EventRect();
            eventRect[col][row].x = 0;
            eventRect[col][row].y = 0;
            eventRect[col][row].width = gp.tileSize;
            eventRect[col][row].height = gp.tileSize;
            eventRect[col][row].eventRectDefaultX = eventRect[col][row].x;
            eventRect[col][row].eventRectDefaultY = eventRect[col][row].y;

            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
            }
        }

    }


    public void checkEvent() {

        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if (distance > gp.tileSize) {
            canTouchEvent = true;
        }


        if (canTouchEvent) {
            if (Objects.equals(gp.currentMapName, "startroom")) {
                if (hit(28, 12)) {
                    teleportEvent("tutorialroom", DirectionEnum.UP, gp.tileSize * 20 + 16, gp.tileSize * 35);
                }
                if (hit(29, 12)) {
                    teleportEvent("tutorialroom", DirectionEnum.UP, gp.tileSize * 20 + 16, gp.tileSize * 35);
                }
            } else if (Objects.equals(gp.currentMapName, "tutorialroom")) {
                if (hit(18, 37)) {
                    teleportEvent("startroom", DirectionEnum.DOWN, gp.tileSize * 28 + 16, gp.tileSize * 13);
                }
                if (hit(19, 37)) {
                    teleportEvent("startroom", DirectionEnum.DOWN, gp.tileSize * 28 + 16, gp.tileSize * 13);
                }
                if (hit(20, 37)) {
                    teleportEvent("startroom", DirectionEnum.DOWN, gp.tileSize * 28 + 16, gp.tileSize * 13);
                }
                if (hit(21, 37)) {
                    teleportEvent("startroom", DirectionEnum.DOWN, gp.tileSize * 28 + 16, gp.tileSize * 13);
                }
            }
        }


    }

    public boolean hit(int col, int row, DirectionEnum reqDirection) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            if (gp.player.direction == reqDirection || reqDirection == DirectionEnum.DOWN) {
                hit = true;

                previousEventX = gp.player.worldX;
                previousEventY = gp.player.worldY;
            }
        }
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public boolean hit(int col, int row) {
        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect[col][row].x = col * gp.tileSize + eventRect[col][row].x;
        eventRect[col][row].y = row * gp.tileSize + eventRect[col][row].y;

        if (gp.player.solidArea.intersects(eventRect[col][row]) && !eventRect[col][row].eventDone) {
            hit = true;

            previousEventX = gp.player.worldX;
            previousEventY = gp.player.worldY;
        }
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect[col][row].x = eventRect[col][row].eventRectDefaultX;
        eventRect[col][row].y = eventRect[col][row].eventRectDefaultY;

        return hit;
    }

    public void teleportEvent(String mapName, DirectionEnum direction, int x, int y) {
        gp.loadMapAndEntity(mapName);
        gp.player.direction = direction;
        gp.player.worldX = x;
        gp.player.worldY = y;
    }
}
