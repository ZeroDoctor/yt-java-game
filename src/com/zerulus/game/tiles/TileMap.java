package com.zerulus.game.tiles;

import java.awt.Graphics2D;
import com.zerulus.game.tiles.blocks.Block;
import com.zerulus.game.util.AABB;

public abstract class TileMap {
    public abstract void render(Graphics2D g, AABB cam);

	public abstract Block[] getArray();
}
