package com.zerulus.game.tiles;

import java.util.HashMap;
import java.awt.Graphics2D;

import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.util.Vector2f;
import com.zerulus.game.tiles.blocks.Block;
import com.zerulus.game.tiles.blocks.HoleBlock;
import com.zerulus.game.tiles.blocks.ObjBlock;

public class TileMapObj extends TileMap {

    public static HashMap<String, Block> tmo_blocks;

    public TileMapObj(String data, Sprite sprite, int width, int height, int tileWidth, int tileHeight, int tileColumns) {
        Block tempBlock;
        tmo_blocks = new HashMap<String, Block>();

        String[] block = data.split(",");
        for(int i = 0; i < (width * height); i++) {
            int temp = Integer.parseInt(block[i].replaceAll("\\s+",""));
             if(temp != 0) {
                 if(temp == 172) {
                     tempBlock = new HoleBlock(sprite.getSprite((int) ((temp - 1) % tileColumns), (int) ((temp - 1) / tileColumns) ),new Vector2f((int) (i % width) * tileWidth, (int) (i / height) * tileHeight), tileWidth, tileHeight);
                 } else {
                     tempBlock = new ObjBlock(sprite.getSprite((int) ((temp - 1) % tileColumns), (int) ((temp - 1) / tileColumns) ),new Vector2f((int) (i % width) * tileWidth, (int) (i / height) * tileHeight), tileWidth, tileHeight);
                 }
                 tmo_blocks.put(String.valueOf((int) (i % width)) + "," + String.valueOf((int) (i / height)), tempBlock);
             }
        }
    }

    public void render(Graphics2D g) {
        for(Block block: tmo_blocks.values()) {
            block.render(g);
        }
    }

}
