package com.zerulus.game.tiles;

import com.zerulus.game.math.AABB;
import com.zerulus.game.tiles.blocks.Block;

import java.awt.Graphics2D;

public abstract class TileMap {

    public abstract Block[] getBlocks();
    public abstract void render(Graphics2D g, AABB cam);
}
