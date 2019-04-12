package com.zerulus.game.graphics;

import com.zerulus.game.util.Matrix;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Sprite {

	public BufferedImage image;
	
	public int[] pixels;
	private int[] ogpixels;

	public int w;
	public int h;

	public static enum effect {NORMAL, SEPIA, REDISH, GRAYSCALE, NEGATIVE};

	private float[][] id = {{1.0f, 0.0f, 0.0f},
							{0.0f, 1.0f, 0.0f},
							{0.0f, 0.0f, 1.0f},
							{0.0f, 0.0f, 0.0f}};

	private float[][] negative = {{1.0f, 0.0f, 1.0f},
								  {0.0f, 1.0f, 0.0f},
								  {1.0f, 1.0f, 0.0f},
								  {0.0f, 0.0f, 0.0f}};

	private float[][] sepia = {{0.393f, 0.349f, 0.272f},
							   {0.769f, 0.686f, 0.534f},
							   {0.189f, 0.168f, 0.131f},
							   {0.000f, 0.000f, 0.000f}};

	private float[][] redish = {{1.0f, 0.0f, 0.0f},
							    {0.0f, 0.3f, 0.0f},
								{0.0f, 0.0f, 0.3f},
							    {0.0f, 0.0f, 0.0f}};
								
	private float[][] grayscale = {{0.333f, 0.333f, 0.333f},
							  	   {0.333f, 0.333f, 0.333f},
								   {0.333f, 0.333f, 0.333f},
								   {0.000f, 0.000f, 0.000f}};

	private float[][] currentEffect = id;

    public Sprite(BufferedImage image) {
		this.image = image;
		this.w = image.getWidth();
		this.h = image.getHeight();
		ogpixels = image.getRGB(0, 0, w, h, ogpixels, 0, w);
		pixels = ogpixels;
	}
	
	public Sprite(BufferedImage image, int w, int h) {
		this.image = image;
		this.w = w;
		this.h = h;
		ogpixels = image.getRGB(0, 0, w, h, ogpixels, 0, w);
		pixels = ogpixels;
	}
	
	public void resize(int w, int h) {
		int[] newPixels = new int[w * h];

		int xRatio = (int) ((this.w << 16) / w) + 1;
		int yRatio = (int) ((this.h << 16) / h) + 1;

		int x2;
		int y2;
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				x2 = (x * xRatio) >> 16;
				y2 = (y * yRatio) >> 16;
				newPixels[x + y * w] = pixels[x2 + y2 * this.w]; 
			}
		}

		pixels = new int[w * h];
		pixels = newPixels;

		this.w = w;
		this.h = h;

		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		newImage.setRGB(0, 0, w, h, pixels, 0, w);
		image = newImage;
    }

    public void saveColors() {
		ogpixels = pixels;
		currentEffect = id;
	}
	
	public void restoreColors() {
		pixels = ogpixels;
	}

	public void restoreDefault() {
		pixels = image.getRGB(0, 0, w, h, pixels, 0, w);
	}

	// in #FFFFFF format
	public Color hexToColor(String color) {
		return new Color(
            Integer.valueOf(color.substring(1, 3), 16),
            Integer.valueOf(color.substring(3, 5), 16),
            Integer.valueOf(color.substring(5, 7), 16));
	}

	public void setContrast(float value) {
		float[][] effect = id;
		float contrast = (259 * (value + 255)) / (255 * (259 - value));
		for(int i = 0; i < 3; i++) {
			if(i < 3)
				effect[i][i] = contrast;
			effect[3][i] = 128 * (1 - contrast);
		}
		
		addEffect(effect);
	}

	public void setBrightness(float value) {
		float[][] effect = id;
		for(int i = 0; i < 3; i++)
			effect[3][i] = value;
		
		addEffect(effect);
	}

    public void setEffect(effect e) {
		float[][] effect;
		switch (e) {
			case SEPIA: effect = sepia;
			break;
			case REDISH: effect = redish;
			break;
			case GRAYSCALE: effect = grayscale;
			break;
			case NEGATIVE: effect = negative;
			break;
			default: effect = id;
		}
		
		if(effect != currentEffect) {
			addEffect(effect);
		}
	}

	private void addEffect(float[][] effect) {
		float[][] rgb = new float[1][4];
		float[][] xrgb;
		for(int x = 0; x < w; x++) {
		    for(int y = 0; y < h; y++) {
			    int p = pixels[x + y * h];

				int a = (p >> 24) & 0xff;

				rgb[0][0] = (p >> 16) & 0xff;
				rgb[0][1] = (p >> 8) & 0xff;
				rgb[0][2] = (p) & 0xff;
				rgb[0][3] = 1f;

				xrgb = Matrix.multiply(rgb, effect);

				for(int i = 0; i < 3; i++) {
					if(xrgb[0][i] > 255) rgb[0][i] = 255;
					else if(xrgb[0][i] < 0) rgb[0][i] = 0;
					else rgb[0][i] = xrgb[0][i];
				}
				
				p = (a<<24) | ((int) rgb[0][0]<<16) | ((int) rgb[0][1]<<8) | (int) rgb[0][2];
				pixels[x + y * h] = p;
			}
		}
		currentEffect = effect;
	}

    public Sprite getSubimage(int x, int y, int w, int h) {
		return new Sprite(image.getSubimage(x, y, w, h));
	}
	
	public Sprite getNewSubimage(int x, int y, int w, int h) {
		BufferedImage temp = image.getSubimage(x, y, w, h);
		BufferedImage newImage = new BufferedImage(image.getColorModel(), image.getRaster().createCompatibleWritableRaster(w,h), image.isAlphaPremultiplied(), null);
		temp.copyData(newImage.getRaster());
        return new Sprite(newImage);
	}
}

