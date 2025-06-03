package main.Dialogs;

import main.GamePanel;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DialogManager {
    public int dialogCounter = 0;
    public int dialogLetterCount = 0;
    public String lastDialogKey = "";
    GamePanel gp;
    Map<String, Dialog> dialogMap = new HashMap<>();

    public DialogManager(GamePanel gp) {
        this.gp = gp;

        //dialogMap.put("FloweyDialog1", "* Hello World. \n   This is a Test Dialog.");

        loadDialogs();
    }

    public void loadDialogs() {
        String path = "/translations/english.tran";
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) {
            throw new RuntimeException(new FileNotFoundException("<!> Resource not found: " + path));
        }

        try (InputStreamReader isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8); BufferedReader reader = new BufferedReader(isr)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] dialog = line.split("<S>");
                if (dialog.length != 4) System.out.println("<!> Error: dialog length != 4");

                dialogMap.put(dialog[0], new Dialog(dialog[3], dialog[1], Integer.parseInt(dialog[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] getDialogSplitText(String key) {
        return dialogMap.get(key).text.split("<N>");
    }

    public int getDialogSpeed(String key) {
        return dialogMap.get(key).speed;
    }

    public String getDialogSoundKey(String key) {
        return dialogMap.get(key).soundKey;
    }

    public void drawDialog(String dialogKey, Graphics2D g2, int x, int y, Font font, Color fontColor) {
        g2.setFont(font);
        g2.setColor(fontColor);
        int dialogSpeed = getDialogSpeed(dialogKey);
        if (!Objects.equals(lastDialogKey, dialogKey)) {
            lastDialogKey = dialogKey;
            dialogLetterCount = 0;
        }

        String[] dialogSplit = getDialogSplitText(dialogKey);
        String dialog = String.join("", dialogSplit);

        if (dialogCounter <= 0 && dialogLetterCount < dialog.length()) {
            dialogCounter = dialogSpeed;
            dialogLetterCount++;

            if (dialogLetterCount < dialog.length() && dialog.charAt(dialogLetterCount) != ' ') {
                gp.playSE(getDialogSoundKey(dialogKey));
            }

        } else if (dialogCounter > 0) {
            dialogCounter--;
        }

        int dialogLetterBuffer = dialogLetterCount;
        for (int dialogNum = 0; dialogNum < dialogSplit.length; dialogNum++) {
            int toBeShown = 0;

            for (int i = 0; i < dialogSplit[dialogNum].length(); i++) {
                if (dialogLetterBuffer > 0) {
                    dialogLetterBuffer--;
                    toBeShown++;
                }
            }

            g2.drawString(dialogSplit[dialogNum].substring(0, toBeShown), x, y + (gp.tileSize * 0.5f * dialogNum));
        }
    }
}