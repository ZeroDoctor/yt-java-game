package com.zerulus.game.graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Font {

    private BufferedImage FONTSHEET = null;
    private BufferedImage[][] spriteArray;
    private final int TILE_SIZE = 32;
    public int w;
    public int h;
    private int wLetter;
    private int hLetter;
    private Color color = new Color(0, 0, 0);
    private Color defaultColor = new Color(0, 0, 0);

    public Font(String file) {
        w = TILE_SIZE;
        h = TILE_SIZE;

        System.out.println("Loading: " + file + "...");
        FONTSHEET = loadFont(file);

        wLetter = FONTSHEET.getWidth() / w;
        hLetter = FONTSHEET.getHeight() / h;
        loadFontArray();
    }

    public Font(String file, int w, int h) {
        this.w = w;
        this.h = h;

        System.out.println("Loading: " + file + "...");
        FONTSHEET = loadFont(file);

        wLetter = FONTSHEET.getWidth() / w;
        hLetter = FONTSHEET.getHeight() / h;
        loadFontArray();
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
        this.color = defaultColor;
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setWidth(int i) {
        w = i;
        wLetter = FONTSHEET.getWidth() / w;
    }

    public void setHeight(int i) {
        h = i;
        hLetter = FONTSHEET.getHeight() / h;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    private BufferedImage loadFont(String file) {
        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(file));
        } catch (Exception e) {
            System.out.println("ERROR: could not load file: " + file);
        }

        return sprite;
    }

    public void loadFontArray() {
        spriteArray = new BufferedImage[wLetter][hLetter];

        for (int x = 0; x < wLetter; x++) {
            for (int y = 0; y < hLetter; y++) {
                spriteArray[x][y] = getLetter(x, y);
            }
        }
    }

    public BufferedImage getFontSheet() {
        return FONTSHEET;
    }

    public BufferedImage getLetter(int x, int y) {
        BufferedImage img = FONTSHEET.getSubimage(x * w, y * h, w, h);
        if(color == defaultColor) {
            return img;
        }

        int[] p = new int[w * h];
        for(int i = 0; i < w; i++) {
            for(int j = 0; j < h; j++) {
                p[i+j] = img.getRGB(i, j);
                if(p[i+j] == defaultColor.getRGB()) {
                    img.setRGB(i, j, color.getRGB());
                }
            }
        }

        return img;
    }

    public Sprite getLetter(char letter) {
        int value = letter;

        int x = value % wLetter;
        int y = value / wLetter;
        return new Sprite(getLetter(x, y));
    }
}
