package entities.monsters;

import entities.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Flowey extends Entity {
    GamePanel gp;

    public Flowey(GamePanel gp) {
        super(gp);

        this.gp = gp;

        getImages();

        currentSprite = sprites[0];

        worldX = gp.tileSize * 20 + gp.tileSize / 2;
        worldY = gp.tileSize * 30 + gp.tileSize / 2;

        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 64;
        solidArea.height = 64;
    }

    public void getImages() {
        try {
            sprites = new BufferedImage[2];

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/monsters/flowey/flowey_spritesheet.png"));

            int index = 0;
            for (int y = 0; y < tileSheet.getHeight() / 32; y++) {
                for (int x = 0; x < tileSheet.getWidth() / 32; x++) {
                    sprites[index] = tileSheet.getSubimage(x * 32, y * 32, 32, 32);
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            g2.drawImage(currentSprite, screenX, screenY, currentSprite.getWidth() * gp.scale, currentSprite.getHeight() * gp.scale, null);
            if (gp.keyHandler.debugMode) {
                g2.setColor(Color.blue);
                g2.drawRect(screenX + solidAreaDefaultX, screenY + solidAreaDefaultY, solidArea.width, solidArea.height);
            }
        }

    }
}
