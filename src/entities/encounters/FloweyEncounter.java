package entities.encounters;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class FloweyEncounter extends Encounter{
    public FloweyEncounter(GamePanel gp) {
        super(gp);

        getEncounterImages();

        currentSprite = sprites[0];
    }

    public void getEncounterImages() {
        try {
            sprites = new BufferedImage[2];

            BufferedImage tileSheet = ImageIO.read(getClass().getResourceAsStream("/monsters/flowey/flowey_encounter_spritesheet.png"));

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
}
