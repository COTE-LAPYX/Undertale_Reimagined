package main;

import entities.Entity;

import java.util.List;


public class CollisionChecker {
    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTileEntity(Entity entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;
        try {
        switch (entity.direction) {
            case UP:
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];

                if (gp.tileManager.tileList.get(tileNum1).collision || gp.tileManager.tileList.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }

                break;
            case DOWN:
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];

                if (gp.tileManager.tileList.get(tileNum1).collision || gp.tileManager.tileList.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }

                break;
            case LEFT:
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNum[entityLeftCol][entityBottomRow];

                if (gp.tileManager.tileList.get(tileNum1).collision || gp.tileManager.tileList.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }
                break;
            case RIGHT:
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tileNum1 = gp.tileManager.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileManager.mapTileNum[entityRightCol][entityBottomRow];

                if (gp.tileManager.tileList.get(tileNum1).collision || gp.tileManager.tileList.get(tileNum2).collision) {
                    entity.collisionOn = true;
                }

                break;
        }
    } catch (ArrayIndexOutOfBoundsException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void checkHitBoxCollision(Entity entity, List<Entity> target) {

        for (int i = 0; i < target.size(); i++) {
            if (target.get(i) != null) {
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target.get(i).solidArea.x = target.get(i).worldX + target.get(i).solidArea.x;
                target.get(i).solidArea.y = target.get(i).worldY + target.get(i).solidArea.y;

                switch (entity.direction) {
                    case UP -> entity.solidArea.y -= entity.speed;
                    case DOWN -> entity.solidArea.y += entity.speed;
                    case LEFT -> entity.solidArea.x -= entity.speed;
                    case RIGHT -> entity.solidArea.x += entity.speed;
                }
                if (entity.solidArea.intersects(target.get(i).solidArea)) {
                    if (target.get(i) != entity) {
                        entity.collisionOn = true;
                    }
                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target.get(i).solidArea.x = target.get(i).solidAreaDefaultX;
                target.get(i).solidArea.y = target.get(i).solidAreaDefaultY;
            }
        }
    }
}
