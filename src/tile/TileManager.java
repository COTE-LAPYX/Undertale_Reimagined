package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TileManager {

    public List<Tile> tileList;
    public int[][] mapTileNum;
    GamePanel gp;
    Color collisionColor = new Color(172, 50, 50);

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tileList = new ArrayList<>();
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

        getTileImage();
    }

    public void getTileImage() {

        try {

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/tiles/tilesheet1.png"));
            BufferedImage collisionSheet = ImageIO.read(getClass().getResourceAsStream("/tiles/collisionsheet1.png"));

            if (tileSheet.getWidth() != collisionSheet.getWidth() || tileSheet.getHeight() != collisionSheet.getHeight()) {
                System.out.println("COLLISION SHEET AND TILE SHEET MUST MATCH (TileManager.java)");
                throw new RuntimeException();
            }


            for (int y = 0; y < tileSheet.getHeight() / gp.originalTileSize; y++) {
                for (int x = 0; x < tileSheet.getWidth() / gp.originalTileSize; x++) {
                    Tile tile = new Tile();
                    tile.image = tileSheet.getSubimage(x * gp.originalTileSize, y * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize);
                    if (collisionSheet.getSubimage(x * gp.originalTileSize, y * gp.originalTileSize, gp.originalTileSize, gp.originalTileSize).getRGB(1, 1) == collisionColor.getRGB()) {
                        tile.collision = true;
                    }
                    tileList.add(tile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String numbers[] = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {

        int worldCol = 0;
        int worldRow = 0;


        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.tileSize - gp.player.screenX &&
                    worldX - gp.tileSize < gp.player.worldX + gp.tileSize + gp.player.screenX &&
                    worldY + gp.tileSize > gp.player.worldY - gp.tileSize - gp.player.screenY &&
                    worldY - gp.tileSize < gp.player.worldY + gp.tileSize + gp.player.screenY) {
                g2.drawImage(tileList.get(tileNum).image, screenX, screenY, gp.tileSize, gp.tileSize, null);
            }


            worldCol++;

            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
