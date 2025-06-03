package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SoundManager {

    Map<String, URL> soundURLMap = new HashMap<>();
    GamePanel gp;
    public FloatControl volumeControl;
    Clip clip;

    public SoundManager(GamePanel gp) {
        loadSoundFiles();
        this.gp = gp;

        System.out.println(soundURLMap.keySet().size());

        for (String key : soundURLMap.keySet()) {
            System.out.println(key);
        } //#TODO REMOVE
    }

    public void setFile(String key) {

        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURLMap.get(key));
            clip = AudioSystem.getClip();
            clip.open(ais);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        volumeControl.setValue(gp.volumeValue);
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    private void loadSoundFiles() {
        try {
            URL dirURL = getClass().getResource("/sounds");
            if (dirURL != null) {
                File directory = new File(dirURL.toURI());
                scanDirectory(directory, "sounds");
            } else {
                System.err.println("<!> ERROR: Directory not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanDirectory(File dir, String relativePath) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanDirectory(file, relativePath + "/" + file.getName());
            } else if (file.getName().toLowerCase().endsWith(".wav")) {
                String subPath = relativePath.substring("sounds".length());
                if (subPath.startsWith("/")) subPath = subPath.substring(1);

                String fileNameWithoutExt = file.getName().substring(0, file.getName().length() - 4);

                String key = subPath.isEmpty() ? fileNameWithoutExt : subPath + "/" + fileNameWithoutExt;

                URL fileURL = getClass().getResource("/" + relativePath + "/" + file.getName());
                soundURLMap.put(key, fileURL);
            }
        }
    }

}
