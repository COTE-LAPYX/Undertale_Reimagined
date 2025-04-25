package main.handlers;

import main.GamePanel;
import main.Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
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
        switch (gp.gameState){
            case PLAY -> {
                //region Play
                if (code == KeyEvent.VK_W) upPressed = true;
                if (code == KeyEvent.VK_S) downPressed = true;
                if (code == KeyEvent.VK_A) leftPressed = true;
                if (code == KeyEvent.VK_D) rightPressed = true;
                if (code == KeyEvent.VK_J) debugMode = !debugMode;
                if (code == KeyEvent.VK_F11) {
                    gp.isFullScreenOn = !gp.isFullScreenOn;
                    gp.setFullScreen();
                    Main.setFullScreen(gp.isFullScreenOn);
                }

                if (debugMode){
                    if (code == KeyEvent.VK_MINUS){
                        gp.player.life-=1;
                        System.out.println(gp.player.life);
                    }
                    if (code == KeyEvent.VK_EQUALS){
                        gp.player.life+=1;
                    }
                }
                //endregion
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
    }
}
