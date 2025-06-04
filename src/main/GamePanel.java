package main;


import entities.Entity;
import entities.Player;
import entities.encounters.PlayerSoul;
import enums.EncounterStateEnum;
import enums.GameStateEnum;
import enums.TitleStateEnum;
import events.EventHandler;
import main.Dialogs.DialogManager;
import main.handlers.KeyHandler;
import main.handlers.MouseHandler;
import tiles.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements Runnable {

    //region Variables

    public final boolean IsDev = false;
    public final int originalTileSize = 32; // 16x16
    public final int scale = 2;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 10; //20
    public final int maxScreenRow = 8; //12
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int screenWidth = tileSize * maxScreenCol;
    public float volumeValue;
    int screenWidth2 = screenWidth;
    public final int screenHeight = tileSize * maxScreenRow;
    int screenHeight2 = screenHeight;
    public int FPS = 60;
    public double drawInterval;
    public int frames;
    public boolean isFullScreenOn = false;
    public GameStateEnum gameState = GameStateEnum.OTHER;
    public EncounterStateEnum encounterState = EncounterStateEnum.NONE;
    public TitleStateEnum titleState = TitleStateEnum.CUTSCENE;
    public KeyHandler keyHandler = new KeyHandler(this);
    public Player player = new Player(this, keyHandler);
    public TileManager tileManager = new TileManager(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public MouseHandler mouseHandler = new MouseHandler(this);
    public EventHandler eventHandler = new EventHandler(this);
    private CustomFontLoader customFontLoader = new CustomFontLoader();
    public UI ui = new UI(this);
    long timer = 0;
    int drawCount = 0;
    double delta = 0;
    BufferedImage tempScreen;
    Graphics2D g2;
    Thread gameThread;
    List<Entity> entityList = new ArrayList<>();
    public Font basicFont;
    public String currentMapName = "startroom";
    public Entity playerSoul = new PlayerSoul(this, keyHandler);
    public List<Entity> hitboxes = new ArrayList<>();
    public Entity currentEncounter;
    public DialogManager dialogManager = new DialogManager(this);
    public DataManager dataManager = new DataManager(this);
    public AssetSetter assetSetter = new AssetSetter(this);
    public EncounterManager encounterManager = new EncounterManager(this);
    public AudioManager audioManager = new AudioManager(this);
    public int maxEncounterAnimationCounter = 120;
    public int encounterAnimationCounter = maxEncounterAnimationCounter;
    public int maxBlackoutCounter = 60;
    public int blackoutCounter = maxBlackoutCounter;

    //endregion

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.addMouseMotionListener(mouseHandler);
        this.setFocusable(true);
    }

    public void setUpGame() {
        basicFont = customFontLoader.loadCustomFont("/fonts/determination_eng.otf", 24);
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
        g2.setFont(basicFont);
        gameState = GameStateEnum.TITLE;

        audioManager.playMusic("themes/titleIntroTheme", false);
/*        try {
            dataManager.loadData();
            dataManager.loadOptionConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }

    public void startGameThread() {
        try {
            gameThread = new Thread(this);
            gameThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        drawInterval = 1000000000 / FPS;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                drawToTempScreen();
                drawToScreen();
                delta--;
                drawCount++;
            }
            if (timer > 1000000000) {
                frames = drawCount;
                drawCount = 0;
                timer = 0;
            }
        }
    }

    private void update() {
        if (gameState == GameStateEnum.PLAY) {
            eventHandler.checkEvent();
            player.update();
        } else if (gameState == GameStateEnum.ENCOUNTER){
            playerSoul.update();

            if (encounterState == EncounterStateEnum.TRANSITION){
                if (blackoutCounter > 0) {
                    blackoutCounter--;
                } else {
                    if (encounterAnimationCounter > 0){
                        encounterAnimationCounter --;
                    } else {
                        encounterState = EncounterStateEnum.TURN_PLAYER;
                        encounterAnimationCounter = maxEncounterAnimationCounter;
                        blackoutCounter = maxBlackoutCounter;
                    }
                }
            }
        }
    }

    public void drawToScreen() {
        Graphics g = getGraphics();
        if (g != null) {
            /*g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
            g.dispose();*/

            double originalAspect = (double) screenWidth / screenHeight;
            double targetAspect = (double) screenWidth2 / screenHeight2;

            int drawWidth, drawHeight;
            int offsetX = 0, offsetY = 0;

            if (targetAspect > originalAspect) {
                drawHeight = screenHeight2;
                drawWidth = (int) (drawHeight * originalAspect);
                offsetX = (screenWidth2 - drawWidth) / 2;
            } else {
                drawWidth = screenWidth2;
                drawHeight = (int) (drawWidth / originalAspect);
                offsetY = (screenHeight2 - drawHeight) / 2;
            }

            g.drawImage(tempScreen, offsetX, offsetY, drawWidth, drawHeight, null);
            g.dispose();
        }
    }

    public void drawToTempScreen() {
        if (gameState == GameStateEnum.PLAY) {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);

            tileManager.draw(g2);

            for (int i = 0; i < entityList.size(); i++)
                entityList.get(i).draw(g2);

            player.draw(g2);
        } else if (gameState == GameStateEnum.ENCOUNTER) {
            g2.setColor(Color.black);
            g2.fillRect(0, 0, screenWidth, screenHeight);

            playerSoul.draw(g2);

            if (keyHandler.debugMode){
                for (int i = 0; i < hitboxes.size(); i++) {
                    hitboxes.get(i).draw(g2);
                }
            }
        }
        ui.draw(g2);
    }

    public void setFullScreen() {
        if (isFullScreenOn) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth();
            double height = screenSize.getHeight();
            Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            screenWidth2 = (int) width;
            screenHeight2 = (int) height;
            /*//offset factor to be used by mouse listener or mouse motion listener if you are using cursor in your game. Multiply your e.getX()e.getY() by this.
            fullScreenOffsetFactor = (float) screenWidth / (float) screenWidth2;*/
        } else {
            screenWidth2 = screenWidth;
            screenHeight2 = screenHeight;
            Main.window.setSize(screenWidth2, screenHeight2);
            Main.window.setLocationRelativeTo(null);
        }
    }

    public void loadMapAndEntity(String mapName) {
        currentMapName = mapName;
        tileManager.loadMap("/maps/" + mapName + ".txt");
        assetSetter.loadEntity();
    }
}
