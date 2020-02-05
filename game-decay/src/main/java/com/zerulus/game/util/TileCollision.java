package com.zerulus.game.util;

import com.zerulus.game.entity.Entity;
import com.zerulus.game.tiles.TileMapObj;
import com.zerulus.game.tiles.blocks.Block;
import com.zerulus.game.tiles.blocks.HoleBlock;

public class TileCollision {

    private Entity e;
    private int tileId;

    public TileCollision(Entity e) {
        this.e = e;
    }

    public boolean normalTile(float ax, float ay) {
        int xt;
        int yt;

        xt = (int) ( (e.getPos().x + ax) + e.getBounds().getXOffset()) / 64;
        yt = (int) ( (e.getPos().y + ay) + e.getBounds().getYOffset()) / 64;
        tileId = (xt + (yt * TileMapObj.height));

        if(tileId > TileMapObj.height * TileMapObj.width) tileId = (TileMapObj.height * TileMapObj.width) - 2;

        return false;
    }

    public boolean collisionTile(float ax, float ay) {
        if(TileMapObj.event_blocks != null) {
            int xt;
            int yt;

            for(int c = 0; c < 4; c++) {
                
                xt = (int) ( (e.getPos().x + ax) + (c % 2) * e.getBounds().getWidth() + e.getBounds().getXOffset()) / 64;
                yt = (int) ( (e.getPos().y + ay) + (c / 2) * e.getBounds().getHeight() + e.getBounds().getYOffset()) / 64;

                if(xt <= 0 || yt <= 0 || xt + (yt * TileMapObj.height) < 0 || xt + (yt * TileMapObj.height) > (TileMapObj.height * TileMapObj.width) - 2) {
                    return true;
                } 
                
                if(TileMapObj.event_blocks[xt + (yt * TileMapObj.height)] instanceof Block) {
                    Block block = TileMapObj.event_blocks[xt + (yt * TileMapObj.height)];
                    if(block instanceof HoleBlock) {
                        return collisionHole(ax, ay, xt, yt, block);
                    }
                    return block.update(e.getBounds());
                }
            }
        }

        return false;
    }

    public int getTile() { return tileId; }

    private boolean collisionHole(float ax, float ay, float xt, float yt, Block block) {
        int nextXt = (int) ((( (e.getPos().x + ax) + e.getBounds().getXOffset()) / 64) + e.getBounds().getWidth() / 64);
        int nextYt = (int) ((( (e.getPos().y + ay) + e.getBounds().getYOffset()) / 64) + e.getBounds().getHeight() / 64);

        if(block.isInside(e.getBounds())) {
            e.setFallen(true);
            return false;
        }
        else if((nextXt == yt + 1) || (nextXt == xt + 1) || (nextYt == yt - 1) || (nextXt == xt - 1)) {
            if(TileMapObj.event_blocks[nextXt + (nextYt * TileMapObj.height)] instanceof HoleBlock){
                Block nextblock = TileMapObj.event_blocks[nextXt + (nextYt * TileMapObj.height)];

                if(e.getPos().x + e.getBounds().getXOffset() > block.getPos().x 
                && e.getPos().y + e.getBounds().getYOffset() > block.getPos().y
                && nextblock.getWidth() + nextblock.getPos().x > e.getBounds().getWidth() + (e.getPos().x + e.getBounds().getXOffset())
                && nextblock.getHeight() + nextblock.getPos().y > e.getBounds().getHeight() + (e.getPos().y + e.getBounds().getYOffset())) {
                    e.setFallen(true);
                }
                return false;
            }
        }

        e.setFallen(false);
        return false;
    }
}
