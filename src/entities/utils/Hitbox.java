package entities.utils;

import entities.Entity;
import main.GamePanel;

import java.awt.*;

public class Hitbox extends Entity {
    GamePanel gp;

    public Hitbox(GamePanel gp, int x, int y, int w, int h) {
        super(gp);
        this.gp = gp;

        worldX = x;
        worldY = y;

        solidArea = new Rectangle(0, 0, w, h);
    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.CYAN);
        g2.fillRect(worldX, worldY, solidArea.width, solidArea.height);
    }
}
