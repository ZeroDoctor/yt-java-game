package com.zerulus.game.tiles;

import java.awt.Graphics2D;
import java.util.ArrayList;

import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.util.AABB;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.tiles.blocks.Block;
import com.zerulus.game.tiles.blocks.NormBlock;

public class TileMapNorm extends TileMap {

    private Block[] blocks;

    private int tileWidth;
    private int tileHeight;

    private int height;

    public TileMapNorm(String data, Sprite sprite, int width, int height, int tileWidth, int tileHeight, int tileColumns) {
        blocks = new Block[width * height];

        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        this.height = height;

        String[] block = data.split(",");
        for(int i = 0; i < (width * height); i++) {
            int temp = Integer.parseInt(block[i].replaceAll("\\s+",""));
             if(temp != 0) {
                 blocks[i] = new NormBlock(sprite.getSprite((int) ((temp - 1) % tileColumns), (int) ((temp - 1) / tileColumns) ), new Vector2f((int) (i % width) * tileWidth, (int) (i / height) * tileHeight), tileWidth, tileHeight);
             }
        }
    }

    public void render(Graphics2D g, AABB cam) {
        int x = (int) ((cam.getPos().x) / tileWidth);
        int y = (int) ((cam.getPos().y) / tileHeight);
        for(int i = x; i < x + (cam.getWidth() / tileWidth); i++) {
            for(int j = y; j < y + (cam.getHeight() / tileHeight); j++) {
                if(blocks[i + (j * height)] != null)
                    blocks[i + (j * height)].render(g);
            }
        }
    }

}
