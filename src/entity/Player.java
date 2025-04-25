package entity;

import enums.DirectionEnum;
import main.GamePanel;
import main.handlers.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity{
    public boolean noColFeature = false;
    KeyHandler keyHandler;
    public final int screenX;
    public final int screenY;
    int activityCounter = 0;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        super(gp);

        this.keyHandler = keyHandler;

        screenX = gp.screenWidth / 2 - (48);
        screenY = gp.screenHeight / 2 - (48);

        setDefaultValues();
        getPlayerImage();

        solidArea = new Rectangle();
        solidArea.x = 32;
        solidArea.y = 48;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 30;
        solidArea.height = 46;
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 14 - 16;
        worldY = gp.tileSize * 12;
        speed = 3; //4
        direction = DirectionEnum.DOWN;

//        Player Status
        maxLife = 30;
        life = maxLife;
    }

    public void getPlayerImage() {
        try {
            sprites = new BufferedImage[16];

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/player/slava_spritesheet.png"));
            //BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/player/chara_spritesheet.png"));

            int index = 0;
            for (int y = 0; y < tileSheet.getHeight() / 48; y++) {
                for (int x = 0; x < tileSheet.getWidth() / 48; x++) {
                    sprites[index] = tileSheet.getSubimage(x * 48, y * 48, 48, 48);
                    index++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {

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

            if (!gp.keyHandler.debugMode){
                gp.collisionChecker.checkTileEntity(this);
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

            spriteCounter++;

            if (spriteCounter > 12) {
                spriteNum++;
                if (spriteNum > maxSpriteNum) {
                    spriteNum = 0;
                }
                spriteCounter = 0;
            }
        } else {
            if (activityCounter > 13) {
                spriteNum = 0;
                spriteCounter = 0;
            }
            activityCounter++;
        }
    }

    @Override
    public void draw(Graphics2D g2) {

        switch (direction){
            case DOWN -> currentSprite = sprites[spriteNum];
            case LEFT -> currentSprite = sprites[4+spriteNum];
            case RIGHT -> currentSprite = sprites[8+spriteNum];
            case UP -> currentSprite = sprites[12+spriteNum];
        }

        g2.drawImage(currentSprite, screenX, screenY, currentSprite.getWidth()* gp.scale, currentSprite.getHeight()*gp.scale, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2.setColor(Color.blue);
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}
