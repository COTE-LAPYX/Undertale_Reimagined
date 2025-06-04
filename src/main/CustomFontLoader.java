package main;

import java.awt.*;
import java.io.IOException;

public class CustomFontLoader {
    public Font loadCustomFont(String path, float size) {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(path));

            Font derivedFont = customFont.deriveFont(size);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);

            return derivedFont;
        } catch (FontFormatException e) {
            e.printStackTrace();
            return new Font("Times New Roman", Font.BOLD, (int) size);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}