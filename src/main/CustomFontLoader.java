package main;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CustomFontLoader {
    public static Font loadCustomFont(String path, float size) {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(path));

            Font derivedFont = customFont.deriveFont(size);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            return derivedFont;
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Times New Roman", Font.BOLD, (int) size);
        }
    }
}