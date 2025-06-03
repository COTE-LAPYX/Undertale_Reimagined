package main;

import enums.EncounterStateEnum;
import enums.TitleStateEnum;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UI {
    public int commandNum = 0;
    public int cutsceneCounter = 360;
    public int blackoutCounter = 30; //#TODO MAKE BLACKSCREENING
    public int dialogNum = 1;
    GamePanel gp;
    List<BufferedImage> battleImages = new ArrayList<>();
    List<BufferedImage> cutsceneImages = new ArrayList<>();

    public UI(GamePanel gp) {
        this.gp = gp;

        getImages();
    }

    private void getImages() {
        BufferedImage tileSheet;
        try {
            tileSheet = ImageIO.read(getClass().getResourceAsStream("/images/buttons.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int y = 0; y < tileSheet.getHeight() / 24; y++) {
            for (int x = 0; x < tileSheet.getWidth() / 71; x++) {
                battleImages.add(tileSheet.getSubimage(x * 71, y * 24, 71, 24));
            }
        }

        try {
            tileSheet = ImageIO.read(getClass().getResourceAsStream("/images/slidesheet.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int y = 0; y < tileSheet.getHeight() / 110; y++) {
            for (int x = 0; x < tileSheet.getWidth() / 200; x++) {
                cutsceneImages.add(tileSheet.getSubimage(x * 200, y * 110, 200, 110));
            }
        }
    }

    public void draw(Graphics2D g2) {
        switch (gp.gameState) {
            case PLAY -> {
            }
            case TITLE -> { //#TODO CUTSCENE FOR TITLE STATE
                switch (gp.titleState) {
                    case CUTSCENE -> {
                        drawCutsceneState(g2);
                    }
                    case PRESS_TO_CONTINUE -> drawTitleState(g2);
                }
            }
            case ENCOUNTER -> {
                drawEncounterState(g2);
                if (gp.encounterState == EncounterStateEnum.TRANSITION) {
                    drawEncounterIntro(g2);
                }
            }
        }

        drawDebugMenu(g2);
    }

    private void drawDebugMenu(Graphics2D g2) {
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
        g2.setColor(Color.red);
        g2.fillRect(gp.tileSize * 4, (int) (gp.tileSize * 6.43f), gp.tileSize * 2, (int) (gp.tileSize / 3f));
        double oneScale = (double) gp.tileSize * 2 / gp.player.maxLife;
        double hpBarValue = (double) oneScale * gp.player.life;

        g2.setColor(Color.yellow);
        g2.fillRect(gp.tileSize * 4, (int) (gp.tileSize * 6.43f), (int) hpBarValue, (int) (gp.tileSize / 3f));
    }

    private void drawPatience(Graphics2D g2) {
        double oneScale = (double) gp.tileSize * 2 / gp.player.maxPatience;
        double barValue = (double) oneScale * gp.player.patience;

        g2.setColor(Color.cyan);
        g2.fillRect(gp.tileSize * 4, (int) (gp.tileSize * 6.43f), (int) barValue, (int) (gp.tileSize / 12f));
    }

    private void drawTitleState(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(72f));
        g2.drawString("Undertale", getXForCenter("Undertale", g2), gp.tileSize * 2.5f);
        g2.setColor(Color.cyan);
        g2.drawString("Re", getXForCenter("Reimagined", g2), gp.tileSize * 3.7f);
        g2.setColor(Color.red);
        g2.drawString("  imagined", getXForCenter("Reimagined", g2), gp.tileSize * 3.7f);

        g2.setFont(g2.getFont().deriveFont(16f));
        g2.setColor(Color.lightGray);
        g2.drawString("[Press " + KeyEvent.getKeyText(gp.keyHandler.acceptKey) + " to start]", getXForCenter("[Press " + KeyEvent.getKeyText(gp.keyHandler.acceptKey) + " to start]", g2), gp.tileSize * 7);
    }

    private void drawEncounterState(Graphics2D g2) {
        int buttonWidth = battleImages.get(0).getWidth() * 2;
        int buttonHeight = battleImages.get(0).getHeight() * 2;

        g2.drawImage(battleImages.get(0), (int) (gp.tileSize * 0.2f), gp.tileSize * 7, buttonWidth, buttonHeight, null);
        g2.drawImage(battleImages.get(1), (int) (gp.tileSize * 2.7f), gp.tileSize * 7, buttonWidth, buttonHeight, null);
        g2.drawImage(battleImages.get(2), (int) (gp.tileSize * 5.2f), gp.tileSize * 7, buttonWidth, buttonHeight, null);
        g2.drawImage(battleImages.get(3), (int) (gp.tileSize * 7.7f), gp.tileSize * 7, buttonWidth, buttonHeight, null);

        g2.setColor(Color.lightGray);

        if (gp.encounterState == EncounterStateEnum.TURN_ENEMY) {
            g2.drawRect(gp.tileSize * 3, gp.tileSize * 4, gp.tileSize * 4, gp.tileSize * 2);
        } else if (gp.encounterState == EncounterStateEnum.TURN_PLAYER) {
            switch (commandNum) {
                case 0 ->
                        g2.drawImage(battleImages.get(4), (int) (gp.tileSize * 0.2f), gp.tileSize * 7, buttonWidth, buttonHeight, null);
                case 1 ->
                        g2.drawImage(battleImages.get(5), (int) (gp.tileSize * 2.7f), gp.tileSize * 7, buttonWidth, buttonHeight, null);
                case 2 ->
                        g2.drawImage(battleImages.get(6), (int) (gp.tileSize * 5.2f), gp.tileSize * 7, buttonWidth, buttonHeight, null);
                case 3 ->
                        g2.drawImage(battleImages.get(7), (int) (gp.tileSize * 7.7f), gp.tileSize * 7, buttonWidth, buttonHeight, null);
            }

            g2.drawRect(gp.tileSize, gp.tileSize * 4, gp.tileSize * 8, gp.tileSize * 2);
            /*g2.setFont(gp.basicFont);
            String[] dialogSplit = gp.dialogManager.getDialog("FloweyDialog1");
            for (int i = 0; i < dialogSplit.length; i++) {
                g2.drawString(dialogSplit[i], gp.tileSize + gp.tileSize / 4f, gp.tileSize * 4.5f + (gp.tileSize * 0.5f * i));
            }*/

            gp.dialogManager.drawDialog("FloweyDialog1", g2, gp.tileSize + gp.tileSize / 4, (int) (gp.tileSize * 4.5), gp.basicFont, Color.RED);
        }

        g2.setFont(gp.basicFont);
        g2.setColor(Color.white);
        g2.drawString("SLAVA", (int) (gp.tileSize * 0.5f), gp.tileSize * 6.7f);
        g2.drawString("LV " + gp.player.level, (int) (gp.tileSize * 2f), gp.tileSize * 6.7f);

        g2.drawString("HP", (int) (gp.tileSize * 3.5f), gp.tileSize * 6.7f);
        g2.drawString(gp.player.life + " / " + gp.player.maxLife, (int) (gp.tileSize * 6.1f), gp.tileSize * 6.7f);
        drawHealth(g2);

        drawPatience(g2);

        g2.drawImage(gp.currentEncounter.currentSprite, (int) (gp.tileSize * 4.25f), gp.tileSize * 2, gp.currentEncounter.currentSprite.getWidth() * 2, gp.currentEncounter.currentSprite.getHeight() * 2, null);
    }

    private void drawEncounterIntro(Graphics2D g2) {
        float transparency = 0f + 0.1f * (gp.encounterAnimationCounter / (gp.maxEncounterAnimationCounter / 10f));
        g2.setColor(Color.BLACK);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

    private void drawCutsceneState(Graphics2D g2) {
        if (cutsceneCounter >= 0 && dialogNum <= 6) {
            cutsceneCounter--;
        } else if (dialogNum <= 5){
            cutsceneCounter = 360;
            dialogNum++;
        } else {
            gp.titleState = TitleStateEnum.PRESS_TO_CONTINUE;
        }

        g2.setColor(Color.black);
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        g2.drawImage(cutsceneImages.get(dialogNum - 1), 120, (int) (gp.tileSize * 1.2f), cutsceneImages.get(0).getWidth() * 2, cutsceneImages.get(0).getHeight() * 2, null);

        gp.dialogManager.drawDialog("IntroDialog" + dialogNum, g2, (int) (gp.tileSize * 1.80f), (int) (gp.tileSize * 5.5f), gp.basicFont, Color.white);
    }

    public int getXForCenter(String text, Graphics2D g2) {
        int x;
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        x = gp.screenWidth / 2 - length / 2;
        return x;
    }
}
