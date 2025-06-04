package main.handlers;

import enums.GameStateEnum;
import enums.TitleStateEnum;
import main.GamePanel;
import main.Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public int upKey = KeyEvent.VK_UP;
    public int leftKey = KeyEvent.VK_LEFT;
    public int rightKey = KeyEvent.VK_RIGHT;
    public int downKey = KeyEvent.VK_DOWN;
    public int acceptKey = KeyEvent.VK_ENTER;
    public int specialKey = KeyEvent.VK_X;
    public boolean upPressed, downPressed, leftPressed, rightPressed, specialPressed;
    public boolean debugMode = false;

    GamePanel gp;

    public KeyHandler(GamePanel gp) {
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (debugMode) {
            System.out.println("------------------------------");
            System.out.println(KeyEvent.getKeyText(code));
            System.out.println("------------------------------");
        }

        if (code == KeyEvent.VK_F11) {
            gp.isFullScreenOn = !gp.isFullScreenOn;
            gp.setFullScreen();
            Main.setFullScreen(gp.isFullScreenOn);
        }


        switch (gp.gameState) {
            case TITLE -> {
                //region Title
                switch (gp.titleState) {
                    case CUTSCENE -> {
                        if (code == acceptKey) {
                            gp.ui.cutsceneCounter = 0;
                            gp.titleState = TitleStateEnum.PRESS_TO_CONTINUE;
                            gp.audioManager.stopMusic();
                        }
                    }
                    case PRESS_TO_CONTINUE -> {
                        if (code == acceptKey) {
                            gp.loadMapAndEntity("startroom");
                            gp.gameState = GameStateEnum.PLAY;
                        }
                    }
                }

                //endregion
            }
            case PLAY -> {
                //region Play
                if (code == upKey) upPressed = true;
                if (code == downKey) downPressed = true;
                if (code == leftKey) leftPressed = true;
                if (code == rightKey) rightPressed = true;
                if (code == KeyEvent.VK_J) debugMode = !debugMode;

                if (debugMode) {
                    if (code == KeyEvent.VK_MINUS) {
                        gp.player.life -= 1;
                    }
                    if (code == KeyEvent.VK_EQUALS) {
                        gp.player.life += 1;
                    }
                }
                //endregion
            }
            case ENCOUNTER -> {
                //region Encounter
                if (code == upKey) upPressed = true;
                if (code == downKey) downPressed = true;
                if (code == leftKey) leftPressed = true;
                if (code == rightKey) rightPressed = true;
                if (code == specialKey) specialPressed = true;
                if (code == KeyEvent.VK_J) debugMode = !debugMode;

                if (debugMode) {
                    if (code == KeyEvent.VK_MINUS) {
                        gp.player.life -= 1;
                    }
                    if (code == KeyEvent.VK_EQUALS) {
                        gp.player.life += 1;
                    }
                }

                switch (gp.encounterState){
                    case TURN_PLAYER -> {
                        if (rightPressed) {
                            gp.ui.commandNum++;
                            gp.audioManager.playSfx("soundEffects/ping");
                            if (gp.ui.commandNum > 3) {
                                gp.ui.commandNum = 0;
                            }
                        }

                        if (leftPressed) {
                            gp.ui.commandNum--;
                            gp.audioManager.playSfx("soundEffects/ping");
                            if (gp.ui.commandNum < 0) {
                                gp.ui.commandNum = 3;
                            }
                        }
                    }
                }
                //endregion
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == upKey) upPressed = false;
        if (code == downKey) downPressed = false;
        if (code == leftKey) leftPressed = false;
        if (code == rightKey) rightPressed = false;
        if (code == specialKey) specialPressed = false;
    }
}
