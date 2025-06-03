package entities.encounters;

import entities.Entity;
import enums.DirectionEnum;
import enums.EncounterStateEnum;
import main.GamePanel;
import main.handlers.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class PlayerSoul extends Entity {
    KeyHandler keyHandler;
    private GamePanel gp;

    public PlayerSoul(GamePanel gp, KeyHandler keyHandler) {
        super(gp);

        this.gp = gp;
        this.keyHandler = keyHandler;

        solidArea = new Rectangle();
        solidArea.x = 0;
        solidArea.y = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 15;
        solidArea.height = 15;

        speed = 3;

        getSoulImages();

        currentSprite = sprites[4];

        worldX = gp.screenWidth / 2 - currentSprite.getWidth() / 2;
        worldY = gp.screenHeight / 2 + gp.tileSize;
    }

    public void getSoulImages() {
        try {
            sprites = new BufferedImage[13];

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/player/souls_spritesheet.png"));

            int index = 0;
            for (int y = 0; y < tileSheet.getHeight() / 16; y++) {
                for (int x = 0; x < tileSheet.getWidth() / 16; x++) {
                    sprites[index] = tileSheet.getSubimage(x * 16, y * 16, 16, 16);
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        {
            if (gp.encounterState != EncounterStateEnum.TURN_ENEMY) return;

            if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {
                if (keyHandler.upPressed) {
                    direction = DirectionEnum.UP;
                } else if (keyHandler.downPressed) {
                    direction = DirectionEnum.DOWN;
                } else if (keyHandler.leftPressed) {
                    direction = DirectionEnum.LEFT;
                } else if (keyHandler.rightPressed) {
                    direction = DirectionEnum.RIGHT;
                }

                if (!gp.keyHandler.debugMode) {
                    gp.collisionChecker.checkHitBoxCollision(this, gp.hitboxes);
                }

                if (!collisionOn) {
                    switch (direction) {
                        case UP -> worldY -= speed;
                        case DOWN -> worldY += speed;
                        case LEFT -> worldX -= speed;
                        case RIGHT -> worldX += speed;
                    }
                }

                collisionOn = false;
            }

            if (keyHandler.specialPressed) {
                if (gp.player.patience > 0) {
                    gp.player.patience--;
                }
            } else if (gp.player.patience < gp.player.maxPatience) {
                gp.player.patience++;
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (gp.encounterState != EncounterStateEnum.TURN_ENEMY) return;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        g2.drawImage(currentSprite, worldX, worldY, currentSprite.getWidth(), currentSprite.getHeight(), null);
        //g2.setColor(Color.blue);
        //g2.drawRect(worldX + solidArea.x, worldY + solidArea.y, solidArea.width, solidArea.height);
    }
}
