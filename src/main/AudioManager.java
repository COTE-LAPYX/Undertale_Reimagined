package main;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class AudioManager {

    Map<String, URL> soundURLMap = new HashMap<>();
    private final List<Clip> activeSfxClips = new ArrayList<>();
    GamePanel gp;
    public float musicVolume;
    public float sfxVolume;
    private Clip musicClip;
    private Clip barkClip;
    private long musicClipPosition = 0; // in microseconds
    private String currentMusicKey = null;

    public AudioManager(GamePanel gp) {
        this.gp = gp;

        if (gp.IsDev) {
            generateIndexFileFromFolder();
        }
        loadSoundFilesFromIndex();

        System.out.println(soundURLMap.keySet().size());

        for (String key : soundURLMap.keySet()) {
            System.out.println(key);
        } //#TODO REMOVE
    }

    public void playMusic(String key, boolean loop) {
        stopMusic();

        try {
            currentMusicKey = key;
            URL url = soundURLMap.get(key);

            if (url == null) {
                System.err.println("No audio file found for key: " + key);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            musicClip = AudioSystem.getClip();
            musicClip.open(ais);

            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(musicVolume);

            if (loop) musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClipPosition = musicClip.getMicrosecondPosition();
            musicClip.stop();
        }
    }

    public void continueMusic(boolean looped) {
        if (musicClip != null && currentMusicKey != null) {
            musicClip.setMicrosecondPosition(musicClipPosition);
            if (looped) {
                musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            musicClip.start();
        }
    }

    public void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
            musicClipPosition = 0;
        }
    }

    public void playSfx(String key) {
        try {
            URL url = soundURLMap.get(key);

            if (url == null) {
                System.err.println("No audio file found for key: " + key);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip sfxClip = AudioSystem.getClip();
            sfxClip.open(ais);

            FloatControl volumeControl = (FloatControl) sfxClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(sfxVolume);

            sfxClip.start();

            sfxClip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    sfxClip.close();
                    activeSfxClips.remove(sfxClip);
                }
            });

            activeSfxClips.add(sfxClip);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopAllSfx() {
        for (Clip clip : new ArrayList<>(activeSfxClips)) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.close();
        }
        activeSfxClips.clear();
    }

    public void playBark(String key) {
        try {
            if (barkClip != null){
                barkClip.stop();
            }

            URL url = soundURLMap.get(key);

            if (url == null) {
                System.err.println("No audio file found for key: " + key);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            barkClip = AudioSystem.getClip();
            barkClip.open(ais);

            FloatControl volumeControl = (FloatControl) barkClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(sfxVolume);

            barkClip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateIndexFileFromFolder() {
        try {
            URL dirURL = getClass().getResource("/sounds");
            if (dirURL == null) {
                System.err.println("<!> ERROR: Directory /sounds not found!");
                return;
            }
            File directory = new File(dirURL.toURI());

            List<String> wavFiles = new ArrayList<>();
            scanDirectory(directory, "sounds", wavFiles);

            File indexFile = new File("res/sounds/sounds.index");

            indexFile.getParentFile().mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(indexFile))) {
                for (String f : wavFiles) {
                    bw.write(f);
                    bw.newLine();
                }
            }
            System.out.println("Sounds index generated with " + wavFiles.size() + " entries.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scanDirectory(File dir, String relativePath, List<String> wavFiles) {
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                scanDirectory(file, relativePath + "/" + file.getName(), wavFiles);
            } else if (file.getName().toLowerCase().endsWith(".wav")) {
                String subPath = relativePath.substring("sounds".length());
                if (subPath.startsWith("/")) subPath = subPath.substring(1);
                String fileName = subPath.isEmpty() ? file.getName() : subPath + "/" + file.getName();
                wavFiles.add(fileName);
            }
        }
    }

    private void loadSoundFilesFromIndex() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/sounds/sounds.index")))) {

            if (br == null) {
                System.err.println("<!> ERROR: sounds.index file not found!");
                return;
            }

            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String key = line.substring(0, line.length() - 4);
                URL url = getClass().getResource("/sounds/" + line);
                if (url != null) {
                    soundURLMap.put(key, url);
                } else {
                    System.err.println("WARNING: Couldn't find resource for " + line);
                }
            }

            System.out.println("Loaded " + soundURLMap.size() + " sounds from index.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
