package entity;

import enums.DirectionEnum;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    GamePanel gp;
    public int worldX, worldY;
    public BufferedImage currentSprite;
    public BufferedImage[] sprites;
    public int spriteCounter = 0;
    public int spriteNum = 0;
    public int maxSpriteNum = 3;
    public Rectangle solidArea = new Rectangle(0, 0, 32, 32);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public int maxLife;
    public int life;
    public DirectionEnum direction = DirectionEnum.DOWN;
    public int speed;

    public Entity(GamePanel gp) {
        this.gp = gp;
    }

    public void update(){}

    public void setAction() {}

    public void draw(Graphics2D g2){}
}
