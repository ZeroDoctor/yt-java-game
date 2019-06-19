package com.zerulus.game.tiles.blocks;

import java.awt.Graphics2D;


import com.zerulus.game.graphics.Sprite;
import com.zerulus.game.math.Vector2f;
import com.zerulus.game.math.AABB;

public class ObjBlock extends Block {
    
    public ObjBlock(Sprite img, Vector2f pos, int w, int h) {
        super(img, pos, w, h);
    }

    public boolean update(AABB p) {
        return true;
    }

    public Sprite getImage() {
        return img;
    }
    
    public boolean isInside(AABB p) {
        return false;
    }

    public void render(Graphics2D g){
        super.render(g);
    }

}
