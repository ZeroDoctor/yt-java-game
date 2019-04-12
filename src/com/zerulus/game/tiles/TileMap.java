package com.zerulus.game.tiles;

import com.zerulus.game.util.AABB;
import com.zerulus.game.graphics.Screen;
import com.zerulus.game.tiles.blocks.Block;

public abstract class TileMap {

    public abstract Block[] getBlocks();
    public abstract void render(Screen s, AABB cam);
}
