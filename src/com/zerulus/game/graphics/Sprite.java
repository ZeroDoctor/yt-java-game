package com.zerulus.game.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.zerulus.game.util.Vector2f;

/**
 * Created by danie on 5/25/2017.
 */
public class Sprite {

    private BufferedImage SPRITESHEET = null;
    private BufferedImage[][] spriteArray;
    private final int TILE_SIZE = 32;
    public int w;
    public int h;
    private int wSprite;
    private int hSprite;

    public static Font currentFont;

    public Sprite(String file) {
        w = TILE_SIZE;
        h = TILE_SIZE;

        System.out.println("Loading: " + file + "...");
        SPRITESHEET = loadSprite(file);

        wSprite = SPRITESHEET.getWidth() / w;
        hSprite = SPRITESHEET.getHeight() / h;
        loadSpriteArray();
    }

    public Sprite(String file, int w, int h) {
        this.w = w;
        this.h = h;

        System.out.println("Loading: " + file + "...");
        SPRITESHEET = loadSprite(file);

        wSprite = SPRITESHEET.getWidth() / w;
        hSprite = SPRITESHEET.getHeight() / h;
        loadSpriteArray();
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void setWidth(int i) {
        w = i;
        wSprite = SPRITESHEET.getWidth() / w;
    }

    public void setHeight(int i) {
        h = i;
        hSprite = SPRITESHEET.getHeight() / h;
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    private BufferedImage loadSprite(String file) {
        BufferedImage sprite = null;
        try {
            sprite = ImageIO.read(getClass().getClassLoader().getResourceAsStream(file));
        } catch (Exception e) {
            System.out.println("ERROR: could not load file: " + file);
        }

        return sprite;
    }

    public void loadSpriteArray() {
        spriteArray = new BufferedImage[hSprite][wSprite];

        for (int y = 0; y < hSprite; y++) {
            for (int x = 0; x < wSprite; x++) {
                spriteArray[y][x] = getSprite(x, y);
            }
        }
    }

    public BufferedImage getSpriteSheet() {
        return SPRITESHEET;
    }

    public BufferedImage getSprite(int x, int y) {
        return SPRITESHEET.getSubimage(x * w, y * h, w, h);
    }

        public BufferedImage getSprite(int x, int y, int w, int h) {
        return SPRITESHEET.getSubimage(x * w, y * h, w, h);
    }

    public BufferedImage[] getSpriteArray(int i) {
        return spriteArray[i];
    }

    public BufferedImage[][] getSpriteArray2(int i) {
        return spriteArray;
    }

    public static void drawArray(Graphics2D g, ArrayList<BufferedImage> img, Vector2f pos, int width, int height,
            int xOffset, int yOffset) {
        float x = pos.x;
        float y = pos.y;

        for (int i = 0; i < img.size(); i++) {
            if (img.get(i) != null) {
                g.drawImage(img.get(i), (int) x, (int) y, width, height, null);
            }

            x += xOffset;
            y += yOffset;
        }
    }

    public static void drawArray(Graphics2D g, String word, Vector2f pos, int size) {
        drawArray(g, currentFont, word, pos, size, size, size, 0);
    }

    public static void drawArray(Graphics2D g, String word, Vector2f pos, int size, int xOffset) {
        drawArray(g, currentFont, word, pos, size, size, xOffset, 0);
    }

    public static void drawArray(Graphics2D g, String word, Vector2f pos, int width, int height, int xOffset) {
        drawArray(g, currentFont, word, pos, width, height, xOffset, 0);
    }

    public static void drawArray(Graphics2D g, Font f, String word, Vector2f pos, int size, int xOffset) {
        drawArray(g, f, word, pos, size, size, xOffset, 0);
    }

    public static void drawArray(Graphics2D g, Font f, String word, Vector2f pos, int width, int height, int xOffset,
            int yOffset) {
        float x = pos.x;
        float y = pos.y;

        currentFont = f;

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != 32)
                g.drawImage(f.getLetter(word.charAt(i)), (int) x, (int) y, width, height, null);

            x += xOffset;
            y += yOffset;
        }

    }

}
