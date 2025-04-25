package main;

import java.awt.*;

public class UI {
    GamePanel gp;

    public UI(GamePanel gp) {
        this.gp = gp;
    }

    public void draw(Graphics2D g2) {
        switch (gp.gameState) {
            case PLAY -> {
                drawDebugMenu(g2);
            }
            case TITLE -> {
                drawTitleState(g2);
            }
        }
    }

    private void drawDebugMenu(Graphics2D g2){
        if (gp.keyHandler.debugMode) {
            g2.setFont(gp.basicFont);

            g2.setColor(Color.WHITE);
            g2.drawString("Debug mode", gp.tileSize * 7, gp.tileSize);
            g2.drawString("FPS: " + gp.frames, gp.tileSize * 7, gp.tileSize * 2);
            g2.drawString("X: " + gp.player.worldX / gp.tileSize, gp.tileSize * 7, gp.tileSize * 3);
            g2.drawString("Y: " + gp.player.worldY / gp.tileSize, gp.tileSize * 8.5f, gp.tileSize * 3);
        }
    }

    private void drawHealth(Graphics2D g2) {
        g2.setColor(Color.darkGray);
        g2.fillRect(gp.tileSize / 2, gp.tileSize / 2, gp.tileSize * 3, gp.tileSize / 2);
        double oneScale = (double) gp.tileSize * 3 / gp.player.maxLife;
        double hpBarValue = (double) oneScale * gp.player.life;
        g2.setColor(Color.red);
        g2.fillRect(gp.tileSize / 2 + 4, gp.tileSize / 2 + 4, (int) hpBarValue - 8, gp.tileSize / 2 - 8);
    }

    private void drawTitleState(Graphics2D g2) {
        g2.setColor(Color.lightGray);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setColor(Color.black);
        g2.setFont(g2.getFont().deriveFont(50f));
        g2.drawString("Short P✺int", gp.tileSize*8, gp.tileSize*3);
    }


}
