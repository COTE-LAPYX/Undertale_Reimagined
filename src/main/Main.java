package main;

import javax.swing.*;

public class Main {
    public static JFrame window;

    public static void main(String[] args) {
        window = new JFrame();
        GamePanel gamePanel = new GamePanel();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Undertale:Reimagined");
        new Main().setIcon();

        window.setUndecorated(gamePanel.isFullScreenOn);

        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setUpGame();
        gamePanel.startGameThread();
    }

    public void setIcon(){
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("images/icon.png"));
        window.setIconImage(icon.getImage());
    }

    public static void setFullScreen(boolean fson){
        window.dispose();
        window.setUndecorated(fson);
        window.setVisible(true);
    }
}