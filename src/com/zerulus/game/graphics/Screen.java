package com.zerulus.game.graphics;

import java.awt.Color;

public class Screen {

    public int[] pixels;

    private int width;
    private int height;

    public Screen(int width, int height) {
        pixels = new int[width * height];

        this.width = width;
        this.height = height;

    }

    public void clear() {
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }

    /* TODO: Create some werid thing so that multiple alpha pixels can overlap each other */
    public void drawImage(Sprite sprite, int x, int y, int w, int h, Color color) {
        int ya = 0;
        int xa = 0;
        int p;
        int a;

        for(int i = 0; i < sprite.h; i++) {
            ya = i + y;
            for(int j = 0; j < sprite.w; j++) {
                xa = j + x;

                if(xa < -sprite.w || xa >= width || ya < 0 || ya >= height) break;
                if(xa < 0) xa = 0;

                p = sprite.pixels[j + i * sprite.w];
                a = (p >> 24) & 0xff;

                if(a == 0) continue;
                pixels[xa + ya * width] = sprite.pixels[j + i * sprite.w]; 
            }    
        }

    }

    public void drawRect(int x, int y, int width, int height, int color) {
        for (int i = x; i < x + width; i++) {
			if (i < 0 | i >= this.width || y >= this.height) continue;
			if (y > 0) pixels[i + y * this.width] = color;
			if (y + height >= this.height) continue;
			if (y + height > 0) pixels[i + (y + height) * this.width] = color;
		}
		for (int j = y; j <= y + height; j++) {
			if (x >= this.width || j < 0 || j >= this.height) continue;
			if (x > 0) pixels[x + j * this.width] = color;
			if (x + width >= this.width) continue;
			if (x + width > 0) pixels[(x + width) + j * this.width] = color;
		}
    }

    public void fillRect(int x, int y, int width, int height, int color) {
        for (int i = 0; i < height; i++) {
			int yo = y + i;
			if (yo < 0 || yo >= this.height)
				continue;
			for (int j = 0; j < width; j++) {
				int xo = x + j;
				if (xo < 0 || xo >= this.width)
					continue;
				pixels[xo + yo * this.width] = color;
			}
		}
    }

}