package main;

import java.io.*;

public class DataManager {
    GamePanel gp;
    String dataFolder = System.getProperty("user.home") + "\\AppData\\Roaming\\Undertale-Reimagined";
    String dataFolderBin = System.getProperty("user.home") + "\\AppData\\Roaming\\Undertale-Reimagined\\bin";
    String dataFolderSave = System.getProperty("user.home") + "\\AppData\\Roaming\\Undertale-Reimagined\\bin\\saves";
    String dataFolderContent = System.getProperty("user.home") + "\\AppData\\Roaming\\Undertale-Reimagined\\content";
    File dir = new File(dataFolder);
    File dirBin = new File(dataFolderBin);
    File dirSave = new File(dataFolderSave);
    File dirContent = new File(dataFolderContent);
    File data = new File(dataFolderSave + "\\save01.dat");
    File optionsConfig = new File(dataFolderBin + "\\config.conf");

    public DataManager(GamePanel gp) {
        this.gp = gp;
    }

    public void loadData() throws IOException {
        checkFiles();

        BufferedReader br = new BufferedReader(new FileReader(data));

        String line;
        while ((line = br.readLine()) != null)
            //if (line.startsWith("p:")) {}

            br.close();
    }

    public void saveData() throws IOException {
        checkFiles();

        PrintWriter pw = new PrintWriter(data);
        //pw.print("p:" + gp.points);

        pw.close();
    }

    public void loadOptionConfig() throws IOException {
        checkFiles();
        BufferedReader br = new BufferedReader(new FileReader(optionsConfig));

        String st;
        while ((st = br.readLine()) != null)
            if (st.startsWith("fs:")) {
                gp.isFullScreenOn = Boolean.parseBoolean(st.substring(3));
        }
        br.close();
    }

    public void saveOptionConfig() throws IOException {
        checkFiles();

        PrintWriter pw = new PrintWriter(optionsConfig);
        pw.print("fs:" + gp.isFullScreenOn);

        pw.close();
    }


    void checkFiles() throws IOException {
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!dirBin.exists()) {
            dirBin.mkdir();
        }
        if (!dirContent.exists()) {
            dirContent.mkdir();
        }
        if (!dirSave.exists()) {
            dirSave.mkdir();
        }
        if (!data.exists()) {
            data.createNewFile();
        }
        if (!optionsConfig.exists()) {
            optionsConfig.createNewFile();
        }
    }
}
